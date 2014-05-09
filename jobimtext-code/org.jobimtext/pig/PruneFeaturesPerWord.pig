-- This is the mapreduce step to "prune" (optimize) the results given
-- by the FreqSig step (take only the most significant features per word).

-- setting the number of parallel mapred tasks
SET default_parallel 100;
SET mapred.job.map.memory.mb 2048
SET mapred.job.reduce.memory.mb 2048

set output.compression.enabled true;
set output.compression.codec org.apache.hadoop.io.compress.GzipCodec;

SET mapred.output.compress true;
SET mapred.output.compression.codec org.apache.hadoop.io.compress.GzipCodec;
SET mapred.compress.map.output true;
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

-- load the word feature count table
A = LOAD '$contextout' USING PigStorage('\t')  as (word1: chararray, feature1: chararray, bothcount1: int);
A2 = LOAD '$contextout' USING PigStorage('\t')  as (word2: chararray, feature2: chararray, bothcount2: int);

-- group by feature to determine how many words a feature shares
B = GROUP A BY (feature1);
C = FOREACH B GENERATE group as feature1, COUNT(A.word1) as word_count;
-- filter features with too many words
R = FILTER C BY word_count < $w;

J = JOIN R BY (feature1), A2 BY (feature2);
JF = FOREACH J GENERATE word2, feature1, bothcount2;
--store the reduced word_feature_counts
STORE JF INTO '$contextout_filter' USING PigStorage('\t');
