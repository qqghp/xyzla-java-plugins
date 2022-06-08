package com.xyzla.common.function;

@FunctionalInterface
public interface Consumer<T> {

    void accept(T t) throws Exception;
}
