package com.shepherdjerred.seminar.paper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShaderLoader {

  private final String basePath;

  public String getShaderCode(String shaderFileName) throws IOException {
    var stream = this.getClass().getResourceAsStream(basePath + shaderFileName);
    if (stream == null) {
      throw new FileNotFoundException(shaderFileName);
    }
    var bytes = stream.readAllBytes();
    stream.close();
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
