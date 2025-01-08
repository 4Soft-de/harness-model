package com.foursoft.harness.vec.scripting;

@FunctionalInterface
public interface Locator<T> {

    T locate(String identification);

}
