package net.moznion.donovan;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowableFunction<T, R> extends Function<T, R> {
  @Override
  default R apply(T t) {
    try {
      return throwableApply(t);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  };

  R throwableApply(T t) throws Throwable;
}
