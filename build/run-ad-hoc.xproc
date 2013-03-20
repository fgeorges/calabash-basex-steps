<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
                xmlns:c="http://www.w3.org/ns/xproc-step"
                xmlns:basex="http://fgeorges.org/ns/calabash/basex"
                name="pipeline"
                version="1.0">

   <p:output port="result" primary="true"/>

   <p:import href="http://fgeorges.org/calabash/basex.xpl"/>

   <basex:ad-hoc-query>
      <p:input port="source">
         <p:inline>
            <c:query>
               &lt;res> { 1 + 1 } &lt;/res>
            </c:query>
         </p:inline>
      </p:input>
   </basex:ad-hoc-query>

</p:declare-step>
