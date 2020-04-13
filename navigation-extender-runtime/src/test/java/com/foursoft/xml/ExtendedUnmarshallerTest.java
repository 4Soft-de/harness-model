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

import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import com.foursoft.test.model.AbstractBase;
import com.foursoft.test.model.ChildA;
import com.foursoft.test.model.ChildB;
import com.foursoft.test.model.Root;

public class ExtendedUnmarshallerTest {

	@Test
	public void unmarshallTest() throws JAXBException {
		final ExtendedUnmarshaller<Root, AbstractBase> unmarshaller = new ExtendedUnmarshaller<Root, AbstractBase>(
				Root.class).withBackReferences()
						.withIdMapper(AbstractBase.class, AbstractBase::getXmlId);

		final JaxbModel<Root, AbstractBase> unmarshalledResult = unmarshaller.unmarshall(this.getClass()
				.getResourceAsStream("/basic-test.xml"));

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
				.getReverseChildA()).containsExactly(childAone);

		assertThat(unmarshalledResult.getIdLookup()
				.findById(ChildB.class, "id_7")
				.get()
				.getReverseChildA()).containsExactly(childAone, childATwo);

		assertThat(unmarshalledResult.getIdLookup()
				.findById(ChildB.class, "id_8")
				.get()
				.getReverseChildA()).containsExactly(childATwo);

	}

}
