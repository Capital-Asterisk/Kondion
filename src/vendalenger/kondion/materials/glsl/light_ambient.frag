#version 120

uniform float fog;
uniform vec4 color;

uniform sampler2D texture0; // Diffuse
uniform sampler2D texture1; // Depth
uniform sampler2D texture2; // Normals
uniform sampler2D texture3; // Brightness

varying vec3 normal;
varying vec4 texCoord;
varying vec4 viewPos;
varying vec4 worldPos;
varying mat4 cuteMatrix;

void main(){
    gl_FragData[0] = texture2D(texture0, texCoord.st);
    gl_FragData[1] = texture2D(texture2, texCoord.st);
    gl_FragData[2] = gl_FragData[2];
    gl_FragData[3] = texture2D(texture3, texCoord.st) + color;
}