canvas_size(400,400).
brush(blackthinline).
brush_color(blackthinline,black).
brush_width(blackthinline,1).

brush(blackthickline).
brush_color(blackthickline,black).
brush_width(blackthickline,3).

brush(blackvthickline).
brush_color(blackvthickline,black).
brush_width(blackvthickline,8).

brush(blackthinline).
brush_color(blackthinline,black).
brush_width(blackthinline,1).

brush(redthinline).
brush_color(redthinline,red).
brush_width(redthinline,1).

brush(greenthinline).
brush_color(greenthinline,green).
brush_width(greenthinline,1).

brush(bluethinline).
brush_color(bluethinline,blue).
brush_width(bluethinline,1).

draw_line(blackvthickline,p(10,10), p(30,10)).
draw_line(blackthickline,p(10,20), p(30,20)).
draw_line(blackthinline,p(10,30), p(30,30)).
draw_line(redthinline,p(10,40), p(30,40)).
draw_line(greenthinline,p(10,50), p(30,50)).
draw_line(bluethinline,p(10,60), p(30,60)).

color(lightgrey,rgb(128,128,128)).

draw_rect(blackthickline,p(50,10),30,30).

fill_rect(blackthickline,lightgrey,p(100,10),30,30).


draw_ellipse(blackthinline,p(10,100),40,30).
draw_ellipse(blackthinline,p(60,100),30,40).

fill_ellipse(blackthinline,green,p(10,150),40,30).
fill_ellipse(blackthinline,blue,p(60,150),30,40).

fill_ellipse(none,red,p(90,150),40,30).

font(redbold). 

font_style(redbold,bold).
font_color(redbold,red). 
font_size(redbold,14). 
draw_text(redbold,c,c,p(10,200),"This should be 14pt bold red").