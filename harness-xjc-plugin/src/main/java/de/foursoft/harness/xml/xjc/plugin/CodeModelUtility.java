package de.foursoft.harness.xml.xjc.plugin;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

import de.foursoft.harness.xml.ExtendedUnmarshaller;

public final class CodeModelUtility {

    private CodeModelUtility() {
        // HIDE
    }

    public static JDefinedClass findSchemaType(final Outline outline, final String schemaTypeName) {
        for (final ClassOutline classOutline : outline.getClasses()) {
            if (classOutline.target.getTypeName()
                    .getLocalPart()
                    .equals(schemaTypeName)) {
                return classOutline.implClass;
            }
        }
        throw new RuntimeException(schemaTypeName
                + " is not a type in this schema and has no generated class, so no backreference can be set!");
    }

    public static JDocComment appendGetterDisclaimer(final JDocComment comment, final JCodeModel codeModel) {
        return comment.append(String.format("%n"))
                .append("<b>Warning!:</b> This is a readonly property! It has to be initialized during the unmarshalling process by the ")
                .append(codeModel.ref(ExtendedUnmarshaller.class))
                .append(".<br/>")
                .append("This property is consistent to the state of object model at the time of unmarshalling. It does <b>not</b> reflect any changes done in the object model after the unmarshalling.<br/>")
                .append(String.format("%n"))
                .append("This property has no effect when the object is marshalled to xml.");
    }

}
