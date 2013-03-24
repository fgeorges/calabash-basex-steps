NAME=calabash-basex
VER=0.2.0
DIST=$(NAME)-$(VER)
PROJ=../calabash-basex

pkg:  $(DIST).xar
dist: $(DIST).zip

$(DIST).xar:
	( cd $(PROJ) && ant )
	mkdir content
	cp $(PROJ)/dist/calabash-basex.jar content/$(NAME).jar
	cp $(PROJ)/lib/basex-7.6.jar content/
	cp calabash-basex-lib.xpl content/
	zip -r $(DIST).xar expath-pkg.xml calabash.xml cxan.xml content/
	rm -rf content/

$(DIST).zip: $(DIST).xar
	mkdir $(DIST)
	cp $(DIST).xar $(DIST)/
	echo $(VER) > $(DIST)/VERSION
	mkdir $(DIST)/samples
	cp run-ad-hoc.xproc $(DIST)/samples/
	zip -r $(DIST).zip $(DIST)/
	rm -rf $(DIST)/
