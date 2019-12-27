package de.foursoft.harness.xml.xjc.plugin;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class PropertyBuilder {

    private static final String GETTER_PREFIX = "get";

    private final JCodeModel codeModel;
    private String name;
    private String getterName;
    private JType baseType;
    private JExpression init;
    private JDocComment getterJavadoc;

    public PropertyBuilder(final JCodeModel codeModel) {
        this.codeModel = codeModel;
    }

    public PropertyBuilder withName(final String name) {
        this.name = name;
        final String suffix = name.substring(0, 1)
                .toUpperCase() + name.substring(1);
        getterName = GETTER_PREFIX + suffix;
        return this;
    }

    public PropertyBuilder withBaseType(final String typeName) throws ClassNotFoundException {
        baseType = codeModel.parseType(typeName);
        return this;
    }

    public PropertyBuilder withBaseType(final JType baseType) {
        this.baseType = baseType;
        return this;
    }

    public JFieldVar build(final JDefinedClass targetClass) {
        final JFieldVar field = targetClass.field(JMod.PRIVATE, baseType, name);
        if (init != null) {
            field.init(init);
        }

        field.annotate(codeModel.ref(XmlTransient.class));

        final JMethod getter = targetClass.method(JMod.PUBLIC, baseType, getterName);
        getter.body()
                ._return(JExpr.ref(field.name()));

        if (getterJavadoc != null) {
            getter.javadoc()
                    .addAll(getterJavadoc);
        }

        return field;
    }

    public void buildAbstract(final JDefinedClass targetClass) {
        if (targetClass.getMethod(getterName, new JType[] {}) != null) {
            return;
        }
        final JMethod getter = targetClass.method(JMod.PUBLIC, baseType, getterName);

        if (getterJavadoc != null) {
            getter.javadoc()
                    .addAll(getterJavadoc);
        }

    }

    public PropertyBuilder withInit(final JExpression init) {
        this.init = init;
        return this;
    }

    public PropertyBuilder withGetterJavadoc(final JDocComment comment) {
        this.getterJavadoc = comment;
        return this;
    }
}
