/*-
 * ========================LICENSE_START=================================
 * NavExt XJC Plugin
 * %%
 * Copyright (C) 2019 - 2026 4Soft GmbH
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
package com.foursoft.harness.navext.xjc.plugin.openenum;

import com.foursoft.harness.navext.xjc.plugin.openenum.testruntime.CustomOpenEnumLiteral;
import com.foursoft.harness.navext.xjc.plugin.openenum.testruntime.OpenEnumLiteral;
import com.foursoft.harness.navext.xjc.plugin.openenum.testruntime.OpenEnumLiterals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Generates the fixture schema once and asserts what the plugin produced. The generated sources are
 * compiled and loaded, so anything the plugin emits that does not compile fails the whole class.
 */
class OpenEnumerationsPluginTest {

    private static final String PACKAGE = GeneratedModel.PACKAGE;

    private static GeneratedModel model;

    @BeforeAll
    static void generate() throws Exception {
        model = GeneratedModel.generate("standard", GeneratedModel.NAMES_OPTION);
    }

    @AfterEach
    void clearContributedLiterals() {
        OpenEnumLiterals.reload();
    }

    @Test
    void generatesAnInterfaceAndAnEnumPerOpenEnumeration() throws Exception {
        final Class<?> literalInterface = model.load(PACKAGE + ".DocumentTypeLiteral");
        final Class<?> literalEnum = model.load(PACKAGE + ".DocumentType");

        assertThat(literalInterface).isInterface();
        assertThat(OpenEnumLiteral.class).isAssignableFrom(literalInterface);
        assertThat(literalEnum.isEnum()).isTrue();
        assertThat(literalInterface).isAssignableFrom(literalEnum);

        assertThat(literalEnum.getEnumConstants())
                .extracting(constant -> ((OpenEnumLiteral) constant).value())
                .containsExactly("PartMaster", "HarnessDescription");
    }

    @Test
    void namesLiteralsThatAreNoJavaIdentifiers() throws Exception {
        final Class<?> boltSize = model.load(PACKAGE + ".BoltSize");

        assertThat(boltSize.getEnumConstants())
                .extracting(constant -> ((Enum<?>) constant).name())
                // "#0", "#1" and "12V" are named by sample-open-enum-names.xml, the rest by the
                // same algorithm XJC uses for closed enumerations, prefixed to stay identifiers.
                // The order is the order of the schema, which is what fixes the ordinals.
                .containsExactly("NO_0", "NO_1", "_1", "_1_2", "M_1_2", "V12", "NORMALLY_OPEN");
    }

    @Test
    void fromValueReturnsNullForAnUnknownLiteralInsteadOfThrowing() throws Exception {
        final Class<?> documentType = model.load(PACKAGE + ".DocumentType");
        final Method fromValue = documentType.getMethod("fromValue", String.class);

        assertThat(fromValue.invoke(null, "PartMaster")).isNotNull();
        assertThat(fromValue.invoke(null, "AcmeSpecification")).isNull();
        assertThat(fromValue.invoke(null, new Object[]{null})).isNull();
    }

    @Test
    void resolvesAnUnrecognizedLiteralToACustomLiteral() throws Exception {
        final OpenEnumLiteral literal = model.literalOf("DocumentType", "AcmeSpecification");

        assertThat(literal)
                .isInstanceOf(CustomOpenEnumLiteral.class)
                .isEqualTo(model.literalOf("DocumentType", "AcmeSpecification"))
                .isNotEqualTo(model.literalOf("DocumentType", "Other"))
                .returns("AcmeSpecification", OpenEnumLiteral::value)
                .returns(true, OpenEnumLiteral::isCustom);
    }

    @Test
    void prefersAContributedLiteralOverACustomOne() throws Exception {
        final Class<?> literalInterface = model.load(PACKAGE + ".DocumentTypeLiteral");
        final OpenEnumLiteral contributed = model.contributedLiteral(literalInterface, "AcmeSpecification");
        OpenEnumLiterals.contribute(contributed);

        assertThat(model.literalOf("DocumentType", "AcmeSpecification")).isSameAs(contributed);
    }

    @Test
    void prefersADefinedLiteralOverAContributedOne() throws Exception {
        final Class<?> literalInterface = model.load(PACKAGE + ".DocumentTypeLiteral");
        OpenEnumLiterals.contribute(model.contributedLiteral(literalInterface, "PartMaster"));

        assertThat(model.literalOf("DocumentType", "PartMaster"))
                .isInstanceOf(model.load(PACKAGE + ".DocumentType"));
    }

