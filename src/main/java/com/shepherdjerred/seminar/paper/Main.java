package com.shepherdjerred.seminar.paper;

import java.io.IOException;

// https://www.lwjgl.org/guide
public class Main {

  private static Window window;
  private static ShaderProgram shaderProgram;
  private static Drawer drawer;

  private static void init() throws IOException {
    window = new Window();
    window.init();

    shaderProgram = new ShaderProgram();
    shaderProgram.init();

    drawer = new Drawer();
    drawer.init();
  }

  private static void cleanup() {
    window.cleanup();
  }

  private static void loop() {
    while (!window.shouldClose()) {
      window.clearScreen();

      var projectionMatrix = drawer.getProjectionMatrix();
      var modelMatrix = drawer.getModelMatrix();
      shaderProgram.setProjectionMatrix(projectionMatrix);
      shaderProgram.setModelMatrix(modelMatrix);

      drawer.draw();
      window.update();
    }
  }

  public static void main(String[] args) throws IOException {
    init();
    loop();
    cleanup();
  }
}
