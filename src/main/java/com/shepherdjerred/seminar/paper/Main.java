package com.shepherdjerred.seminar.paper;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
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
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

// https://www.lwjgl.org/guide
public class Main {

  // The window handle
  private long window;
  private int glShaderProgramName;
  private int glVertexShaderName;
  private int glFragmentShaderName;
  private int glVertexVboName;
  private int glColorVboName;
  private int glVaoName;
  private int glUniformName;

  public void run() throws IOException {
    System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() throws IOException {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    glfwSetErrorCallback((error, description) -> System.err.println(
        "GLFW Error: " + error + ", " + description));

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

    // Create the window
    window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
      }
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          window,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    createShaderProgram();
    createVao();
    createVertexVbo();
    createColorVbo();
    bindVboToVao();
//    createMatrixUniform();
  }

  private void createVertexVbo() {
    glVertexVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glVertexVboName);

    float[] vertices = new float[] {
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(vertices.length);
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }
  }

  private void createMatrixUniform() {
    var matrix = new Matrix4f();
    glUniformName = glGetUniformLocation(glShaderProgramName, "projectionMatrix");
    try (MemoryStack stack = MemoryStack.stackPush()) {
      FloatBuffer fb = stack.mallocFloat(16);
      matrix.get(fb);
      glUniformMatrix4fv(glUniformName, false, fb);
    }
  }

  private void createColorVbo() {
    glColorVboName = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, glColorVboName);

    float[] color = new float[]{
        1, 1, 1, 1
    };

    try (var stack = MemoryStack.stackPush()) {
      var vertexBuffer = stack.mallocFloat(color.length);
      vertexBuffer.put(color);
      vertexBuffer.flip();
      glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }
  }

  private void createVao() {
    glVaoName = glGenVertexArrays();
    glBindVertexArray(glVaoName);
  }

  private void bindVboToVao() {
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(0);

    glBindBuffer(GL_ARRAY_BUFFER, glColorVboName);
    glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
    glEnableVertexAttribArray(1);
  }

  private void createShaderProgram() throws IOException {
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

  private void draw() {
    glDrawArrays(GL_TRIANGLES, 0, 3);
  }

  private void loop() {
    // Set the clear color
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

      draw();

      glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }
}
