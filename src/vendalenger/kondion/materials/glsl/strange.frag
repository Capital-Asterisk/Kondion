#version 120

uniform int eggs = 0;
uniform int fuk = 1;

varying vec4 texCoord;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
	int amt = 256;
	if (fuk == 1)
    	gl_FragColor = vec4(sin(float(eggs) / 200),
    	rand(vec2(floor(texCoord.s * amt + eggs) / amt, floor(texCoord.t * amt + eggs) / amt)),
    	rand(vec2(floor(texCoord.s * amt + eggs) / amt, floor(texCoord.t * amt + eggs) / amt)), 1.0);
    else
    	gl_FragColor = vec4(sin(float(eggs) / 200), rand(vec2(gl_FragCoord) + eggs), rand(vec2(gl_FragCoord) + eggs), 1.0);
}