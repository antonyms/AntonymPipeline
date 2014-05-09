mvn install:install-file -Dfile=lib/com.ibm.sai.dca-VERSION.jar -DgroupId=com.ibm.sai.dca -DartifactId=com.ibm.ai.dca -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/crf-VERSION.jar -DgroupId=crf -DartifactId=crf -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/util-VERSION.jar -DgroupId=jobimtext.util -DartifactId=jobimtext.util -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/lemmatizer-VERSION.jar -DgroupId=jobimtext.lemmatizer -DartifactId=jobimtext.lemmatizer -Dversion=VERSION -Dpackaging=jar

mvn install:install-file -Dfile=lib/holing-VERSION.jar -DgroupId=jobimtext.holing -DartifactId=jobimtext.holing -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/thesaurus.distributional-VERSION.jar -DgroupId=jobimtext.thesaurus.distributional -DartifactId=jobimtext.thesaurus.distributional -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/thesaurus.distributional.hadoop-VERSION.jar -DgroupId=jobimtext.thesaurus.distributional.hadoop -DartifactId=jobimtext.thesaurus.distributional.hadoop -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/thesaurus.contextual-VERSION.jar -DgroupId=jobimtext.thesaurus.contextual -DartifactId=jobimtext.thesaurus.contextual -Dversion=VERSION -Dpackaging=jar
mvn install:install-file -Dfile=lib/examples-VERSION.jar -DgroupId=jobimtext.examples -DartifactId=jobimtext.examples -Dversion=VERSION -Dpackaging=jar

