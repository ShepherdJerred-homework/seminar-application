package com.shepherdjerred.seminar.paper;

// https://www.lwjgl.org/guide
public class Main {

  private static Window window;
  private static ShaderProgram shaderProgram;
  private static Drawer drawer;
  private static Timer timer = new Timer();

  private static void init() throws Exception {
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
    float elapsedTime;
    float accumulator = 0f;
    float interval = 1f / 20;

    boolean running = true;
    while (running && !window.shouldClose()) {
      elapsedTime = timer.getElapsedTime();
      accumulator += elapsedTime;

      while (accumulator >= interval) {
        drawer.update();
        accumulator -= interval;
      }

      window.clearScreen();

      var projectionMatrix = drawer.getProjectionMatrix(window.getAspectRatio());
      var modelMatrix = drawer.getModelMatrix();
      shaderProgram.setProjectionMatrix(projectionMatrix);
      shaderProgram.setModelMatrix(modelMatrix);

      drawer.draw();
      window.update();

      sync();
    }
  }

  private static void sync() {
    float loopSlot = 1f / 60;
    double endTime = timer.getLastLoopTime() + loopSlot;
    while (timer.getTime() < endTime) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
      }
    }
  }

  public static void main(String[] args) throws Exception {
    init();
    loop();
    cleanup();
  }
}
