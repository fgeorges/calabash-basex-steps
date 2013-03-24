/****************************************************************************/
/*  File:       BasexStandaloneQuery.java                                   */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2011-08-31                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2011 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.fgeorges.calabash.basex;

import com.xmlcalabash.core.XProcException;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.ReadablePipe;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.runtime.XAtomicStep;
import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.XQuery;


/**
 * Sample extension step to evaluate a query using BaseX.
 *
 * @author Florent Georges
 * @date   2011-08-31
 */
public class BasexStandaloneQuery
        extends DefaultStep
{
    public BasexStandaloneQuery(XProcRuntime runtime, XAtomicStep step)
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

        XdmNode query_doc = mySource.read();
        String query_txt = query_doc.getStringValue();
        XQuery query = new XQuery(query_txt);
        Context ctxt = new Context();
        // TODO: There should be something more efficient than serializing
        // everything and parsing it again...  Besides, if the result is not an
        // XML document, wrap it into a c:data element.
        String result;
        try {
            result = query.execute(ctxt);
        }
        catch ( BaseXException ex ) {
            throw new XProcException("Error executing a query with BaseX", ex);
        }
        DocumentBuilder builder = runtime.getProcessor().newDocumentBuilder();
        Source src = new StreamSource(new StringReader(result));
        XdmNode doc = builder.build(src);

        myResult.write(doc);
    }

    private ReadablePipe mySource = null;
    private WritablePipe myResult = null;
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
