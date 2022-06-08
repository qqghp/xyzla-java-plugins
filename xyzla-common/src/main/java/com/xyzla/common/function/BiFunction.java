package com.xyzla.common.function;

@FunctionalInterface
public interface BiFunction<T, V, K, U> {

    U get(T t, V v, K k) throws Exception;
}
