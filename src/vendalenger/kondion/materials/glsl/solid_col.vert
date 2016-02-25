#version 120

// A cute vertex shader with many purposes

varying vec3 normal;
varying vec4 texCoord;
varying vec4 viewPos;
varying mat4 cuteMatrix;


float rand(vec2 co){
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 5434);
}

void main(){
	texCoord = gl_MultiTexCoord0;
	normal = gl_Normal;
	cuteMatrix = gl_ModelViewMatrix;
	viewPos = gl_ModelViewProjectionMatrix * gl_Vertex;
    //gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + vec4(normal * 3, 0.0));
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}