NAME=calabash-basex-steps
VER=0.1.0
DIST=$(NAME)-$(VER)

pkg:  $(DIST).xar
dist: $(DIST).zip

$(DIST).xar:
	( cd ../basex-steps && ant )
	mkdir content
	cp ../basex-steps/dist/basex-steps.jar content/$(NAME).jar
	cp ../basex-steps/lib/basex-7.6.jar content/
	cp calabash-basex-lib.xpl content/
	zip -r $(DIST).xar expath-pkg.xml calabash.xml content/
	rm -rf content/

$(DIST).zip: $(DIST).xar
	mkdir $(DIST)
	cp $(DIST).xar $(DIST)/
	echo $(VER) > $(DIST)/VERSION
	mkdir $(DIST)/samples
	cp run-ad-hoc.xproc $(DIST)/samples/
	zip -r $(DIST).zip $(DIST)/
	rm -rf $(DIST)/
