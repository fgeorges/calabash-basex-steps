# options
BASEX_JAR=/Library/Java/org/basex/basex-7.9.jar
BASEX_HOME=-Dorg.basex.path=`pwd`/basex
# commands
JAVA=java -cp $(BASEX_JAR) $(BASEX_HOME)
SERVER=$(JAVA) org.basex.BaseXServer -p19984 -e19985
CLIENT=$(JAVA) org.basex.BaseXClient -p19984 -Uadmin -Padmin

run:
	rm -rf repo/
	rm -rf basex/
	xrepo create repo
	xrepo --repo repo install ../build/calabash-basex-0.4.0.xar
	calabash ++repo repo standalone.xproc
	mkdir basex/
	$(SERVER) &
	sleep 2
	$(CLIENT) < create-tests.basex
	calabash ++repo repo server.xproc
	$(SERVER) stop

start-server:
	$(SERVER) &

stop-server:
	$(SERVER) stop
