package com.foursoft.xml.io.write;

import java.util.HashMap;
import java.util.Map;

public class Comments {
    private final Map<Object, String> map;

    public Comments() {
        map = new HashMap<>();
    }

    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    public String get(final Object key) {
        return map.get(key);
    }
}
