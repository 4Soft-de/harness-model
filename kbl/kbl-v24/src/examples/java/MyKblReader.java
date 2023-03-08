/*-
 * ========================LICENSE_START=================================
 * KBL 2.4
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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

import com.foursoft.harness.kbl.v24.*;
import com.foursoft.harness.navext.runtime.JaxbModel;
import com.foursoft.harness.navext.runtime.model.Identifiable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MyKblReader {

    public static void readKblFile(final String pathToFile) throws IOException {
        try (final InputStream is = MyKblReader.class.getResourceAsStream(pathToFile)) {
            final KblReader kblReader = new KblReader();
            final JaxbModel<KBLContainer, Identifiable> model = kblReader.readModel(is);

            final KblConnectorOccurrence occurrence = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, "I1616")
                    .orElse(null);

            final KblConnectorHousing part = model.getIdLookup()
                    .findById(KblConnectorHousing.class, "ch_part")
                    .orElse(null);

            if (occurrence == null || part == null) {
                return;
            }

            final boolean equalsPart = occurrence.getPart().equals(part);
            System.out.println("Part via. lookup identical to getter call? " + (equalsPart));

            final KblHarness parentHarness = occurrence.getParentHarness();
            final KblHarness harnessOfRootElement = model.getRootElement().getHarness();

            final boolean equalsHarness = harnessOfRootElement.equals(parentHarness);
            System.out.println("Harness of root element identical to parent harness of occurrence? " + (equalsHarness));

            final Set<KblConnectorOccurrence> singleton = Collections.singleton(occurrence);
            final Set<KblConnectorOccurrence> refConnectorOccurrence = part.getRefConnectorOccurrence();

            final boolean equalsOccurrence = refConnectorOccurrence.equals(singleton);
            System.out.println(
                    "Occurrence being equals to the referenced connector of the part? " + (equalsOccurrence));
        }
    }

    public static void getBackReferences(final String pathToFile) throws IOException {
        try (final InputStream is = MyKblReader.class.getResourceAsStream(pathToFile)) {
            final KblReader kblReader = new KblReader();
            final JaxbModel<KBLContainer, Identifiable> model = kblReader.readModel(is);
            final KBLContainer container = model.getRootElement();

            final List<KblConnectorHousing> connectorHousings = container.getConnectorHousings();
            if (connectorHousings.isEmpty()) {
                return;
            }
            final KblConnectorHousing kblConnectorHousing = connectorHousings.get(0);

            final Set<KblConnectorOccurrence> refConnectorOccurrence = kblConnectorHousing.getRefConnectorOccurrence();
            final KblConnectorOccurrence kblConnectorOccurrence = refConnectorOccurrence.stream().findFirst().orElse(
                    null);
            if (kblConnectorOccurrence == null) {
                return;
            }

            // KblConnectorOccurrence -> KblHarness
            final KblHarness parentHarness = kblConnectorOccurrence.getParentHarness();
            // KblHarness -> KblConnectorOccurrence
            final KblConnectorOccurrence occurrenceByHarness = parentHarness.getConnectorOccurrences().stream()
                    .findFirst()
                    .orElse(null);
            if (occurrenceByHarness == null) {
                return;
            }
            System.out.println("KblConnectorOccurrence from KblContainer = KKblConnectorOccurrence from KblHarness? " +
                                       (kblConnectorOccurrence.equals(occurrenceByHarness)));

            final String xmlId = kblConnectorOccurrence.getXmlId();
            final KblConnectorOccurrence occurrenceByLookup = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, xmlId)
                    .orElse(null);
            if (occurrenceByLookup == null) {
                return;
            }
            System.out.println("KblConnectorOccurrence from KblContainer = KblConnectorOccurrence by id lookup? " +
                                       (kblConnectorOccurrence.equals(occurrenceByLookup)));
        }
    }
}
