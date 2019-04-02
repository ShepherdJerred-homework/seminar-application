#version 330 core

in VertexData {
    vec4 color;
} inputs;

out vec4 outColor;

void main() {
    outColor = inputs.color;
}

