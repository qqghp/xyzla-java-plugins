package com.xyzla.common.function;

@FunctionalInterface
public interface Supplier<T> {

    T get() throws Exception;
}
