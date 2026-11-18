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

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.PropertyAdditionProvider;
import com.foursoft.harness.compatibility.core.PurePropertyAdditions;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.newmodel.Element;
import com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.newmodel.Item;
import com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.newmodel.Part;
import com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.OnlyOldElement;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigInteger;
import java.util.List;

import static com.foursoft.harness.compatibility.core.PropertyAddition.renamed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Reads an older version of a model through the API of a newer one, whose properties differ in type or name
 * but not in meaning, the way consecutive versions of a standard typically differ.
 */
class ReflectionBasedWrapperModelEvolutionTest {

    private static final String OLD_MODEL =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel";
    private static final String NEW_MODEL =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.newmodel";

    private final CompatibilityContext context = new CompatibilityContext.CompatibilityContextBuilder()
            .withClassMapper(new EvolutionClassMapper())
            .build();

    private final com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Item source =
            new com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Item();

    @Test
    void singleReferenceBecomesTheOnlyElementOfAList() {
        source.setReference(oldPart("reference"));

        final List<Part> reference = upgraded().getReference();

        assertThat(reference).extracting(Part::getName).containsExactly("reference");
    }

    @Test
    void missingSingleReferenceBecomesAnEmptyListWhichIsKept() {
        final Item item = upgraded();

        final List<Part> reference = item.getReference();
        reference.add(null);

        assertThat(item.getReference()).isSameAs(reference);
    }

    @Test
    void listBecomesItsFirstElement() {
        source.getParts().add(oldPart("first"));
        source.getParts().add(oldPart("second"));

        assertThat(upgraded().getParts().getName()).isEqualTo("first");
    }

    @Test
    void textBecomesANumber() {
        source.setCount(" 42 ");

        assertThat(upgraded().getCount()).isEqualTo(BigInteger.valueOf(42));
    }

    @Test
    void textWhichIsNoNumberCannotBeRead() {
        source.setCount("many");

        final Item item = upgraded();

        assertThatThrownBy(item::getCount).isInstanceOf(WrapperException.class);
    }

    @Test
    void untypedDomNodeBecomesItsText() throws Exception {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        final org.w3c.dom.Element element = document.createElement("BoltNominalSize");
        element.setTextContent("M8");
        source.setUntyped(element);

        assertThat(upgraded().getUntyped()).isEqualTo("M8");
    }

    @Test
    void untypedValueIsReadThroughTheOtherAccessorPrefix() {
        source.setFlag("true");

        assertThat(upgraded().isFlag()).isTrue();
    }

    @Test
    void missingValueOfAPrimitiveIsItsDefault() {
        assertThat(upgraded().isFlag()).isFalse();
    }

    @Test
    void setterConvertsItsArgumentToTheTypeOfTheWrappedProperty() {
        final Item item = upgraded();

        item.setFlag(true);

        assertThat(source.getFlag()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void renamedPropertyIsReadFromItsOldName() {
        source.setLegacyName("legacy");

        assertThat(upgraded().getNewName()).isEqualTo("legacy");
    }

    @Test
    void valueOfASetterWithoutCounterpartIsKeptByTheProxy() {
        final Item item = upgraded();

        item.setExtra("extra");

        assertThat(item.getExtra()).isEqualTo("extra");
    }

    @Test
    void valueSetForAListPropertyIsKeptByTheProxy() {
        source.getParts().add(oldPart("original"));
        final Item item = upgraded();
        final Part replacement = new Part();

        item.setParts(replacement);

        assertThat(item.getParts()).isSameAs(replacement);
        assertThat(source.getParts()).extracting(
                        com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Part::getName)
                .containsExactly("original");
    }

    @Test
    void elementsWithoutCounterpartAreLeftOut() {
        source.getElements().add(
                new com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Element("kept"));
        source.getElements().add(new OnlyOldElement("removed"));

        assertThat(upgraded().getElements()).extracting(Element::getName).containsExactly("kept");
    }

    @Test
    void objectWithoutCounterpartHasNoProxy() {
        final Object proxy = context.getWrapperProxyFactory().createProxy(new OnlyOldElement("removed"));

        assertThat(proxy).isNull();
    }

    @Test
    void sameSourceClassMappedToDifferentClassesGetsDifferentProxyTypes() {
        final CompatibilityContext otherContext = new CompatibilityContext.CompatibilityContextBuilder()
                .withClassMapper(new OtherClassMapper())
                .build();

        final Object toNewModel = context.getWrapperProxyFactory().createProxy(oldPart("a"));
        final Object toOtherModel = otherContext.getWrapperProxyFactory().createProxy(oldPart("b"));

        assertThat(toNewModel).isInstanceOf(Part.class);
        assertThat(toOtherModel)
                .isInstanceOf(com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.othermodel.Part.class);
    }

    private Item upgraded() {
        return context.getWrapperProxyFactory().createProxy(source);
    }

    private com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Part oldPart(
            final String name) {
        return new com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Part(name);
    }

    private static final class EvolutionClassMapper extends NameBasedClassMapper
            implements PropertyAdditionProvider {

        private final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel.Item.class,
                          renamed("newName", "legacyName"));

        EvolutionClassMapper() {
            super(OLD_MODEL, NEW_MODEL);
        }

        @Override
        public PurePropertyAdditions getPropertyAdditions() {
            return additions;
        }

    }

    private static final class OtherClassMapper extends NameBasedClassMapper {

        OtherClassMapper() {
            super(OLD_MODEL, "com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.othermodel");
        }

    }

}
