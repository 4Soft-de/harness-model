package com.foursoft.harness.kbl.v25;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasSpecifiedOccurrences {

    Set<KblSpecifiedAccessoryOccurrence> getRefSpecifiedAccessoryOccurrence();

    Set<KblSpecifiedCavityPlugOccurrence> getRefSpecifiedCavityPlugOccurrence();

    Set<KblSpecifiedCavitySealOccurrence> getRefSpecifiedCavitySealOccurrence();

    Set<KblSpecifiedCoPackOccurrence> getRefSpecifiedCoPackOccurrence();

    Set<KblSpecifiedComponentBoxOccurrence> getRefSpecifiedComponentBoxOccurrence();

    Set<KblSpecifiedComponentOccurrence> getRefSpecifiedComponentOccurrence();

    Set<KblSpecifiedConnectorOccurrence> getRefSpecifiedConnectorOccurrence();

    Set<KblSpecifiedFixingOccurrence> getRefSpecifiedFixingOccurrence();

    Set<KblSpecifiedSpecialTerminalOccurrence> getRefSpecifiedSpecialTerminalOccurrence();

    Set<KblSpecifiedSpecialWireOccurrence> getRefSpecifiedSpecialWireOccurrence();

    Set<KblSpecifiedTerminalOccurrence> getRefSpecifiedTerminalOccurrence();

    Set<KblSpecifiedWireOccurrence> getRefSpecifiedWireOccurrence();

    Set<KblSpecifiedWireProtectionOccurrence> getRefSpecifiedWireProtectionOccurrence();

    default Set<HasRelatedAssembly> getSpecifiedOccurrences() {
        return Stream.of(getRefSpecifiedAccessoryOccurrence(),
                        getRefSpecifiedCavityPlugOccurrence(),
                        getRefSpecifiedCavitySealOccurrence(),
                        getRefSpecifiedCoPackOccurrence(),
                        getRefSpecifiedComponentBoxOccurrence(),
                        getRefSpecifiedComponentOccurrence(),
                        getRefSpecifiedConnectorOccurrence(),
                        getRefSpecifiedFixingOccurrence(),
                        getRefSpecifiedSpecialTerminalOccurrence(),
                        getRefSpecifiedSpecialWireOccurrence(),
                        getRefSpecifiedTerminalOccurrence(),
                        getRefSpecifiedWireOccurrence(),
                        getRefSpecifiedWireProtectionOccurrence())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
