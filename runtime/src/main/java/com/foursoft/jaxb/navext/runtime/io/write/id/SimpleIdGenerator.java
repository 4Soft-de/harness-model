package com.foursoft.jaxb.navext.runtime.io.write.id;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The id generator generates the xml id based on the respective class names.
 */
public class SimpleIdGenerator implements IdGenerator {
    private final Map<String, AtomicInteger> count = new HashMap<>();
    private final int prefixCount;
    private final String delimiter;


    private SimpleIdGenerator(final Builder builder) {
        prefixCount = builder.prefixCount;
        delimiter = builder.delimiter;
    }

    @Override
    public synchronized String getNextId(final Object object) {
        final AtomicInteger counter = count.computeIfAbsent(object.getClass().getSimpleName(), c -> new AtomicInteger(1));
        String name = object.getClass().getSimpleName().substring(prefixCount);
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name + delimiter + counter.getAndIncrement();
    }


    public static final class Builder {
        private int prefixCount = 0;
        private String delimiter = "_";

        /**
         * Some class names have a prefix like "Kbl" or "Vec". These can be removed if necessary.
         *
         * @param prefixCount Length of the prefix.
         * @return this builder.
         */
        public Builder withRemovePrefix(final int prefixCount) {
            this.prefixCount = prefixCount;
            return this;
        }

        /**
         * Delimiter between class name and counter.
         *
         * @param delimiter delimiter.
         * @return this builder.
         */
        public Builder withDelimiter(final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public SimpleIdGenerator build() {
            return new SimpleIdGenerator(this);
        }
    }
}
