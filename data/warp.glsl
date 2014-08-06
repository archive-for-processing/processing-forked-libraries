#define PROCESSING_COLOR_SHADER

#ifdef GL_ES
precision mediump float;
#endif




	uniform float time;
	uniform vec2 resolution;
	uniform float detail;
	uniform float complexity;
	





float rand(vec2 p){
p+=.2127+p.x+.3713*p.y;
vec2 r=4.789*sin(789.123*(p));
return fract(r.x*r.y);}

float sn(vec2 p){
vec2 i=floor(p-.5);
vec2 f=fract(p-.5);
f = f*f*f*(f*(f*6.0-15.0)+10.0);
float rt=mix(rand(i),rand(i+vec2(1.,0.)),f.x);
float rb=mix(rand(i+vec2(0.,1.)),rand(i+vec2(1.,1.)),f.x);
return mix(rt,rb,f.y);}


float fbm (vec2 p, float _time){
float value =
.5*sn(p+_time)
+.25*sn(2.*p-_time)
+.125*sn(4.*p+_time)
+.0625*sn(8.*p-_time)
+.03125*sn(16.*p+_time)
+.015*sn(32.*p-_time);

return value;
}

float warp(vec2 p, float _time, float _complexity) {

 
if (_complexity == 0.0)
	return fbm( p + 4.0 , _time);

if (_complexity == 1.0){
	vec2 q = vec2( fbm( p + vec2(0.0,0.0) ,_time),
	fbm( p + vec2(5.2,1.3), _time ) );
	return fbm( p + 4.0*q , _time);
	}
if (_complexity == 2.0){
	vec2 q = vec2( fbm( p + vec2(0.0,0.0) ,_time),
	fbm( p + vec2(5.2,1.3), _time ) );
	
	vec2 r = vec2( fbm( p + 4.0*q + vec2(1.7,4.2), _time),
	fbm( p + 4.0*q + vec2(2.3,2.8), _time ) );                         
	
	return fbm( p + 4.0*r , _time);
	}
	
if (_complexity >= 3.0){
	vec2 q = vec2( fbm( p + vec2(0.0,0.0) ,_time),
	fbm( p + vec2(5.2,1.3), _time ) );
	
	vec2 r = vec2( fbm( p + 4.0*q + vec2(1.7,4.2), _time),
	fbm( p + 4.0*q + vec2(2.3,2.8), _time ) );  
	
	vec2 s = vec2( fbm( p + 4.0*r + vec2(1.5,2.1), _time),
	fbm( p + 4.0*r + vec2(2.2,3.7), _time ) );                                             
	
	return fbm( p + 4.0*s , _time);
	}
	
}
	
	
	
	
void main(void) {
	vec2 p = gl_FragCoord.xy*detail;
	vec4 f = vec4(vec3(
	warp(p,time, complexity)
	),1.);
	
	gl_FragColor = f;
	
	
	
	}
	
	
	
	
	