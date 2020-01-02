# jaxb-enhanced-navigation
A library for enhanced navigations for JAXB models. It consists of two parts: An xjc-plugin (compile time) and a runtime component.

# Key Features are:

- Typesafe Idref-Relations (also Idrefs). The type can be defined as an abstract type if required
- Typesafe Parent navigation (multiple parents are supported - at runtime of course only one can be set)
- Bi-directional typesafe navigations
- Id-Lookup Map as gimmick when loading an XML-File with this lib. The XML-Id is used as key.

The last point was the main driver for this project and has helped us a lot. Without it the customer project would not have been possible. 


# Details
The get a better understanding of this lib please have a look at our blog post about it based on a real project.
https://www.4soft.de/blog/2018/navigation-on-large-xml-structures/

# Limitations
There is one major limitation of this project - it can only be used in read-only usecases! Non of the data structure are updated (parent, back-navigation, id-lookup, etc). 
