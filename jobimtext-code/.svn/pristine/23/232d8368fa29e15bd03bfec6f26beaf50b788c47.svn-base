-- n = sum of all (word,feature) pair counts

-- setting the number of parallel mapred tasks
SET default_parallel 100;
SET mapred.job.map.memory.mb 2048
SET mapred.job.reduce.memory.mb 2048

SET mapred.compress.map.output true
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

SET mapred.output.compress true;
SET mapred.output.compression.codec org.apache.hadoop.io.compress.GzipCodec;
SET mapred.compress.map.output true;
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

-- load output of Context class
A = LOAD '$contextout' USING PigStorage('\t')  as (word: chararray, feature: chararray, bothcount: float);

-- t >= 2 ("t" is a command line parameter)
A2 = FILTER A BY bothcount >= $t;

-- group (I don't know why this is necessary) --> userwise it is nood guaranteed that you have a uniq structure
B = group A2 all;

-- calculate total count of all (word,feature) pairs
Z = foreach B generate SUM(A2.bothcount) as total;

-- the total count (n) is now in Z.total

-- A = LOAD '$contextout' USING PigStorage('\t')  as (word: chararray, feature: chararray, bothcount: float);
B = LOAD '$wordcountout' as (word: chararray, wordcount: float);
C = LOAD '$featurecountout' as (feature: chararray, featurecount: float);
D = JOIN A2 BY word, B BY word;
E = JOIN D BY feature, C BY feature;

-- E: word feature bothcount word wordcount feature featurecount

F = FOREACH E GENERATE A2::word, A2::feature, ( bothcount*LOG( (Z.total*bothcount)/(wordcount*featurecount) )/LOG(2) ) AS s;

-- s > 0
F2 = FILTER F BY s >= $s;

STORE F2 INTO '$freqsigout' USING PigStorage('\t');