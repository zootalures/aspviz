dir(n;s;e;w).

nogo(psn(X,X)):- blocked(psn(X,X)), pos(X;Y).

0{move(T,D):time(T)}1:- time(T),dir(D).
:- move(T,D1),move(T,D2),D1!=D2, dir(D1;D2),time(T).
:- p(T,psn(X,Y)), nogo(psn(X,Y)), pos(X;Y),time(T).
:- move(T,D),p(T,psn(X,Y)),target(psn(X,Y)),pos(X;Y),dir(D),time(T).
:- p(T1,psn(X,Y)), p(T2,psn(X,Y)),T2<T1, not target(psn(X,Y)),pos(X;Y), time(T1;T2).
% movement vectors
d(e,1,0). d(w,-1,0). d(n,0,1). d(s,0,-1).
moved(T):- move(T,D),time(T),dir(D).
%move effect
p(T+1,psn(X+DX,Y+DY)):- p(T,psn(X,Y)),
	move(T,D), d(D,DX,DY),time(T),pos(X;Y).

% inertia
p(T+1,psn(X,Y)):- 	p(T,psn(X,Y)),
	not moved(T), time(T),pos(X;Y).
	
p(0,psn(X,Y)):- initial(psn(X,Y)),pos(X;Y).
correct :- p(T+1,Dest),target(Dest),time(T).
:- not correct.