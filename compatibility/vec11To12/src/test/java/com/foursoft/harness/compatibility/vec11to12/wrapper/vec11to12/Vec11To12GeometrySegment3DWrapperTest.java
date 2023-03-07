package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v113.VecBuildingBlockSpecification3D;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecGeometrySegment3D;
import com.foursoft.harness.vec.v113.VecReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class Vec11To12GeometrySegment3DWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent113 = new VecReader().read(inputStream);

            final List<VecGeometrySegment3D> vecGeometrySegments113 = vecContent113.getDocumentVersions().stream()
                    .map(documentVersion -> documentVersion
                            .getSpecificationsWithType(VecBuildingBlockSpecification3D.class))
                    .flatMap(Collection::stream)
                    .map(VecBuildingBlockSpecification3D::getGeometrySegments)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            final String xmlId113 = vecGeometrySegments113.get(0).getXmlId();

            final com.foursoft.harness.vec.v12x.VecContent vecContent120 =
                    get11To12Context().getWrapperProxyFactory().createProxy(vecContent113);

            final com.foursoft.harness.vec.v12x.VecNURBSCurve vecCurve3D = vecContent120.getDocumentVersions().stream()
                    .map(documentVersion -> documentVersion.getSpecificationsWithType(
                            com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D.class))
                    .flatMap(Collection::stream)
                    .map(com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D::getGeometrySegments)
                    .flatMap(List::stream)
                    .filter(geometrySegment3D -> xmlId113.equals(geometrySegment3D.getXmlId()))
                    .findFirst()
                    .map(com.foursoft.harness.vec.v12x.VecGeometrySegment3D::getCurves)
                    .map(curves -> curves.get(0))
                    .map(com.foursoft.harness.vec.v12x.VecNURBSCurve.class::cast)
                    .orElse(null);
            assertThat(vecCurve3D)
                    .isNotNull();
            assertThat(vecCurve3D.getControlPoints())
                    .isNotNull()
                    .isNotEmpty();
        }
    }

}   