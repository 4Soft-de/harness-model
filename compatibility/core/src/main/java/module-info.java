module com.foursoft.harness.compatibility.core {
    requires net.bytebuddy;
    requires org.slf4j;
    requires java.xml.bind;
    requires org.reflections;

    exports com.foursoft.harness.compatibility.core;
    exports com.foursoft.harness.compatibility.core.common;
    exports com.foursoft.harness.compatibility.core.exception;
    exports com.foursoft.harness.compatibility.core.mapping;
    exports com.foursoft.harness.compatibility.core.util;
    exports com.foursoft.harness.compatibility.core.wrapper;
}