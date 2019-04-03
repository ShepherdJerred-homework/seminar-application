package com.shepherdjerred.seminar.paper;

import java.nio.ByteBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TextureData {

  private final int width;
  private final int height;
  private final ByteBuffer byteBuffer;
}
