NAME=calabash-basex
VER=0.4.0
DIST=$(NAME)-$(VER)
PROJ=../calabash-basex

pkg:  $(DIST).xar
dist: $(DIST).zip

$(DIST).xar:
	( cd $(PROJ) && ant )
	mkdir content
	cp $(PROJ)/dist/calabash-basex.jar content/$(NAME).jar
	cp $(PROJ)/lib/basex-7.9.jar content/
	cp calabash-basex-lib.xpl content/
	zip -r $(DIST).xar expath-pkg.xml calabash.xml cxan.xml content/
	rm -rf content/

$(DIST).zip: $(DIST).xar
	mkdir $(DIST)
	cp $(DIST).xar $(DIST)/
	echo "Version: $(VER)" > $(DIST)/VERSION
	echo "Git revision: `git describe --always`" >> $(DIST)/VERSION
	mkdir $(DIST)/samples
	cp ../samples/Makefile           $(DIST)/samples/
	cp ../samples/create-tests.basex $(DIST)/samples/
	cp ../samples/server.xproc       $(DIST)/samples/
	cp ../samples/standalone.xproc   $(DIST)/samples/
	zip -r $(DIST).zip $(DIST)/
	rm -rf $(DIST)/
