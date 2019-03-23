#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 inColor;

out vec4 color;

uniform mat4 projectionMatrix;

void main() {
    gl_Position = vec4(position, 1.0);
    color = vec4(1, 1, 1, 1);
}
