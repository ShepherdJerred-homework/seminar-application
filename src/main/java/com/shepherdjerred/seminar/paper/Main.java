package com.shepherdjerred.seminar.paper;

import java.io.IOException;

// https://www.lwjgl.org/guide
public class Main {

  private static Window window;
  private static Drawer drawer;

  private static void init() throws IOException {
    window = new Window();
    window.init();

    new ShaderProgram().init();

    drawer = new Drawer();
    drawer.init();
  }

  private static void cleanup() {
    window.cleanup();
  }

  private static void loop() {
    while (!window.shouldClose()) {
      window.clearScreen();
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
