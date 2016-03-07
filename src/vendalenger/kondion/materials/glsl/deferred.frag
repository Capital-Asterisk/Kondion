#version 120

uniform int type;
uniform float fog;
uniform vec4 color;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;

varying vec3 normal;
varying vec4 texCoord;
varying vec4 viewPos;
varying vec4 worldPos;
varying mat4 cuteMatrix;

void main(){
	vec4 final = vec4(0.0, 0.0, 0.0, 0.0);
	final = mix(texture2D(texture0, texCoord.st), vec4(0.0, 0.0, 0.0, 1.0), clamp(vec4(pow(texture2D(texture1, texCoord.st).r, 900)), 0.0, 1.0));
	if (texture2D(texture1, texCoord.st).r == 1.0) {
		final = vec4(0.6, 0.6, 1.0, 1.0);
	}
	//final.x -= mod(gl_FragCoord.y / 20, 2);
    //gl_FragColor = texture2D(texture0, texCoord.st) * texture2D(texture2, texCoord.st) * texture2D(texture1, texCoord.st);
    //gl_FragColor -= vec4(floor(mod(gl_FragColor.x / 2, 2)), 0.0, 0.0, 0.0);
    //final = clamp(1 - vec4(pow(texture2D(texture1, texCoord.st).r, 80)), 0.0, 1.0);
    gl_FragColor = final;
}