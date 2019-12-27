package de.foursoft.harness.xml.xjc.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jvnet.jaxb2.maven2.AbstractXJC2Mojo;

public class EmptyListHandlerPluginTest extends AbstractPluginTest {

    @Override
    protected String getTestName() {
        return "emptylist";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void configureMojo(final AbstractXJC2Mojo mojo) {
        super.configureMojo(mojo);

        mojo.setForceRegenerate(true);

        mojo.setBindingIncludes(new String[] { "basic.xjb" });

        mojo.setExtension(true);

    }

    @Override
    public List<String> getArgs() {

        final List<String> args = new ArrayList<>(super.getArgs());

        args.add("-Xext-navs");
        args.add("-Xnull-empty-lists");

        return args;

    }

    @Override
    public void testExecute() throws Exception {
        super.testExecute();

        assertTypeSafeIDREF();

    }

    private void assertTypeSafeIDREF() throws Exception {
        final Class<?> forName = Class.forName("de.foursoft.harness.xml.xjc.test.NavsChildA");

        final Method m = forName.getMethod("getRefBs");

        assertThat(m.getGenericReturnType()
                .getTypeName()).isEqualTo("java.util.List<de.foursoft.harness.xml.xjc.test.NavsChildB>");

    }

}
