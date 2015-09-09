%% su-doku-clean.lp
%%
%% Copyright Martin Brain 20/10/06, see COPYING for licence
%% mjb@cs.bath.ac.uk
%% 15/10/04
%%
%% A generic program for solving the Su-Duko puzzle.
%% It is a description of a solution rather than how to find one.
%% I am told that it can be done without backtracking
%% Thus this program should not branch
#const sub=3.

%% The constant sub must be declared in the data - it is the dimensions of one sub square
%% In the obvious cases this is 3
subSize(sub).    % Declared as a literal so the decode script can easily access the size

%% Position gives a two dimensional index for each square
position(1 .. sub*sub).

#domain position(X).
#domain position(XA).
#domain position(XB).
#domain position(Y).
#domain position(YA).
#domain position(YB).


%% Each square may take a range of values
value(1 .. sub*sub).

#domain value(N).
#domain value(NA).
#domain value(NB).
%% each initial position fills the square X,Y with the number N
state(X,Y,N):- initial(X,Y,N).
%% Each square may take any value
1 { state(X,Y,NU) : value(NU) } 1 .

%% A number cannot appear twice in a column
:- state(XA,Y,N), state(XB,Y,N), XA != XB.

%% A twice in a row
:- state(X,YA,N), state(X,YB,N), YA != YB.

%% Define the concept of sub squares
sameSubSquare(NA,NB) :- A = (NA - 1) / sub, B = (NB - 1) / sub, A = B.

%% Now say there may not be two of the same number in the same subsquare
 :- state(XA,YA,N), state(XB,YB,N), sameSubSquare(XA,XB), sameSubSquare(YA,YB), XA != XB, YA != YB.

