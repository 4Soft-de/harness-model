/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl2vec.core.ConversionException;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.core.TransformerRegistry;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectionsBasedTransformerRegistry implements TransformerRegistry {

    private final Map<ClassTupleKey<?, ?>, List<Class<? extends Transformer>>>
            transformerClasses;
    private final Map<ClassTupleKey<?, ?>, Collection<Transformer>> transformerInstances = new HashMap<>();

    public ReflectionsBasedTransformerRegistry() {
        transformerClasses = createTransformerClassMap();
    }

    @Override
    public <S, D> Collection<Transformer<S, D>> getTransformer(final Class<S> source,
                                                               final Class<D> destination) {
        final ClassTupleKey key = new ClassTupleKey(source, destination);

        return transformerInstances.computeIfAbsent(key,
                                                    k -> createTransformerInstances(
                                                            k.source,

                                                            k.destination))
                .stream()
                .map(x -> (Transformer<S, D>) x)
                .toList();
    }

    private <S, D> Collection<Transformer> createTransformerInstances(
            final Class<S> source,
            final Class<D> destination) {
        final List<Class<? extends Transformer>> transformers = transformerClasses.get(
                new ClassTupleKey<>(source, destination));
        if (transformers == null || transformers.isEmpty()) {
            throw new ConversionException("No transformer found for " + source + " and " + destination);
        }

        return transformers.stream()
                .map(this::createTransformerInstance)
                .toList();
    }

    private Transformer createTransformerInstance(final Class<? extends Transformer> transformerClass) {
        try {
            final Constructor<? extends Transformer> constructor = transformerClass.getConstructor();
            return constructor.newInstance();
        } catch (final NoSuchMethodException e) {
            throw new ConversionException("The transformer " + transformerClass.getName() +
                                                  "has no default or single arg TransformationContext constructor");
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionException("Error while creating transformer instance.", e);
        }
    }

    @SuppressWarnings({"rawtypes"})
    private static ClassTupleKey<?, ?> extractTransformerClassKey(
            final Class<? extends Transformer> transformerClass) {
        final Type[] genericInterfaces = transformerClass.getGenericInterfaces();
        for (final Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof final ParameterizedType parameterizedType &&
                    parameterizedType.getRawType() == Transformer.class) {
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 2) {
                    final Type source = actualTypeArguments[0];
                    final Type destination = actualTypeArguments[1];
                    if (source instanceof final Class<?> sourceClass &&
                            destination instanceof final Class<?> destClass) {
                        return new ClassTupleKey<>(sourceClass, destClass);
                    }
                }
            }
        }
        throw new ConversionException("It seems that " + transformerClass.getName() +
                                              " does not implement Transformer<S,D> with actual type arguments.");
    }

    private static Map<ClassTupleKey<?, ?>, List<Class<? extends Transformer>>> createTransformerClassMap() {
        final Reflections reflections = new Reflections("com.foursoft.harness.kbl2vec");

        final Set<Class<? extends Transformer>> findTransformers = reflections.getSubTypesOf(Transformer.class);

        return findTransformers.stream().collect(
                Collectors.groupingBy(ReflectionsBasedTransformerRegistry::extractTransformerClassKey));
    }

    private record ClassTupleKey<S, D>(Class<S> source, Class<D> destination) {
    }

}
