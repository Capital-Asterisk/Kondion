uniform int eggs;

varying vec4 texCoord;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * eggs);
}

void main(){
	float s = floor(texCoord.s * 64) / 64;
	float t = floor(texCoord.t * 64) / 64;
	float b = rand(vec2(s, t));
    gl_FragColor = vec4(b, b, b, 1.0);
}