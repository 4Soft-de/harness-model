package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.factories.NumericalValueFactory;
import com.foursoft.harness.vec.v2x.VecIntegerValueProperty;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecTerminalPairing;
import com.foursoft.harness.vec.v2x.VecTerminalPairingSpecification;

import java.math.BigInteger;

public class TerminalPairingBuilder implements Builder<VecTerminalPairingSpecification> {

    private final VecSession session;
    private final VecTerminalPairingSpecification specification = new VecTerminalPairingSpecification();
    private final VecTerminalPairing pairing = new VecTerminalPairing();

    TerminalPairingBuilder(final VecSession session, final VecPartVersion firstPartNumber,
                           final VecPartVersion secondPartNumber) {
        this.session = session;

        specification.getTerminalPairings().add(pairing);
        pairing.setFirstTerminal(firstPartNumber);
        pairing.setSecondTerminal(secondPartNumber);
    }

    public TerminalPairingBuilder withMatingForce(final double force, final double lowerTolerance,
                                                  final double upperTolerance) {
        pairing.setMatingForce(
                NumericalValueFactory.valueWithTolerance(force, lowerTolerance, upperTolerance, session.newton()));
        return this;
    }

    public TerminalPairingBuilder withUnMatingForce(final double force, final double lowerTolerance,
                                                    final double upperTolerance) {
        pairing.setUnmatingForce(
                NumericalValueFactory.valueWithTolerance(force, lowerTolerance, upperTolerance, session.newton()));
        return this;
    }

    public TerminalPairingBuilder withNumberOfMatingCycles(final int numberOfMatingCycles) {
        final VecIntegerValueProperty value = new VecIntegerValueProperty();
        value.setValue(BigInteger.valueOf(numberOfMatingCycles));
        value.setPropertyType("numberOfMatingCycles");

        pairing.getCustomProperties().add(value);
        return this;
    }

    @Override public VecTerminalPairingSpecification build() {
        return this.specification;
    }
}
