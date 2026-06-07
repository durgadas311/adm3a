# This must build both plugin and standalone app(s).
# would like to avoid having duplicates of classes
# in ../sim, but standalone JAR needs them copied.
# Perhaps 'jdeps' can help with this.
CLASS_PATH = .

# For the plugin, these must come from the parent
SIM_CLASS = GenericHelp.class \
	SimResource.class \
	ExtBoolean.class \
	ParityGenerator.class

CORE_CLASS = ADM3ACrtScreen.class \
	ADM3A.class \
	BezelRoundedRectangle.class \
	TermContainer.class \
	Beep.class \
	Paster.class \
	Typer.class

JARS = ADM3Atelnet.jar ADM3Aplugin.jar

AUXS = $(wildcard doc/*) $(wildcard fonts/*.ttf) $(wildcard *.wav)

all: $(CLASSES) $(SIM_CLASS)

%.class: %.java
	javac -cp $(CLASS_PATH) $?

#ADM3Atty.class: ADM3Atty.java $(JSERIAL)
#	javac -cp .:$(JSERIAL) $<

jar: $(JARS)

ADM3Atelnet.jar: ADM3Atelnet.class $(CORE_CLASS) $(SIM_CLASS) $(AUXS)
	echo "Main-Class: ADM3Atelnet" >Manifest.txt
	jar -cmf Manifest.txt $@ $^

ADM3Atty.jar: $(CLASSES) $(SIM_CLASS) $(AUXS)
	echo "Main-Class: ADM3Atty" >Manifest.txt
	jar -cmf Manifest.txt $@ *.class $(AUXS)

# Need to exclude ADM3Atelnet.class...
ADM3Aplugin.jar: $(CORE_CLASS) $(AUXS)
	jar -cMf $@ $^

ship: jar
	cp ADM3Atelnet.jar ..

__FRC__:
