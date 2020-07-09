package com.foursoft.xml.io.read;

import com.foursoft.vecmodel.vec113.VecContent;
import com.foursoft.xml.model.Identifiable;

public class Vec113Reader extends CachedXMLReader<VecContent, Identifiable> {

    private static final ThreadLocal<Vec113Reader> localReader = ThreadLocal.withInitial(Vec113Reader::new);

    private Vec113Reader() {
        super(VecContent.class, Identifiable.class, Identifiable::getXmlId);
    }

    public static Vec113Reader getCachedReader() {
        return localReader.get();
    }
}
