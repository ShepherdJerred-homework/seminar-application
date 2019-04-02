package com.shepherdjerred.seminar.paper;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.IOException;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
  private int glShaderProgramName;
  private int glVertexShaderName;
  private int glFragmentShaderName;
  private int glUniformName;

  public void init() throws IOException {
    var shaderCodeLoader = new ShaderLoader("/shaders/");
    var vertexShaderSource = shaderCodeLoader.getShaderCode("vertex.glsl");
    var fragmentShaderSource = shaderCodeLoader.getShaderCode("fragment.glsl");

    glShaderProgramName = glCreateProgram();
    glVertexShaderName = glCreateShader(GL_VERTEX_SHADER);
    glFragmentShaderName = glCreateShader(GL_FRAGMENT_SHADER);

    glShaderSource(glVertexShaderName, vertexShaderSource);
    glShaderSource(glFragmentShaderName, fragmentShaderSource);

    glCompileShader(glVertexShaderName);
    if (glGetShaderi(glVertexShaderName, GL_COMPILE_STATUS) == 0) {
      throw new RuntimeException(
          "Error compiling Shader code: " + glGetShaderInfoLog(glVertexShaderName, 1024));
    }

    glCompileShader(glFragmentShaderName);
    if (glGetShaderi(glFragmentShaderName, GL_COMPILE_STATUS) == 0) {
      throw new RuntimeException(
          "Error compiling Shader code: " + glGetShaderInfoLog(glFragmentShaderName, 1024));
    }

    glAttachShader(glShaderProgramName, glVertexShaderName);
    glAttachShader(glShaderProgramName, glFragmentShaderName);

    glLinkProgram(glShaderProgramName);
    if (glGetProgrami(glShaderProgramName, GL_LINK_STATUS) == 0) {
      throw new RuntimeException(
          "Error linking Shader code: " + glGetProgramInfoLog(glShaderProgramName, 1024));
    }

    glValidateProgram(glShaderProgramName);
    if (glGetProgrami(glShaderProgramName, GL_VALIDATE_STATUS) == 0) {
      System.err.println(
          "Warning validating Shader code: " + glGetProgramInfoLog(glShaderProgramName, 1024));
    }

    glUseProgram(glShaderProgramName);

    if (glShaderProgramName == 0) {
      throw new RuntimeException("Could not create shader program.");
    }

    if (glVertexShaderName == 0) {
      throw new RuntimeException("Error creating vertex shader.");
    }

    if (glFragmentShaderName == 0) {
      throw new RuntimeException("Error creating fragment shader.");
    }
  }

  public void createMatrixUniform() {
    var matrix = new Matrix4f();
    glUniformName = glGetUniformLocation(glShaderProgramName, "projectionMatrix");
    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer fb = stack.mallocFloat(16);
      matrix.get(fb);
      glUniformMatrix4fv(glUniformName, false, fb);
    }
  }
}
