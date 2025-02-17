package com.foursoft.harness.kbl2vec.post;

import com.foursoft.harness.kbl2vec.core.Processor;
import com.foursoft.harness.kbl2vec.utils.XmlIdGeneratingTraverser;
import com.foursoft.harness.kbl2vec.utils.XmlIdGenerator;
import com.foursoft.harness.vec.v2x.VecContent;

public class XmlIdPostProcessor implements Processor<VecContent> {
    private final XmlIdGenerator xmlIdGenerator = new XmlIdGenerator();

    @Override
    public VecContent apply(final VecContent source) {
        source.accept(new XmlIdGeneratingTraverser(xmlIdGenerator));
        return source;
    }
}
