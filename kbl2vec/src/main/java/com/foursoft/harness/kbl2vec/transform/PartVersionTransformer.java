/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl.v25.visitor.StrictBaseVisitor;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.TransformationResult.Builder;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class PartVersionTransformer implements Transformer<KblPart, VecPartVersion> {

    private final PrimaryPartTypeVisitor primaryPartTypeVisitor = new PrimaryPartTypeVisitor();

    @Override
    public TransformationResult<VecPartVersion> transform(
            final TransformationContext context, final KblPart source) {
        final VecPartVersion partVersion = new VecPartVersion();

        final Converter<String, Optional<VecLocalizedString>> stringConverter =
                context.getConverterRegistry().getStringToLocalizedString();

        stringConverter.convert(source.getAbbreviation())
                .ifPresent(v -> partVersion.getAbbreviations()
                        .add(v));
        stringConverter.convert(source.getDescription())
                .ifPresent(v -> partVersion.getDescriptions()
                        .add(v));

        partVersion.setCompanyName(source.getCompanyName());
        partVersion.setPartNumber(source.getPartNumber());
        partVersion.setPartVersion(source.getVersion());
        partVersion.setPrimaryPartType(source.accept(primaryPartTypeVisitor));

        final Builder<VecPartVersion> resultBuilder = TransformationResult.from(partVersion);
        if (StringUtils.isNotBlank(source.getPartNumberType())) {
            context.getLogger().warn("'{}' uses Part_number_type {} cannot be mapped at the moment", source,
                                     source.getPartNumberType());
            resultBuilder.withComment("Part_number_type cannot be mapped at the moment (see KBLFRM-1267)");
        }

        //TODO: Change
        //TODO: Copyright is not that easy
//        if (!StringUtils.isBlank(source.getCopyrightNote())) {
//            resultBuilder.withLinker(Query.of(source), VecCopyrightInformation.class,
//                                     partVersion::setCopyrightInformation);
//        }
        return resultBuilder.withDownstream(KblAliasIdentification.class, VecAliasIdentification.class,
                                            source::getAliasIds, VecPartVersion::getAliasIds)
                .build();
    }

    //TODO: This should be a strategy, as it can become complex
    private static class PrimaryPartTypeVisitor extends StrictBaseVisitor<VecPrimaryPartType, RuntimeException> {

        @Override
        public VecPrimaryPartType visitKblAccessory(final KblAccessory aBean) throws RuntimeException {
            return VecPrimaryPartType.OTHER;
        }

        @Override public VecPrimaryPartType visitKblCoPackPart(final KblCoPackPart aBean) throws RuntimeException {
            return VecPrimaryPartType.OTHER;
        }

        @Override
        public VecPrimaryPartType visitKblCavityPlug(final KblCavityPlug aBean) throws RuntimeException {
            return VecPrimaryPartType.CAVITY_PLUG;
        }

        @Override
        public VecPrimaryPartType visitKblCavitySeal(final KblCavitySeal aBean) throws RuntimeException {
            return VecPrimaryPartType.CAVITY_SEAL;
        }

        @Override
        public VecPrimaryPartType visitKblGeneralWire(final KblGeneralWire aBean) throws RuntimeException {
            return VecPrimaryPartType.WIRE;
        }

        @Override
        public VecPrimaryPartType visitKblConnectorHousing(final KblConnectorHousing aBean) throws RuntimeException {
            return VecPrimaryPartType.CONNECTOR_HOUSING;
        }

        @Override
        public VecPrimaryPartType visitKblFixing(final KblFixing aBean) throws RuntimeException {
            return VecPrimaryPartType.FIXING;
        }

        @Override
        public VecPrimaryPartType visitKblGeneralTerminal(final KblGeneralTerminal aBean) throws RuntimeException {
            return VecPrimaryPartType.TERMINAL;
        }

        @Override
        public VecPrimaryPartType visitKblWireProtection(final KblWireProtection aBean) throws RuntimeException {
            return VecPrimaryPartType.WIRE_PROTECTION;
        }

        @Override
        public VecPrimaryPartType visitKblHarness(final KblHarness aBean) throws RuntimeException {
            return VecPrimaryPartType.PART_STRUCTURE;
        }

        @Override public VecPrimaryPartType visitKblHarnessConfiguration(final KblHarnessConfiguration aBean)
                throws RuntimeException {
            return VecPrimaryPartType.PART_STRUCTURE;
        }

        @Override
        public VecPrimaryPartType visitKblModule(final KblModule aBean) throws RuntimeException {
            return VecPrimaryPartType.PART_STRUCTURE;
        }

        @Override
        public VecPrimaryPartType visitKblAssemblyPart(final KblAssemblyPart aBean) throws RuntimeException {
            return VecPrimaryPartType.PART_STRUCTURE;
        }

        @Override
        public VecPrimaryPartType visitKblComponent(final KblComponent aBean) throws RuntimeException {
            return VecPrimaryPartType.EE_COMPONENT;
        }

        @Override
        public VecPrimaryPartType visitKblFuse(final KblFuse aBean) throws RuntimeException {
            return VecPrimaryPartType.FUSE;
        }

        @Override
        public VecPrimaryPartType visitKblComponentBox(final KblComponentBox aBean) throws RuntimeException {
            return VecPrimaryPartType.EE_COMPONENT;
        }

    }
}
