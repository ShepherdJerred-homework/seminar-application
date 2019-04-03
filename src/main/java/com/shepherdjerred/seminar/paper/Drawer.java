package com.shepherdjerred.seminar.paper;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

// Coordinate data from: https://github.com/lwjglgamedev/lwjglbook/blob/master/chapter07/c07-p2/src/main/java/org/lwjglb/game/DummyGame.java
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
  private int glTextureCoordVbo;
  private int glVaoName;
  private int glEboName;
  private int glTextureName;
  private ShaderProgram shaderProgram;

  public Drawer(ShaderProgram shaderProgram) {
    this.shaderProgram = shaderProgram;
  }

  public void init() throws Exception {
    createVao();
    createVertexVbo();
    createTextureCoordVbo();
    createEbo();
    createTexture();
  }

  public void draw() {
    glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
  }

  public void update() {
    modelRotationX += .1;
    modelRotationY += .1;
    modelRotationZ += .1;
  }

  private void createTexture() throws Exception {
    glTextureName = glGenTextures();

    var fileName = "/Users/jerred/Programming/IdeaProjects/seminar-paper-application/src/main/resources/textures/sheet.png";

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, glTextureName);
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    var dirtTextureData = loadTextureFromDisk(
        fileName);

    glTexImage2D(GL_TEXTURE_2D,
        0,
        GL_RGBA,
        dirtTextureData.getWidth(),
        dirtTextureData.getHeight(),
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        dirtTextureData.getByteBuffer());

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, glTextureName);
  }

  private TextureData loadTextureFromDisk(String fileName) throws Exception {
    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer w = stack.mallocInt(1);
      IntBuffer h = stack.mallocInt(1);
      IntBuffer channels = stack.mallocInt(1);

      ByteBuffer buf = stbi_load(fileName, w, h, channels, 4);

      if (buf == null) {
        throw new Exception("Image file [" + fileName + "] not loaded: " + stbi_failure_reason());
      }

      return new TextureData(w.get(), h.get(), buf);
    }
  }

  private void createVao() {
    glVaoName = glGenVertexArrays();
    glBindVertexArray(glVaoName);
  }

  private void createVertexVbo() {
    glVertexVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glVertexVboName);

    float[] vertices = new float[] {
        // V0
        -0.5f, 0.5f, 0.5f,
        // V1
        -0.5f, -0.5f, 0.5f,
        // V2
        0.5f, -0.5f, 0.5f,
        // V3
        0.5f, 0.5f, 0.5f,
        // V4
        -0.5f, 0.5f, -0.5f,
        // V5
        0.5f, 0.5f, -0.5f,
        // V6
        -0.5f, -0.5f, -0.5f,
        // V7
        0.5f, -0.5f, -0.5f,

        // For text coords in top face
        // V8: V4 repeated
        -0.5f, 0.5f, -0.5f,
        // V9: V5 repeated
        0.5f, 0.5f, -0.5f,
        // V10: V0 repeated
        -0.5f, 0.5f, 0.5f,
        // V11: V3 repeated
        0.5f, 0.5f, 0.5f,

        // For text coords in right face
        // V12: V3 repeated
        0.5f, 0.5f, 0.5f,
        // V13: V2 repeated
        0.5f, -0.5f, 0.5f,

        // For text coords in left face
        // V14: V0 repeated
        -0.5f, 0.5f, 0.5f,
        // V15: V1 repeated
        -0.5f, -0.5f, 0.5f,

        // For text coords in bottom face
        // V16: V6 repeated
        -0.5f, -0.5f, -0.5f,
        // V17: V7 repeated
        0.5f, -0.5f, -0.5f,
        // V18: V1 repeated
        -0.5f, -0.5f, 0.5f,
        // V19: V2 repeated
        0.5f, -0.5f, 0.5f,
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

  private void createTextureCoordVbo() {
    glTextureCoordVbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordVbo);

    float[] textureCoords = new float[] {
        0.0f, 0.0f,
        0.0f, 0.5f,
        0.5f, 0.5f,
        0.5f, 0.0f,

        0.0f, 0.0f,
        0.5f, 0.0f,
        0.0f, 0.5f,
        0.5f, 0.5f,

        // For text coords in top face
        0.0f, 0.5f,
        0.5f, 0.5f,
        0.0f, 1.0f,
        0.5f, 1.0f,

        // For text coords in right face
        0.0f, 0.0f,
        0.0f, 0.5f,

        // For text coords in left face
        0.5f, 0.0f,
        0.5f, 0.5f,

        // For text coords in bottom face
        0.5f, 0.0f,
        1.0f, 0.0f,
        0.5f, 0.5f,
        1.0f, 0.5f,
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(textureCoords.length);
      vertexBuffer.put(textureCoords);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }

    glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(1);
  }

  private void createEbo() {
    glEboName = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glEboName);

    int[] indices = new int[] {
        // Front face
        0, 1, 3, 3, 1, 2,
        // Top Face
        8, 10, 11, 9, 8, 11,
        // Right face
        12, 13, 7, 5, 12, 7,
        // Left face
        14, 15, 6, 4, 14, 6,
        // Bottom face
        16, 18, 19, 17, 16, 19,
        // Back face
        4, 6, 7, 5, 4, 7
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
        .translate(modelPosition.getX(), modelPosition.getY(), modelPosition.getZ() - 2)
        .rotateX(modelRotationX)
        .rotateY(modelRotationY)
        .rotateZ(modelRotationZ)
        .scale(modelScale);
  }

  public Matrix4f getProjectionMatrix(float aspectRatio) {
    return new Matrix4f()
        .perspective(FIELD_OF_VIEW, aspectRatio, Z_NEAR, Z_FAR);
  }
}
