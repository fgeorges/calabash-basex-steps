BASEX_JAR=/Library/Java/org/basex/basex-7.6.jar
BASEX_HOME=-Dorg.basex.path=`pwd`/basex
BASEX_SRV=org.basex.BaseXServer
BASEX_CLT=org.basex.BaseXClient

run:
	rm -rf repo/
	rm -rf basex/
	xrepo create repo
	xrepo --repo repo install ../build/calabash-basex-0.3.0dev.xar
	calabash ++repo repo standalone.xproc
	mkdir basex/
	java -cp $(BASEX_JAR) $(BASEX_HOME) $(BASEX_SRV) -p19984 -e19985 &
	sleep 2
	java -cp $(BASEX_JAR) $(BASEX_HOME) $(BASEX_CLT) -p19984 \
	    -Uadmin -Padmin < create-tests.basex
	calabash ++repo repo server.xproc
	java -cp $(BASEX_JAR) $(BASEX_HOME) $(BASEX_SRV) -p19984 -e19985 stop

start-server:
	java -cp $(BASEX_JAR) $(BASEX_HOME) $(BASEX_SRV) -p19984 -e19985 &

stop-server:
	java -cp $(BASEX_JAR) $(BASEX_HOME) $(BASEX_SRV) -p19984 -e19985 stop
