#version 330 core

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec4 inColor;

out VertexData {
    vec4 color;
} outputs;

uniform mat4 projectionMatrix;

void main() {
    gl_Position = vec4(inPosition, 1.0);
    outputs.color = inColor;
}
