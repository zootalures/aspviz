point(1..10).
brush(dflb).

fill_ellipse(dflb,black,p(X*10-5,Y*10-5),10,10):- point(X;Y), X mod 2 ==0 , Y mod 2 ==1.
fill_ellipse(dflb,black,p(X*10-5,Y*10-5),10,10):- point(X;Y), X mod 2 ==1 ,  Y mod 2 ==0.

fill_ellipse(dflb,rgb(255,255,255),p(X*10-5,Y*10-5),10,10):- point(X;Y), 
						  X mod 2 ==1 , Y mod 2 ==1.
fill_ellipse(dflb,white,p(X*10-5,Y*10-5),10,10):- point(X;Y), X mod 2
==0 ,  Y mod 2 ==0.

color(lightgray,rgb(192,192,192)).
color(dkgray,rgb(96,96,96)).

isframe(0..9).
1{frame(N):isframe(N)}1.

fill_rect(dflb,dkgray,p(X*10+V-5,Y*10+V-5),10+V,10+V):- isframe(V),frame(V),point(X;Y),X mod 2 ==0 , Y mod 2 ==1.
fill_rect(dflb,dkgray,p(X*10+V-5,Y*10+V-5),10+V,10+V):- isframe(V),frame(V),  point(X;Y), X mod 2 ==1 ,  Y mod 2 ==0.
