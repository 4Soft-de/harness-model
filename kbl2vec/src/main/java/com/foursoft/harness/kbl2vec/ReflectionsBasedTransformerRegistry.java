package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl2vec.core.ConversionException;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.core.TransformerRegistry;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectionsBasedTransformerRegistry implements TransformerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionsBasedTransformerRegistry.class);
    private final Map<ClassTupleKey<?, ?>, List<Class<? extends Transformer>>>
            transformerClasses;
    private final Map<TransformerInstanceKey<?, ?>, Transformer<?, ?>> transformerInstances = new HashMap<>();

    public ReflectionsBasedTransformerRegistry() {
        transformerClasses = createTransformerClassMap();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <S, D> Transformer<S, D> getTransformer(final TransformationContext context, final Class<S> source,
                                                   final Class<D> destination) {
        final TransformerInstanceKey key = new TransformerInstanceKey(context, source, destination);

        return (Transformer<S, D>) transformerInstances.computeIfAbsent(key,
                                                                        k -> createTransformerInstance(k.context,
                                                                                                       k.source,
                                                                                                       k.destination));

    }

    @SuppressWarnings({"unchecked"})
    private <S, D> Transformer<S, D> createTransformerInstance(final TransformationContext context,
                                                               final Class<S> source,
                                                               final Class<D> destination) {
        final List<Class<? extends Transformer>> transformers = transformerClasses.get(
                new ClassTupleKey<>(source, destination));
        if (transformers == null || transformers.isEmpty()) {
            throw new ConversionException("No transformer found for " + source + " and " + destination);
        }
        if (transformers.size() > 1) {
            throw new ConversionException("Multiple transformers found for " + source + " and " + destination);
        }
        final Class<? extends Transformer<S, D>> transformerClass =
                (Class<? extends Transformer<S, D>>) transformers.get(0);
        try {
            return tryInstanceCreation(context, transformerClass);
        } catch (final NoSuchMethodException e) {
            throw new ConversionException("The transformer " + transformerClass.getName() +
                                                  "has no default or single arg TransformationContext constructor");
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ConversionException("Error while creating transformer instance.", e);
        }
    }

    private static <S, D> Transformer<S, D> tryInstanceCreation(final TransformationContext context,
                                                                final Class<? extends Transformer<S, D>> transformerClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        try {
            final Constructor<? extends Transformer<S, D>> constructor = transformerClass.getConstructor(
                    TransformationContext.class);
            return constructor.newInstance(context);
        } catch (final NoSuchMethodException e) {
            LOGGER.debug(
                    "Transformer {} has no constructor with TransformationContext as args, using default " +
                            "constructor.",
                    transformerClass);
            final Constructor<? extends Transformer<S, D>> constructor = transformerClass.getConstructor();
            return constructor.newInstance();
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map<ClassTupleKey<?, ?>, List<Class<? extends Transformer>>> createTransformerClassMap() {
        final Reflections reflections = new Reflections("com.foursoft.harness.kbl2vec");

        final Set<Class<? extends Transformer>> findTransformers = reflections.getSubTypesOf(Transformer.class);

        return findTransformers.stream().collect(
                Collectors.groupingBy(ReflectionsBasedTransformerRegistry::extractTransformerClassKey));
    }

    private record ClassTupleKey<S, D>(Class<S> source, Class<D> destination) {
    }

    private record TransformerInstanceKey<S, D>(TransformationContext context, Class<S> source, Class<D> destination) {
    }

}
