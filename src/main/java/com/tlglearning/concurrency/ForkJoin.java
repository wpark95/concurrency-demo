package com.tlglearning.concurrency;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoin implements Computation {

  private static final int FORK_THRESHOLD = 10_000_000;

  private final Object lock = new Object();
  private double logSum;

  @Override
  public double arithmeticMean(int[] data) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public double geometricMean(int[] data) {
    Worker worker = new Worker(data, 0, data.length);
    ForkJoinPool pool = new ForkJoinPool();
    pool.invoke(worker);
    return Math.exp(logSum / data.length);
  }

  private class Worker extends RecursiveAction {

    private final int[] data;
    private final int startIndex;
    private final int endIndex;

    public Worker(int[] data, int startIndex, int endIndex) {
      this.data = data;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    @Override
    protected void compute() {
      if (endIndex - startIndex <= FORK_THRESHOLD) {
        double logSubTotal = 0;
        for (int i = startIndex; i < endIndex; i++) {
          logSubTotal += Math.log(data[i]);
        }
        synchronized (lock) {
          logSum += logSubTotal;
        }
      } else {
        int midpoint = (startIndex + endIndex) / 2;
        invokeAll(new Worker(data, startIndex, midpoint), new Worker(data, midpoint, endIndex));
      }
    }

  }

}