    @Test
    void addsTypedAccessorsForASingleValuedProperty() throws Exception {
        final Class<?> document = model.load(PACKAGE + ".Document");
        final Class<?> literalInterface = model.load(PACKAGE + ".DocumentTypeLiteral");
        final Object instance = document.getConstructor()
                .newInstance();

        // Unset means null, and only unset means null.
        assertThat(document.getMethod("getDocumentTypeLiteral")
                           .invoke(instance)).isNull();

        document.getMethod("setDocumentTypeLiteral", literalInterface)
                .invoke(instance, model.constant("DocumentType", "PART_MASTER"));
        assertThat(document.getMethod("getDocumentType")
                           .invoke(instance)).isEqualTo("PartMaster");

        // A custom literal read from a document comes back typed, not as null.
        document.getMethod("setDocumentType", String.class)
                .invoke(instance, "AcmeSpecification");
        assertThat(document.getMethod("getDocumentTypeLiteral")
                           .invoke(instance)).isInstanceOf(CustomOpenEnumLiteral.class);

        document.getMethod("setDocumentTypeLiteral", literalInterface)
                .invoke(instance, new Object[]{null});
        assertThat(document.getMethod("getDocumentType")
                           .invoke(instance)).isNull();
    }

    @Test
    void addsTypedAccessorsForARepeatingProperty() throws Exception {
        final Class<?> document = model.load(PACKAGE + ".Document");
        final Class<?> literalInterface = model.load(PACKAGE + ".BoltSizeLiteral");
        final Object instance = document.getConstructor()
                .newInstance();

        document.getMethod("addBoltSizeLiteral", literalInterface)
                .invoke(instance, model.constant("BoltSize", "NO_1"));
        document.getMethod("addBoltSizeLiteral", literalInterface)
                .invoke(instance, model.constant("BoltSize", "V12"));

        assertThat((List<?>) document.getMethod("getBoltSize")
                .invoke(instance)).extracting(Object::toString)
                .containsExactly("#1", "12V");
        assertThat((List<?>) document.getMethod("getBoltSizeLiterals")
                .invoke(instance))
                .extracting(literal -> ((OpenEnumLiteral) literal).value())
                .containsExactly("#1", "12V");

        document.getMethod("setBoltSizeLiterals", java.util.Collection.class)
                .invoke(instance, List.of(model.constant("BoltSize", "NORMALLY_OPEN")));
        assertThat((List<?>) document.getMethod("getBoltSize")
                .invoke(instance)).extracting(Object::toString)
                .containsExactly("Normally open");
    }

    @Test
    void returnsAnUnmodifiableSnapshotOfTheLiterals() throws Exception {
        final Class<?> document = model.load(PACKAGE + ".Document");
        final Object instance = document.getConstructor()
                .newInstance();

        final List<?> literals = (List<?>) document.getMethod("getBoltSizeLiterals")
                .invoke(instance);

        assertThatThrownBy(() -> literals.add(null)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void doesNotOverloadTheExistingAccessors() throws Exception {
        final Class<?> document = model.load(PACKAGE + ".Document");

        // An overloaded setX(Literal) would make setX(null) ambiguous and break callers.
        assertThat(Arrays.stream(document.getMethods())
                           .filter(method -> "setDocumentType".equals(method.getName()))
                           .toList()).hasSize(1);
    }

    @Test
    void leavesClosedEnumerationsAndPlainStringsAlone() throws Exception {
        final Class<?> document = model.load(PACKAGE + ".Document");
        final Class<?> anchorType = model.load(PACKAGE + ".AnchorType");

        assertThat(anchorType.isEnum()).isTrue();
        assertThat(model.exists(PACKAGE + ".AnchorTypeLiteral")).isFalse();
        assertThat(methodNames(document))
                .isNotEmpty()
                .doesNotContain("getAnchorLiteral", "getTitleLiteral");

        // XJC keeps generating the throwing fromValue for closed enumerations.
        final Method fromValue = anchorType.getMethod("fromValue", String.class);
        assertThatThrownBy(() -> fromValue.invoke(null, "Nope"))
                .hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void skipsOpenEnumerationsWithoutAnyLiteral() {
        assertThat(model.exists(PACKAGE + ".UndefinedLiteral")).isFalse();
        assertThat(model.exists(PACKAGE + ".Undefined")).isFalse();
    }

    @Test
    void generatesUnusedOpenEnumerationsAsWell() {
        // Nothing references it, but it is part of the vocabulary the standard defines.
        assertThat(model.exists(PACKAGE + ".UnusedLiteral")).isTrue();
        assertThat(model.exists(PACKAGE + ".Unused")).isTrue();
    }

    @Test
    void failsTheBuildOnAConstantNameCollisionWithoutAnOverride() {
        // "#1" and "1" of BoltSize mangle to the same name, so generating without the override file
        // must fail loudly instead of silently renaming one of them.
        assertThatThrownBy(() -> GeneratedModel.generate("collision"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("resolve to the constant name");
    }

    private static List<String> methodNames(final Class<?> type) {
        return Arrays.stream(type.getMethods())
                .map(Method::getName)
                .toList();
    }

}
