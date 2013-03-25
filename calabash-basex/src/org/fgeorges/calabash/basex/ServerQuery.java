/****************************************************************************/
/*  File:       ServerQuery.java                                            */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2013-03-24                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2013 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.fgeorges.calabash.basex;

import com.xmlcalabash.core.XProcException;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.ReadablePipe;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.runtime.XAtomicStep;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import org.basex.core.BaseXException;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.XQuery;
import org.basex.server.ClientSession;


/**
 * Sample extension step to evaluate a query using BaseX.
 *
 * @author Florent Georges
 * @date   2013-03-24
 */
public class ServerQuery
        extends DefaultStep
{
    public ServerQuery(XProcRuntime runtime, XAtomicStep step)
    {
        super(runtime,step);
    }

    @Override
    public void setInput(String port, ReadablePipe pipe)
    {
        mySource = pipe;
    }

    @Override
    public void setOutput(String port, WritablePipe pipe)
    {
        myResult = pipe;
    }

    @Override
    public void reset()
    {
        mySource.resetReader();
        myResult.resetWriter();
    }

    @Override
    public void run()
            throws SaxonApiException
    {
        super.run();
        Options options = new Options(this);
        ClientSession basex = connect(options);
        try {
            // TODO: There should be something more efficient than serializing
            // everything and parsing it again...  Besides, if the result is not an
            // XML document, wrap it into a c:data element. See Christian's comment on
            // http://fgeorges.blogspot.be/2011/09/writing-extension-step-for-calabash-to.html.
            String result = execute(basex, mySource);
            serialize(result, myResult);
        }
        finally {
            disconnect(basex, options);
        }
    }

    /**
     * Connect to the BaseX server identified in the options, opening the correct database.
     */
    private ClientSession connect(Options options)
    {
        ClientSession basex;
        try {
            String host = options.host;
            int    port = options.port;
            String user = options.user;
            String pwd  = options.pwd;
            basex = new ClientSession(host, port, user, pwd);
        }
        catch ( IOException ex ) {
            StringBuilder msg = new StringBuilder("Error connecting to BaseX, using host=");
            msg.append(options.host);
            msg.append(", port=");
            msg.append(Integer.toString(options.port));
            msg.append(", user=");
            msg.append(options.user);
            msg.append(".");
            throw new XProcException(msg.toString(), ex);
        }
        try {
            basex.execute(new Open(options.db));
        }
        catch ( IOException ex ) {
            StringBuilder msg = new StringBuilder("Error opening the database '");
            msg.append(options.db);
            msg.append("' on '");
            msg.append(options.host);
            msg.append(":");
            msg.append(Integer.toString(options.port));
            msg.append("' with user '");
            msg.append(options.user);
            msg.append("'.");
            throw new XProcException(msg.toString(), ex);
        }
        return basex;
    }

    /**
     * Close the BaseX client, if not null.
     */
    private void disconnect(ClientSession basex, Options options)
    {
        try {
            if ( basex != null ) {
                basex.close();
            }
        }
        catch ( IOException ex ) {
            StringBuilder msg = new StringBuilder("Error disconnecting from BaseX, connected with host=");
            msg.append(options.host);
            msg.append(", port=");
            msg.append(Integer.toString(options.port));
            msg.append(", user=");
            msg.append(options.user);
            msg.append(".");
            throw new XProcException(msg.toString(), ex);
        }
    }

    /**
     * Read the source port, extract its string value as a query, and execute it on BaseX.
     */
    private String execute(ClientSession basex, ReadablePipe pipe)
            throws SaxonApiException
    {
        try {
            XdmNode content   = mySource.read();
            String  query_str = content.getStringValue();
            XQuery  query     = new XQuery(query_str);
            return basex.execute(query);
        }
        catch ( BaseXException ex ) {
            throw new XProcException("Error executing a query with BaseX", ex);
        }
        catch ( IOException ex ) {
            throw new XProcException("Error executing a query with BaseX", ex);
        }
    }

    /**
     * Serialize the result to the result port.
     */
    private void serialize(String result, WritablePipe pipe)
            throws SaxonApiException
    {
        DocumentBuilder builder = runtime.getProcessor().newDocumentBuilder();
        Source src = new StreamSource(new StringReader(result));
        XdmNode doc = builder.build(src);
        myResult.write(doc);
    }

    /**
     * The source port.
     */
    private ReadablePipe mySource = null;

    /**
     * The result port.
     */
    private WritablePipe myResult = null;

    /**
     * Dedicated class to represent and parse the options of this step.
     */
    private static final class Options
    {
        public Options(DefaultStep step)
        {
            host = getMandatoryOption(step, HOST);
            user = getMandatoryOption(step, USER);
            pwd  = getMandatoryOption(step, PWD);
            db   = getMandatoryOption(step, DB);
            String port_str = getMandatoryOption(step, PORT);
            try {
                port = Integer.valueOf(port_str);
            }
            catch ( NumberFormatException ex ) {
                throw new XProcException("The option '" + PORT + "' must be an integer, got '" + port_str + "'.");
            }
        }

        public String getMandatoryOption(DefaultStep step, QName option)
        {
            RuntimeValue val = step.getOption(option);
            if ( val == null ) {
                throw new XProcException("The option '" + option + "' is mandatory on '" + step + "'.");
            }
            return val.getString();
        }

        private String host;
        private int    port;
        private String user;
        private String pwd;
        private String db;

        private static final QName HOST = new QName("host");
        private static final QName PORT = new QName("port");
        private static final QName USER = new QName("user");
        private static final QName PWD  = new QName("password");
        private static final QName DB   = new QName("database");
    }
}


/* ------------------------------------------------------------------------ */
/*  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS COMMENT.               */
/*                                                                          */
/*  The contents of this file are subject to the Mozilla Public License     */
/*  Version 1.0 (the "License"); you may not use this file except in        */
/*  compliance with the License. You may obtain a copy of the License at    */
/*  http://www.mozilla.org/MPL/.                                            */
/*                                                                          */
/*  Software distributed under the License is distributed on an "AS IS"     */
/*  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See    */
/*  the License for the specific language governing rights and limitations  */
/*  under the License.                                                      */
/*                                                                          */
/*  The Original Code is: all this file.                                    */
/*                                                                          */
/*  The Initial Developer of the Original Code is Florent Georges.          */
/*                                                                          */
/*  Contributor(s): none.                                                   */
/* ------------------------------------------------------------------------ */
