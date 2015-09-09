

#const gridx=2.
#const gridy=2.
#const gridz=2.

posx(0..gridx).
posy(0..gridy).
posz(0..gridz).

piece(a).
part(a,0,0,0).
part(a,0,1,0).
part(a,1,0,0).
part(a,2,0,0).


piece(b).
part(b,0,0,0).
part(b,1,0,0).
part(b,1,0,1).
part(b,2,0,0).
part(b,2,1,0).


piece(c).
part(c,0,0,0).
part(c,1,0,0).
part(c,1,0,1).
part(c,1,1,1).
part(c,2,1,1).

piece(d).
part(d,0,0,0).
part(d,1,0,0).
part(d,2,0,0).
part(d,2,1,0).
part(d,2,1,1).



piece(e).
part(e,0,0,0).
part(e,1,0,0).
part(e,0,1,0).
part(e,0,1,1).


piece(f).
part(f,0,0,0).
part(f,1,0,0).
part(f,1,1,0).
part(f,1,1,1).
