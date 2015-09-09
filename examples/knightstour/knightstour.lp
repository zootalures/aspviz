% knights.lp
%
% Copyright Martin Brain 16/10/06, see COPYING for licence
% mjb@cs.bath.ac.uk
% 07/10/06
%
% Knight's tour
#const t=35.
#const xmax=6.
#const ymax=6.
xpos(1..xmax).
ypos(1..ymax).

#domain xpos(X).
#domain xpos(X1).
#domain xpos(X2).

#domain ypos(Y).
#domain ypos(Y1).
#domain ypos(Y2).


% Time steps
time(1..t).

#domain time(T).
#domain time(T1).
#domain time(T2).


% Valid moves
validMove(T,X + 2,Y + 1) :- position(T,X,Y), xpos(X + 2), ypos(Y + 1).  
validMove(T,X + 2,Y - 1) :- position(T,X,Y), xpos(X + 2), ypos(Y - 1).  
validMove(T,X + 1,Y + 2) :- position(T,X,Y), xpos(X + 1), ypos(Y + 2).  
validMove(T,X + 1,Y - 2) :- position(T,X,Y), xpos(X + 1), ypos(Y - 2).  
validMove(T,X - 1,Y + 2) :- position(T,X,Y), xpos(X - 1), ypos(Y + 2).  
validMove(T,X - 1,Y - 2) :- position(T,X,Y), xpos(X - 1), ypos(Y - 2).  
validMove(T,X - 2,Y + 1) :- position(T,X,Y), xpos(X - 2), ypos(Y + 1).  
validMove(T,X - 2,Y - 1) :- position(T,X,Y), xpos(X - 2), ypos(Y - 1).  

% At every time step choose a move
1 { choosenMove(T,XN,YN) : xpos(XN) : ypos(YN) } 1 :- time(T).
 :- choosenMove(T,X,Y), not validMove(T,X,Y).

% Then make it
position(T+1,X,Y) :- choosenMove(T,X,Y).

% Can't return to any place
 :- position(T1,X,Y), position(T2,X,Y), T1 < T2.
 :- position(T,X,Y), position(t+1,X,Y).

% Got to start somewhere
position(1,1,1).

%#hide time(T).
%#hide choosenMove(T,X,Y).
