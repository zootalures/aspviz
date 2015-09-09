#!/bin/bash

rm -rf outfile/*

mkdir outfile/simple
../../aspviz -v maze.aspviz -o outfile/simple maze-problem.lp maze.lp


mkdir outfile/broken
../../aspviz -v maze.aspviz  -n 1 -o outfile/broken maze-broken.lp maze-problem.lp


mkdir outfile/anim
../../aspviz -v maze-anim.aspviz -o outfile/anim maze-problem.lp maze.lp


mkdir outfile/combined-broken
../../aspviz  -C -v maze-combined.aspviz -o outfile/combined-broken maze-broken.lp maze-problem.lp

mkdir outfile/combined
../../aspviz  -C -v maze-combined.aspviz -o outfile/combined maze.lp maze-problem.lp

for i in outfile/*/*.svg; do
j=`echo $i | sed -e 's/svg/pdf/'`; 
echo $j; 
rm -f $j;
~occ/scripts/svg2pdf $i $j
done