-- This is the mapreduce step to "prune" (optimize) the results given
-- by the FreqSig step (take only the most significant features per word).

-- setting the number of parallel mapred tasks
SET default_parallel 100
SET mapred.job.map.memory.mb 2048
SET mapred.job.reduce.memory.mb 2048

SET mapred.compress.map.output true
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

SET mapred.output.compress true;
SET mapred.output.compression.codec org.apache.hadoop.io.compress.GzipCodec;
SET mapred.compress.map.output true;
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

-- load the output of FreqSig.pig
A = LOAD '$freqsigout' USING PigStorage('\t') AS (word: chararray, feature: chararray, sig: float, bothcount: float);

-- group by word because we want to limit lines per word
B = GROUP A BY word;

-- B looks like this
-- (president,{(president,barack#RF,117.3016),(president,obama#RF,157.76811),(president,vice#LF,223.11275)})

-- order by word first, second by significance
-- loop through each group and limit by significance
-- at last "ungroup" the line (this is done by FLATTEN)
-- C = FOREACH B {	D = ORDER A BY sig desc; E = LIMIT D $p; GENERATE FLATTEN(E);}
C = FOREACH B {	D = TOP($p, 2, A); GENERATE FLATTEN(D);}

-- output "table" into file (same format as output of FreqSig.pig)
STORE C INTO '$prunegraphout' USING PigStorage('\t');