/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
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
package com.foursoft.xml;

import com.foursoft.xml.model.AbstractBase;
import com.foursoft.xml.model.ChildA;
import com.foursoft.xml.model.ChildB;
import com.foursoft.xml.model.Root;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;


public class ExtendedUnmarshallerTest {

    @Test
    public void consumerTest() throws JAXBException {
        final List<ValidationEvent> result1 = new ArrayList<>();
        final Consumer<ValidationEvent> consumer1 = o -> {
            result1.add(o);
        };

        final ExtendedUnmarshaller<Root, AbstractBase> extUnmarshaller =
                new ExtendedUnmarshaller<Root, AbstractBase>(Root.class)
                        .withBackReferences()
                        .withIdMapper(AbstractBase.class, AbstractBase::getXmlId)
                        .withEventLogging(consumer1);

        final Unmarshaller unmarshaller = extUnmarshaller.getUnmarshaller();
        final ValidationEvent event = new ValidationEvent() {
            @Override
            public int getSeverity() {
                return 0;
            }

            @Override
            public String getMessage() {
                return null;
            }

            @Override
            public Throwable getLinkedException() {
                return null;
            }

            @Override
            public ValidationEventLocator getLocator() {
                return null;
            }
        };
        unmarshaller.getEventHandler().handleEvent(event);
        Assertions.assertEquals(1, result1.size());

    }

    /**
     * if ExtendedUnmarshaller is reused and withEventLogging called multiple times,the old
     * event consumer were not deleted!
     *
     * @throws JAXBException
     */
    @Test
    public void multipleConsumerTest() throws JAXBException {
        final List<ValidationEvent> result1 = new ArrayList<>();
        final Consumer<ValidationEvent> consumer1 = o -> {
            result1.add(o);
        };
        final List<ValidationEvent> result2 = new ArrayList<>();
        final Consumer<ValidationEvent> consumer2 = o -> {
            result2.add(o);
        };
        final ExtendedUnmarshaller<Root, AbstractBase> extUnmarshaller1 =
                new ExtendedUnmarshaller<Root, AbstractBase>(Root.class)
                        .withBackReferences()
                        .withIdMapper(AbstractBase.class, AbstractBase::getXmlId)
                        .withEventLogging(consumer1);
        extUnmarshaller1.withEventLogging(consumer2);

        final Unmarshaller unmarshaller = extUnmarshaller1.getUnmarshaller();
        final ValidationEvent event = new ValidationEvent() {
            @Override
            public int getSeverity() {
                return 0;
            }

            @Override
            public String getMessage() {
                return null;
            }

            @Override
            public Throwable getLinkedException() {
                return null;
            }

            @Override
            public ValidationEventLocator getLocator() {
                return null;
            }
        };
        unmarshaller.getEventHandler().handleEvent(event);
        Assertions.assertEquals(0, result1.size());
        Assertions.assertEquals(1, result2.size());

    }

    @Test
    public void unmarshallTest() throws JAXBException {
        final ExtendedUnmarshaller<Root, AbstractBase> unmarshaller = new ExtendedUnmarshaller<Root, AbstractBase>(
                Root.class).withBackReferences()
                .withIdMapper(AbstractBase.class, AbstractBase::getXmlId);

        final JaxbModel<Root, AbstractBase> unmarshalledResult = unmarshaller.unmarshall(
                getClass().getResourceAsStream("/basic" + "/basic-test.xml"));

        final Root rootElement = unmarshalledResult.getRootElement();

        assertThat(rootElement).isNotNull();

        // Check Id Lookup initialized & parent associations
        assertThat(unmarshalledResult.getIdLookup()
                .findById(ChildB.class, "id_6")).isNotNull()
                .isNotEmpty()
                .get()
                .returns("id_6", ChildB::getXmlId)
                .returns(rootElement, ChildB::getRoot)
                .returns("another value", ChildB::getXyz);

        final ChildA childAone = rootElement.getChildA()
                .get(0);
        final ChildA childATwo = rootElement.getChildA()
                .get(1);
        // Check Backref is working
        assertThat(unmarshalledResult.getIdLookup()
                .findById(ChildB.class, "id_6")
                .get()
                .getReverseChildA()).containsExactlyInAnyOrder(childAone);

        assertThat(unmarshalledResult.getIdLookup()
                .findById(ChildB.class, "id_7")
                .get()
                .getReverseChildA()).containsExactlyInAnyOrder(childAone, childATwo);

        assertThat(unmarshalledResult.getIdLookup()
                .findById(ChildB.class, "id_8")
                .get()
                .getReverseChildA()).containsExactlyInAnyOrder(childATwo);

    }

}
