package org.tlglearning.concurrency.util;

import java.util.Random;

public final class Generator {

  private final int[] data;
  private final double arithmeticMean;
  private final double geometricMean;

  public Generator(long seed, int arrayLength, int upperBound) {
    long[] sum = new long[1];
    double[] sumLog = new double[1];
    Random rng = new Random(seed);
    data = rng
        .ints(arrayLength, 1, upperBound)
        .peek((value) -> {
          sum[0] += value;
          sumLog[0] += Math.log(value);
        })
        .toArray();
    arithmeticMean = sum[0] / (double) arrayLength;
    geometricMean = Math.exp(sumLog[0] / arrayLength);
  }

  public int[] data() {
    return data;
  }

  public double arithmeticMean() {
    return arithmeticMean;
  }

  public double geometricMean() {
    return geometricMean;
  }

}
