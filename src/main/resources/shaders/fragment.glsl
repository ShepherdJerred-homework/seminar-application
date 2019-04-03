#version 330 core

in VertexData {
    vec2 textureCoord;
} inputs;

out vec4 outColor;

uniform sampler2D textureSampler;

void main() {
    outColor = texture(textureSampler, inputs.textureCoord);
}

