import os
import sys

dataset = sys.argv[1]
s  = sys.argv[2]
t  = sys.argv[3]
p  = sys.argv[4]
significance  = sys.argv[5]
simsort_limit = sys.argv[6]


wc = "CREATE TABLE word_count ( "\
"  word varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  count int(11) NOT NULL DEFAULT '0',"\
"  KEY w (word)"\
") ENGINE=MyISAM DEFAULT CHARSET=utf8;";

fc = "CREATE TABLE feature_count ("\
"  feature varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  count int(11) NOT NULL DEFAULT '0',"\
"  KEY f (feature)"\
") ENGINE=MyISAM DEFAULT CHARSET=utf8;";


wfs="CREATE TABLE "+significance+"_"+p+" ("\
"  word varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  feature varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  sig double NOT NULL DEFAULT '0',"\
"  KEY w (word),"\
"  KEY f (feature)"\
") ENGINE=MyISAM DEFAULT CHARSET=utf8;";

sim=" CREATE TABLE "+significance+"_"+p+"_l"+simsort_limit+" ("\
"  word1 varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  word2 varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',"\
"  count int(8) unsigned NOT NULL DEFAULT '0',"\
"  KEY w1 (word1),"\
"  KEY w2 (word2)"\
") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
print wc
print fc
print wfs
print sim
#LOAD DATA INFILE "/srv/data/jobimtext/google_syntax_ngram/google_books_input__FeatureCount" INTO TABLE feature_count  ;
#LOAD DATA INFILE "/srv/data/jobimtext/google_syntax_ngram/google_books_input__FreqSigLMI_s_0_t_0" INTO TABLE LMI_p1000;
#LOAD DATA INFILE "/srv/data/jobimtext/google_syntax_ngram/google_books_input__FreqSigLMI_s_0_t_0__PruneGraph_p_1000__AggrPerFt__SimCounts1WithFeatures__SimSortlimit_200" INTO TABLE LMI_p1000_l200;
#LOAD DATA INFILE "/srv/data/jobimtext/google_syntax_ngram/google_books_input__WordCount" INTO TABLE word_count;
