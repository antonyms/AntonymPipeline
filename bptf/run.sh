#!/bin/sh
./bptf --training=../data/bptf/roget_mm --debug=0 --quiet=0 --max_iter=80 --pmf_burn_in=79 --pmf_additional_output=1 --D=40
#./pmf --training=small_mm --debug=0 --quiet=0 --max_iter=10 --pmf_burn_in=5 --pmf_additional_output=0
#./toolkits/collaborative_filtering/pmf_self --training=smallnetflix_mm --quiet=1 --minval=1 --maxval=5 --max_iter=10 --pmf_burn_in=5
