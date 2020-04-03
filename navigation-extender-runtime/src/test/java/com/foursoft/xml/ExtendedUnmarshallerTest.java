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
