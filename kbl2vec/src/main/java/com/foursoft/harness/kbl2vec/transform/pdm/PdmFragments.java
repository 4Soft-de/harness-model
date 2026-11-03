/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.kbl2vec.transform.pdm;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblApproval;
import com.foursoft.harness.kbl.v25.KblChange;
import com.foursoft.harness.kbl.v25.KblCreation;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl.v25.KblPartWithTitleBlock;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationFragment;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecApproval;
import com.foursoft.harness.vec.v2x.VecChangeDescription;
import com.foursoft.harness.vec.v2x.VecCopyrightInformation;
import com.foursoft.harness.vec.v2x.VecCreation;
import com.foursoft.harness.vec.v2x.VecItemVersion;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.isUndefined;

/**
 * The PDM information of a part (GL pdm-information): copyright, changes, creation and approvals.
 */
public final class PdmFragments {

    private PdmFragments() {
        // hide default constructor
    }

    /**
     * References the {@link VecCopyrightInformation} of the part's copyright note.
     */
    public static <D extends VecItemVersion> TransformationFragment<D, TransformationResult.Builder<D>> copyright(
            final KblPart source) {
        return (itemVersion, builder) -> {
            if (!isUndefined(source.getCopyrightNote())) {
                builder.withLinker(Query.of(source), VecCopyrightInformation.class,
                                   VecItemVersion::setCopyrightInformation);
            }
        };
    }

    /**
     * The changes of the part and, for a part with a title block, the creation and the approvals that are applied to
     * it. They describe the document of a part, therefore they belong to its document version. Only a part without a
     * document version (a module) gets them in its part version.
     */
    public static <D extends VecItemVersion> TransformationFragment<D, TransformationResult.Builder<D>> history(
            final KblPart source, final TransformationContext context) {
        return (itemVersion, builder) -> {
            builder.withDownstream(KblChange.class, VecChangeDescription.class, source::getChanges,
                                   VecItemVersion::getChangeDescriptions);
            if (source instanceof final KblPartWithTitleBlock titleBlock) {
                builder.withDownstream(KblApproval.class, VecApproval.class,
                                       () -> inContainerOrder(titleBlock.getRefApproval(), KBLContainer::getApprovals),
                                       VecItemVersion::getApprovals);
                builder.withDownstream(KblCreation.class, VecCreation.class, () -> creation(titleBlock, context),
                                       VecItemVersion::setCreation);
            }
        };
    }

    private static List<KblCreation> creation(final KblPartWithTitleBlock source, final TransformationContext context) {
        final List<KblCreation> creations = inContainerOrder(source.getRefCreation(), KBLContainer::getCreations);
        if (creations.size() > 1) {
            context.getLogger().warn("{} creations are applied to '{}', only the first one is transformed.",
                                     creations.size(), source);
        }
        return creations.stream().limit(1).toList();
    }

    /**
     * The back references are unordered, they are sorted like the elements of the container (or by their XML id, if
     * the container is unknown).
     */
    private static <T extends Identifiable> List<T> inContainerOrder(
            final Set<T> elements, final Function<KBLContainer, List<T>> ofContainer) {
        if (elements.isEmpty()) {
            return List.of();
        }
        final KBLContainer container = containerOf(elements.iterator().next());
        if (container == null) {
            return elements.stream()
                    .sorted(Comparator.comparing(Identifiable::getXmlId,
                                                 Comparator.nullsFirst(Comparator.naturalOrder())))
                    .toList();
        }
        return ofContainer.apply(container).stream().filter(elements::contains).toList();
    }

    private static KBLContainer containerOf(final Identifiable element) {
        if (element instanceof final KblApproval approval) {
            return approval.getParentKBLContainer();
        }
        if (element instanceof final KblCreation creation) {
            return creation.getParentKBLContainer();
        }
        return null;
    }
}
