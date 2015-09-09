scale_canvas(3,3). translate_canvas(-5,-5).
brush(dflb).
brush_color(dflb,rgb(0,0,0)).
brush_width(dflb,1).


% draw islands and numbers
draw_ellipse(dflb,p(X*16,Y*16),13,13) :- blackCircle(X,Y,N).
draw_text(dflf,c,c,p(X*16,Y*16),N):- island(X,Y,N).

% draw single lines
draw_line(dflb,p(X*16,(Y*16)-8),p(X*16,(Y*16)+8)):- verticleBridge(X,Y), not doubleVerticle(X,Y).  
draw_line(dflb,p((X*16)-8,Y*16),p((X*16)+8,Y*16)) :- horizontalBridge(X,Y), not doubleHorizontal(X,Y).  

% draw double lines
draw_line(dflb,p((X*16)-8,Y*16+2),p((X*16)+8,Y*16+2)) :- doubleHorizontal(X,Y).
draw_line(dflb,p((X*16)-8,Y*16-2),p((X*16)+8,Y*16-2)) :- doubleHorizontal(X,Y).  
draw_line(dflb,p(X*16+2,(Y*16)-8),p(X*16+2,(Y*16)+8)):- doubleVerticle(X,Y).  
draw_line(dflb,p(X*16-2,(Y*16)-8),p(X*16-2,(Y*16)+8)):- doubleVerticle(X,Y).  
