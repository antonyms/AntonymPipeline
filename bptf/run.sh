#!/bin/sh
EXP_PATH="../data/combine"
rm -rf $EXP_PATH/combine_mm.*
./bptf --training=$EXP_PATH/combine_mm --debug=0 --quiet=0 --pmf_additional_output=1 \
    --max_iter=530 --pmf_burn_in=30 --D=40
