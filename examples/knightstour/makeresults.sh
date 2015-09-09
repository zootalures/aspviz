#!/bin/bash

rm -rf outfile/*

mkdir -p  outfile/knightstour
../../aspviz -n1 -v  knightstour.aspviz -o outfile/knightstour knightstour.lp


for i in outfile/*/*.svg; do
j=`echo $i | sed -e 's/svg/pdf/'`; 
echo $j; 
rm -f $j;
~occ/scripts/svg2pdf $i $j
done