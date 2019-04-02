#version 330 core

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec4 inColor;

out VertexData {
    vec4 color;
} outputs;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * modelMatrix * vec4(inPosition, 1.0);
    outputs.color = inColor;
}
