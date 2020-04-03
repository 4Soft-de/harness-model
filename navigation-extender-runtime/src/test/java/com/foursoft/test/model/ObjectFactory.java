package com.foursoft.test.model;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.foursoft.harness.vec.model.vec113
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {
	}

	public Root createRoot() {
		return new Root();
	}

	public ChildA createChildA() {
		return new ChildA();
	}

	public ChildB createChildB() {
		return new ChildB();
	}

}
