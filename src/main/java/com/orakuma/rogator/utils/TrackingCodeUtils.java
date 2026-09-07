package com.orakuma.rogator.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.security.SecureRandom;

public final class TrackingCodeUtils {
  private static final String CHARACTERS = "23456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
  private static final SecureRandom NUMBER_GENERATOR = new SecureRandom();

  private TrackingCodeUtils() {}

  public static String generate(int length) {
    return NanoIdUtils.randomNanoId(NUMBER_GENERATOR, CHARACTERS.toCharArray(), length);
  }
}
