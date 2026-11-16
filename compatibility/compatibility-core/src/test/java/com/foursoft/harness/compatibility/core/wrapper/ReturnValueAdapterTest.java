/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReturnValueAdapterTest {

    private final ReturnValueAdapter adapter = new ReturnValueAdapter();

    static Stream<Arguments> defaultValues() {
        return Stream.of(Arguments.of(void.class, null),
                         Arguments.of(String.class, null),
                         Arguments.of(Integer.class, null),
                         Arguments.of(boolean.class, false),
                         Arguments.of(char.class, '\0'),
                         Arguments.of(int.class, 0),
                         Arguments.of(long.class, 0L),
                         Arguments.of(double.class, 0.0),
                         Arguments.of(float.class, 0.0f),
                         Arguments.of(short.class, (short) 0),
                         Arguments.of(byte.class, (byte) 0));
    }

    @ParameterizedTest
    @MethodSource("defaultValues")
    void defaultValueIsNullExceptForPrimitives(final Class<?> type, final Object expected) {
        assertThat(adapter.defaultValue(type)).isEqualTo(expected);
    }

    @Test
    void collectionOfNothingIsAnEmptyList() throws Exception {
        final Collection<Object> result = adapter.toCollection(null, method("strings"));

        assertThat(result)
                .isInstanceOf(ArrayList.class)
                .isEmpty();
    }

    @Test
    void singleValueBecomesACollectionOfTheDeclaredKind() throws Exception {
        assertThat(adapter.toCollection("text", method("strings")))
                .isInstanceOf(ArrayList.class)
                .containsExactly("text");
        assertThat(adapter.toCollection(7, method("integerSet")))
                .isInstanceOf(HashSet.class)
                .containsExactly(7);
    }

    @Test
    void elementsAreConvertedToTheElementType() throws Exception {
        final Method integers = method("integers");

        // Asked twice, so that the element type is taken from the cache the second time.
        assertThat(adapter.toCollection(List.of("1", 2L, new BigDecimal("3")), integers))
                .containsExactly(1, 2, 3);
        assertThat(adapter.toCollection(Arrays.asList("4", null), integers))
                .containsExactly(4);
    }

    @Test
    void elementsThatDoNotFitAreLeftOut() throws Exception {
        // A model object of another type cannot be converted, and neither can a text that is no number.
        final Object modelObject = new Object();

        assertThat(adapter.toCollection(List.of(modelObject, "no number", "5"), method("integers")))
                .containsExactly(5);
    }

    @Test
    void nodeBecomesItsTextAndAnEnumIsLeftOut() throws Exception {
        assertThat(adapter.toCollection(List.of(Thread.State.NEW, node("from a node")), method("strings")))
                .containsExactly("from a node");
    }

    @Test
    void elementTypeIsTheBoundOfAWildcardOrTheRawTypeOfAParameterizedType() throws Exception {
        assertThat(adapter.toCollection(List.of(1, "x"), method("numbers")))
                .containsExactly(1);
        final List<String> inner = List.of("inner");
        assertThat(adapter.toCollection(List.of(inner, "x"), method("lists")))
                .containsExactly(inner);
    }

    @Test
    void rawCollectionTakesAnyElement() throws Exception {
        final Object element = new Object();

        assertThat(adapter.toCollection(List.of(element, "x"), method("raw")))
                .containsExactly(element, "x");
    }

    @Test
    void singleValueOfNothingIsTheDefaultValue() throws Exception {
        assertThat(adapter.toSingleValue(null, method("intValue"))).isEqualTo(0);
        assertThat(adapter.toSingleValue(null, method("text"))).isNull();
    }

    @Test
    void singleValueIsTheFirstElementOfACollection() throws Exception {
        assertThat(adapter.toSingleValue(Arrays.asList(null, "first", "second"), method("text"))).isEqualTo("first");
        assertThat(adapter.toSingleValue(List.of(), method("booleanValue"))).isEqualTo(false);
        assertThat(adapter.toSingleValue(Arrays.asList(null, null), method("text"))).isNull();
    }

    @Test
    void singleValueOfTheReturnTypeIsHandedOverUnchanged() throws Exception {
        final AtomicInteger atomic = new AtomicInteger(3);

        assertThat(adapter.toSingleValue(atomic, method("atomic"))).isSameAs(atomic);
    }

    @Test
    void scalarsBecomeText() throws Exception {
        final Method text = method("text");

        assertThat(adapter.toSingleValue(new BigDecimal("1E+3"), text)).isEqualTo("1000");
        assertThat(adapter.toSingleValue(42, text)).isEqualTo("42");
        assertThat(adapter.toSingleValue(true, text)).isEqualTo("true");
        assertThat(adapter.toSingleValue('c', text)).isEqualTo("c");
        assertThat(adapter.toSingleValue(node(" text of a node "), text)).isEqualTo(" text of a node ");
    }

    static Stream<Arguments> textsOfBooleans() {
        return Stream.of(Arguments.of("true", true),
                         Arguments.of(" 1 ", true),
                         Arguments.of("false", false),
                         Arguments.of("0", false));
    }

    @ParameterizedTest
    @MethodSource("textsOfBooleans")
    void textBecomesABoolean(final String text, final boolean expected) throws Exception {
        assertThat(adapter.toSingleValue(text, method("booleanValue"))).isEqualTo(expected);
        assertThat(adapter.toSingleValue(node(text), method("booleanValue"))).isEqualTo(expected);
    }

    static Stream<Arguments> numbers() {
        return Stream.of(Arguments.of("longValue", 12L),
                         Arguments.of("intValue", 12),
                         Arguments.of("boxedInteger", 12),
                         Arguments.of("shortValue", (short) 12),
                         Arguments.of("byteValue", (byte) 12),
                         Arguments.of("doubleValue", 12.0),
                         Arguments.of("floatValue", 12.0f),
                         Arguments.of("bigInteger", BigInteger.valueOf(12)),
                         Arguments.of("bigDecimal", new BigDecimal("12")),
                         Arguments.of("number", new BigDecimal("12")));
    }

    @ParameterizedTest
    @MethodSource("numbers")
    void numberAndTextBecomeTheNumberType(final String methodName, final Object expected) throws Exception {
        final Method method = method(methodName);

        assertThat(adapter.toSingleValue("12", method)).isEqualTo(expected);
        assertThat(adapter.toSingleValue(new BigDecimal("12"), method)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"yes", "", "maybe"})
    void textThatIsNoBooleanFails(final String text) throws Exception {
        final Method booleanValue = method("booleanValue");

        assertThatThrownBy(() -> adapter.toSingleValue(text, booleanValue))
                .isInstanceOf(WrapperException.class)
                .hasMessageContaining("booleanValue");
    }

    @Test
    void valuesThatCannotBeConvertedFail() throws Exception {
        final Method intValue = method("intValue");
        final Method charValue = method("charValue");
        final Method atomic = method("atomic");
        final Method text = method("text");

        // Not a number, not a whole number, no number at all.
        assertThatThrownBy(() -> adapter.toSingleValue("abc", intValue)).isInstanceOf(WrapperException.class);
        assertThatThrownBy(() -> adapter.toSingleValue("1.5", intValue)).isInstanceOf(WrapperException.class);
        assertThatThrownBy(() -> adapter.toSingleValue(true, intValue)).isInstanceOf(WrapperException.class);
        // Text is not parsed into a character, nor into a number type the adapter does not know.
        assertThatThrownBy(() -> adapter.toSingleValue("x", charValue)).isInstanceOf(WrapperException.class);
        assertThatThrownBy(() -> adapter.toSingleValue("3", atomic)).isInstanceOf(WrapperException.class);
        assertThatThrownBy(() -> adapter.toSingleValue(3, atomic)).isInstanceOf(WrapperException.class);
        // A model object has no text.
        final Object modelObject = new Object();
        assertThatThrownBy(() -> adapter.toSingleValue(modelObject, text)).isInstanceOf(WrapperException.class);
    }

    @Test
    void argumentsAreAdaptedToTheParameterTypes() throws Exception {
        final Method setter = Signatures.class.getMethod("set", int.class, String.class, Long.class, Object.class);
        final Object kept = new Object();

        assertThat(adapter.toArguments(new Object[]{null, 5, "7", kept}, setter))
                .containsExactly(0, "5", 7L, kept);
        assertThat(adapter.toArguments(new Object[]{"8", null, null, null}, setter))
                .containsExactly(8, null, null, null);
    }

    @Test
    void argumentsThatCannotBeConvertedAreLeftAsTheyAre() throws Exception {
        final Method setter = Signatures.class.getMethod("set", int.class, String.class, Long.class, Object.class);
        final Object modelObject = new Object();

        assertThat(adapter.toArguments(new Object[]{"abc", modelObject, true, null}, setter))
                .containsExactly("abc", modelObject, true, null);
    }

    @Test
    void argumentsThatDoNotMatchTheParametersAreLeftAlone() throws Exception {
        final Method setter = Signatures.class.getMethod("set", int.class, String.class, Long.class, Object.class);
        final Object[] tooFew = {"1"};

        assertThat(adapter.toArguments(null, setter)).isNull();
        assertThat(adapter.toArguments(tooFew, setter)).isSameAs(tooFew);
    }

    @Test
    void primitiveParametersAreBoxedForTheTypeCheck() throws Exception {
        final Method setter = Signatures.class.getMethod("setPrimitives", boolean.class, long.class, double.class,
                                                         float.class, short.class, byte.class, char.class);

        assertThat(adapter.toArguments(new Object[]{"true", 1, 2, 3, 4, 5, 'c'}, setter))
                .containsExactly(true, 1L, 2.0, 3.0f, (short) 4, (byte) 5, 'c');
    }

    private Method method(final String name) throws NoSuchMethodException {
        return Signatures.class.getMethod(name);
    }

    private Element node(final String text) throws Exception {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        final Element element = document.createElement("value");
        element.setTextContent(text);
        return element;
    }

    /**
     * Return and parameter types the adapter converts to.
     */
    @SuppressWarnings({"unused", "rawtypes"})
    interface Signatures {

        List<String> strings();

        List<Integer> integers();

        Set<Integer> integerSet();

        List<? extends Number> numbers();

        List<List<String>> lists();

        Collection raw();

        String text();

        boolean booleanValue();

        char charValue();

        int intValue();

        Integer boxedInteger();

        long longValue();

        short shortValue();

        byte byteValue();

        double doubleValue();

        float floatValue();

        BigInteger bigInteger();

        BigDecimal bigDecimal();

        Number number();

        AtomicInteger atomic();

        void set(int primitive, String text, Long boxed, Object any);

        void setPrimitives(boolean b, long l, double d, float f, short s, byte by, char c);
    }

}
