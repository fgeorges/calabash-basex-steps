calabash-basex-steps
====================

Library of XProc steps (including extensions) to talk to BaseX from Calabash.

Installation
------------

Provided as a XAR package.  Download the latest package version from
[CXAN](http://cxan.org/pkg/fgeorges/calabash-basex) and install it in
you local repository for use by Calabash.  If you have a CXAN client,
all you need to do is:

```
cxan install fgeorges/calabash-basex
```

Once installed, to use it you simply use to use the following import
statement:

```
<p:import href="http://fgeorges.org/calabash/basex.xpl"/>
```

If you don't have the repository manager yet, install it first from
http://github.com/fgeorges/expath-pkg-java.

The steps
---------

This library defines 2 steps, in the namespace
`http://fgeorges.org/ns/calabash/basex`:

- `basex:standalone-query`
- `basex:server-query`

The former evaluates a query with the embedded BaseX engine.  The
latter evaluates a query on an external BaseX server (which is running
as an external process, on a specific host and a specific port).  See
the `samples` directory for examples of use.  The definition of the
steps is:

```
<p:declare-step type="basex:standalone-query">
   <p:input  port="source" primary="true"/>
   <p:output port="result" primary="true"/>
</p:declare-step>

<p:declare-step type="basex:server-query">
   <p:input  port="source" primary="true"/>
   <p:output port="result" primary="true"/>
   <p:option name="host"     required="true"/>
   <p:option name="port"     required="true"/>
   <p:option name="user"     required="true"/>
   <p:option name="password" required="true"/>
   <p:option name="database" required="true"/>
</p:declare-step>
```

Development
-----------

The source code is organized between the 3 following sub-directories:

- `build` contains various descriptor and a Makefile to create the packages
- `calabash-basex` is a NetBeans project, contains the Java classes
- `samples` contains simple examples of both steps
