<p:library xmlns:p="http://www.w3.org/ns/xproc"
           xmlns:basex="http://fgeorges.org/ns/calabash/basex"
           xmlns:pkg="http://expath.org/ns/pkg"
           pkg:import-uri="http://fgeorges.org/calabash/basex.xpl"
           version="1.0">

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

</p:library>
