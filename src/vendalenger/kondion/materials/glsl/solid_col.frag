#version 120

uniform int type;
uniform float fog;
uniform vec4 color;

uniform sampler2D texture1;

varying vec3 normal;
varying vec4 texCoord;
varying vec4 viewPos;
varying mat4 cuteMatrix;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 5434);
}

void main(){
	vec3 final = vec3(0.0, 0.0, 0.0);
	
	if (type == 0) {
		// Default render
		float s = floor(texCoord.s * 200) / 200;
		float t = floor(texCoord.t * 200) / 200;
		float b = 0;
		float eggs = dot(normalize(gl_ModelViewMatrix * vec4(normal, 0.0)), -vec4(0.0, -1.0, 0.0, 0.0));
		b += (eggs + 1) / 2;
	    //final = vec3(texture2D(texture1, texCoord.st).xyz) * vec3(b, b, b);
	    final = color.xyz * vec3(b, b, b);
	    if (fog != 0.0) {
		    final = mix(vec3(1.0, 1.0, 1.0), final, clamp(1.0 / exp(length(viewPos) * fog), 0.0, 1.0));
	    }
    } else if (type == 1) {
    	final = color.xyz;
    } else if (type == 3) {
    	final = (gl_ModelViewMatrix * vec4(normal, 0.0)).xyz;
    }
    
    if (type != 30) {
    	gl_FragData[0] = vec4(final, 1.0);
    } else {
    	//vec2 coord = texCoord.xy;
		//coord.s = mod(coord.s + 0.2, 1.0);
    	gl_FragData[0] = color;
    	gl_FragData[1] = vec4(((mat3(gl_ModelViewMatrix) * normal) + 1.0) / 2, 1.0);
    	gl_FragData[2] = vec4(0.0, 0.0, 0.0, 1.0);
    	gl_FragData[3] = vec4(0.0, 0.0, 0.0, 1.0);
    }
}