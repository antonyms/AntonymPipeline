-- This is the mapreduce step to sort the simcount results, because there are so many
%default IN 'news10M__ContextDP_POS__FeatureCount__WordCount__FreqSig_s_10_t_10__PruneGraph_p_100__AggrPerFt__SimCounts'
--%default IN  'simcounts-out';
%default OUT 'simsort-out';

SET job.name 'SimSort $IN $OUT'

-- setting the number of parallel reduce tasks (has to be one because 
SET default_parallel 100; 

SET mapred.tasktracker.map.tasks.maximum 100;
SET mapred.map.tasks 100;

SET mapred.tasktracker.map 100;
SET mapred.job.map.memory.mb 4000;
SET mapred.job.reduce.memory.mb 4000;

SET dfs.replication 1;

SET io.sort.mb 300;
SET io.sort.factor 30;

SET mapred.compress.map.output true
SET mapred.output.compression org.apache.hadoop.io.compress.GzipCodec;

-- load the output of SimCounts
A = LOAD '$IN' USING PigStorage('\t') AS (word1: chararray, word2: chararray, sig: float);

-- group by word because we want to limit lines per word
B = GROUP A BY word1;

-- B looks like this
-- (president,{(president,barack#RF,117.3016),(president,obama#RF,157.76811),(president,vice#LF,223.11275)})

-- order by word first, second by significance
-- loop through each group and limit by significance
-- at last "ungroup" the line (this is done by FLATTEN)
C = FOREACH B { D = TOP($limit, 2, A); GENERATE FLATTEN(D); }
E = ORDER C BY word1, sig desc;

-- output "table" into file (same format as previous step)
STORE E INTO '$OUT' USING PigStorage('\t');