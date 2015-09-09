% An incorrect initial configuration that has multiple solutions

#const sub=3.

initial(2,1,4).
initial(2,2,8).
initial(2,5,3).
initial(2,6,9).

initial(3,2,7).
initial(3,4,2).
initial(3,6,4).
initial(3,8,3).
initial(3,9,1).

initial(4,2,9).
initial(4,3,4).
initial(4,7,2).
initial(4,8,6).

initial(5,1,8).
initial(5,9,9).

initial(6,2,2).
initial(6,3,7).
initial(6,7,5).
initial(6,8,4).

initial(7,1,5).
initial(7,2,4).
initial(7,4,9).
initial(7,6,3).
initial(7,8,8).

initial(8,4,6).
initial(8,5,2).


% Infered

initial(5,6,2).
initial(6,5,9).
#show initial(X,Y,Z).