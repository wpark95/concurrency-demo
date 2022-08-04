package org.tlglearning.concurrency;

import static org.junit.jupiter.api.Assertions.*;

import com.tlglearning.concurrency.Computation;
import com.tlglearning.concurrency.CriticalSection;
import com.tlglearning.concurrency.ForkJoin;
import com.tlglearning.concurrency.RaceCondition;
import com.tlglearning.concurrency.Reduction;
import com.tlglearning.concurrency.SingleThread;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tlglearning.concurrency.util.Generator;

public class ComputationTest {

  private static final long RANDOM_SEED = 20_220_804_190_000L;
  private static final int ARRAY_LENGTH = 100_000_000;
  private static final int UPPER_BOUND = 1000;
  private static final double TOLERANCE = 0.000001;

  private static Generator generator;

  @BeforeAll
  static void setup() {
    generator = new Generator(RANDOM_SEED, ARRAY_LENGTH, UPPER_BOUND);
  }

  @ParameterizedTest
  @ValueSource(classes = {SingleThread.class, RaceCondition.class, CriticalSection.class,
      Reduction.class, ForkJoin.class})
  void arithmeticMean(Class<? extends Computation> implementation)
      throws ReflectiveOperationException {
    Computation computation = newInstance(implementation);
    double expected = generator.arithmeticMean();
    double actual = computation.arithmeticMean(generator.data());
    System.out.printf("expected=%g; actual=%g%n", expected, actual);
    assertEquals(expected, actual, TOLERANCE);
  }

  @ParameterizedTest
  @ValueSource(classes = {SingleThread.class, RaceCondition.class, CriticalSection.class,
      Reduction.class, ForkJoin.class})
  void geometricMean(Class<? extends Computation> implementation)
      throws ReflectiveOperationException {
    Computation computation = newInstance(implementation);
    double expected = generator.geometricMean();
    double actual = computation.geometricMean(generator.data());
    System.out.printf("expected=%g; actual=%g%n", expected, actual);
    assertEquals(expected, actual, TOLERANCE);
  }

  private static Computation newInstance(Class<? extends Computation> implementation)
      throws ReflectiveOperationException {
    Constructor<? extends Computation> constructor = implementation.getConstructor();
    return constructor.newInstance();
  }

}
