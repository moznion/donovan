package net.moznion.donovan;

import java.util.function.Function;

/**
 * Functional interface which receives a argument and returns a result and it can throw exception.
 * 
 * @author moznion
 *
 * @param <T> Type of argument
 * @param <R> Type of result
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> extends Function<T, R> {
  @Override
  default R apply(T arg) {
    try {
      return throwableApply(arg);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Apply function with throwable processing.
   * 
   * @param arg Argument of function
   * @return Result of function
   * @throws Throwable All of errors and exceptions which is caused by function
   */
  R throwableApply(T arg) throws Throwable;
}
