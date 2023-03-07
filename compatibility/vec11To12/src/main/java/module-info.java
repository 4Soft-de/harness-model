module com.foursoft.harness.compatibility.vec11to12 {
    requires transitive com.foursoft.harness.compatibility.core;
    requires org.slf4j;
    requires java.xml;
    requires java.xml.bind;
    requires com.foursoft.harness.vec.v113;
    requires com.foursoft.harness.vec.v12x;
    requires org.reflections;

    exports com.foursoft.harness.compatibility.vec11to12;
    exports com.foursoft.harness.compatibility.vec11to12.common;
    exports com.foursoft.harness.compatibility.vec11to12.common.util;
    exports com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;
    exports com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;
    exports com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.field;
    exports com.foursoft.harness.compatibility.vec11to12.util;
}