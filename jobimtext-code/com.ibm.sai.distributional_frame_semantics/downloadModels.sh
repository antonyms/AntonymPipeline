mkdir models
cd models
wget http://search.maven.org/remotecontent?filepath=com/clearnlp/clearnlp-dictionary/1.0/clearnlp-dictionary-1.0.jar
wget http://search.maven.org/remotecontent?filepath=com/clearnlp/clearnlp-general-en-pos/1.1/clearnlp-general-en-pos-1.1.jar
wget http://search.maven.org/remotecontent?filepath=com/clearnlp/clearnlp-general-en-dep/1.2/clearnlp-general-en-dep-1.2.jar
wget http://search.maven.org/remotecontent?filepath=com/clearnlp/clearnlp-general-en-srl/1.1/clearnlp-general-en-srl-1.1.jar

mv remotecontent\?filepath\=com%2Fclearnlp%2Fclearnlp-dictionary%2F1.0%2Fclearnlp-dictionary-1.0.jar clearnlp-dictionary-1.0.jar 
mv remotecontent\?filepath\=com%2Fclearnlp%2Fclearnlp-general-en-pos%2F1.1%2Fclearnlp-general-en-pos-1.1.jar  clearnlp-general-en-pos-1.1.jar 
mv remotecontent\?filepath\=com%2Fclearnlp%2Fclearnlp-general-en-dep%2F1.2%2Fclearnlp-general-en-dep-1.2.jar  clearnlp-general-en-dep-1.2.jar 
mv remotecontent\?filepath\=com%2Fclearnlp%2Fclearnlp-general-en-srl%2F1.1%2Fclearnlp-general-en-srl-1.1.jar   clearnlp-general-en-srl-1.1.jar 