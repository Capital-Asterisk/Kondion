uniform int fog = 1;
uniform int eggs;
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
	float s = floor(texCoord.s * 200) / 200;
	float t = floor(texCoord.t * 200) / 200;
	float b = 0;
	//b = rand(vec2(s, t)) / 2;
	// arg B is direction of light
	float eggs = dot(normalize(cuteMatrix * vec4(normal, 0.0)), -vec3(0.0, -1.0, 0.0));
	b += (eggs + 1) / 2;
	//b += 1 - gl_FragCoord.z;
	//b += gl_FragCoord.w;
    final = texture2D(texture1, texCoord.st) * vec4(b, b, b, 1.0);
    //gl_FragColor = vec4(normal, 1.0);
    
    if (fog != 0) {
	    final = mix(vec3(1.0, 1.0, 1.0), final, clamp(1.0 / exp(length(viewPos) * 0.001), 0.0, 1.0));
    }
    
    gl_FragColor = vec4(final, 1.0);
}