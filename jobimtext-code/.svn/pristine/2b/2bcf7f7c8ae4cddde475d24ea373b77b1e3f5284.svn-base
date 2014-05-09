SET wordb.name 'WordCountCount $input $wordcountout $featurecountout $contextout'

SET default_parallel 100;
SET mapred.wordb.map.memory.mb 2048
SET mapred.wordb.reduce.memory.mb 2048
set output.compression.enabled true;
set output.compression.codec org.apache.hadoop.io.compress.GzipCodec;

SET mapred.output.compress true;
SET mapred.output.compression.codec org.apache.hadoop.io.compress.GzipCodec;
SET mapred.compress.map.output true;
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

A = LOAD '$input' USING PigStorage('\t')  as (word: chararray, context: chararray, s_id: chararray, word_id: chararray, context_id: chararray);
JG = GROUP A BY (word,s_id,word_id);
WORD = FOREACH JG GENERATE  $0.word, 1;
WORDG =GROUP WORD BY word;
WORDGC = FOREACH WORDG GENERATE group, COUNT($1);
STORE WORDGC INTO '$wordcountout' USING PigStorage('\t');

BG = GROUP A BY (context,s_id,context_id);
CONTEXT = FOREACH BG GENERATE  $0.context, 1;
CONTEXTG =GROUP CONTEXT BY context;
CONTEXTGC = FOREACH CONTEXTG GENERATE group, COUNT($1);
STORE CONTEXTGC INTO '$featurecountout' USING PigStorage('\t');

JBG = GROUP A BY (word,context,s_id,context_id,word_id);
WORDCONTEXT = FOREACH JBG GENERATE  $0.word,$0.context, 1;
WORDCONTEXTG =GROUP WORDCONTEXT BY (word,context);
WORDCONTEXTGC = FOREACH WORDCONTEXTG GENERATE $0.word,$0.context, COUNT($1);
STORE WORDCONTEXTGC INTO '$contextout' USING PigStorage('\t');