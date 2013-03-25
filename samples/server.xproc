<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
                xmlns:c="http://www.w3.org/ns/xproc-step"
                xmlns:basex="http://fgeorges.org/ns/calabash/basex"
                name="pipeline"
                version="1.0">

   <p:output port="result" primary="true"/>

   <p:import href="http://fgeorges.org/calabash/basex.xpl"/>

   <basex:server-query
       host="localhost" port="19984"
       user="admin"     password="admin"
       database="tests">
      <p:input port="source">
         <p:inline>
            <c:query>doc('tests/hello/world.xml')</c:query>
         </p:inline>
      </p:input>
   </basex:server-query>

</p:declare-step>
