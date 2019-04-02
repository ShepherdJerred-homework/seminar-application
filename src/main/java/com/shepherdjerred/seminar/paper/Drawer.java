package com.shepherdjerred.seminar.paper;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class Drawer {

  // https://ahbejarano.gitbook.io/lwjglgamedev/chapter6
  private final static float FIELD_OF_VIEW = (float) Math.toRadians(60f);
  private final static float Z_NEAR = 0.01f;
  private final static float Z_FAR = 1000f;
  private float modelScale = 1;
  private float modelRotationX = 0;
  private float modelRotationY = 0;
  private float modelRotationZ = 0;
  private Coordinate modelPosition = new Coordinate(0, 0, 0);
  private int glVertexVboName;
  private int glColorVboName;
  private int glVaoName;
  private int glEboName;

  public void init() {
    createVao();
    createVertexVbo();
    createColorVbo();
    createEbo();
  }

  public void draw() {
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
  }

  public void update() {
//    modelRotationX += .5;
//    modelRotationY += .5;
//    modelRotationZ += .5;
  }

  private void createVao() {
    glVaoName = glGenVertexArrays();
    glBindVertexArray(glVaoName);
  }

  private void createVertexVbo() {
    glVertexVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glVertexVboName);

    float[] vertices = new float[] {
        -0.5f, 0.5f, -10f,
        -0.5f, -0.5f, -10f,
        0.5f, -0.5f, -10f,
        0.5f, 0.5f, -10f
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(vertices.length);
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(0);
  }

  private void createColorVbo() {
    glColorVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glColorVboName);

    float[] color = new float[] {
        1, 0, 0, 1,
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

    glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(1);
  }

  private void createEbo() {
    glEboName = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glEboName);

    int[] indices = new int[] {
        0, 1, 2,
        1, 2, 3
    };

    try (var stack = MemoryStack.stackPush()) {
      var eboBuffer = stack.mallocInt(indices.length);
      eboBuffer.put(indices);
      eboBuffer.flip();
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboBuffer, GL_STATIC_DRAW);
    }

  }

  public Matrix4f getModelMatrix() {
    return new Matrix4f()
        .translate(modelPosition.getX(), modelPosition.getY(), modelPosition.getZ())
        .rotateX((float) Math.toRadians(modelRotationX))
        .rotateY((float) Math.toRadians(modelRotationY))
        .rotateZ((float) Math.toRadians(modelRotationZ))
        .scale(modelScale);
  }

  public Matrix4f getProjectionMatrix(float aspectRatio) {
    return new Matrix4f()
        .perspective(FIELD_OF_VIEW, aspectRatio, Z_NEAR, Z_FAR);
  }
}
