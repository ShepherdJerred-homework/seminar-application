package com.shepherdjerred.seminar.paper;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.lwjgl.system.MemoryStack;

public class Drawer {

  private int glVertexVboName;
  private int glColorVboName;
  private int glVaoName;

  public void init() {
    createVao();
    createVertexVbo();
    createColorVbo();
    bindVboToVao();
  }

  public void draw() {
    glDrawArrays(GL_TRIANGLES, 0, 3);
  }

  private void createVao() {
    glVaoName = glGenVertexArrays();
    glBindVertexArray(glVaoName);
  }

  private void createVertexVbo() {
    glVertexVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glVertexVboName);

    float[] vertices = new float[] {
        1, 1, 0,
        0, 1, 0,
        0, 0, 0
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(vertices.length);
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }
  }

  private void createColorVbo() {
    glColorVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glColorVboName);

    float[] color = new float[] {
        1, 0, 0, 1,
        1, 0, 0, 1,
        1, 0, 0, 1
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(color.length);
      vertexBuffer.put(color);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }
  }

  private void bindVboToVao() {
    glBindBuffer(GL_ARRAY_BUFFER, glVertexVboName);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(0);

    glBindBuffer(GL_ARRAY_BUFFER, glColorVboName);
    glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(1);
  }
}
