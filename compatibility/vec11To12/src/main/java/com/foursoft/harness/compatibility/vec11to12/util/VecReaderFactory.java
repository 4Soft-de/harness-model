package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XCompatibilityWrapper;
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Reader factory to create a VEC 1.2.X {@link VecContent}.
 */
public class VecReaderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecReaderFactory.class);

    private VecReaderFactory() {
        //hidden Constructor
    }

    /**
     * Creates a VEC 1.2.X {@link VecContent} from the given {@link InputStream}.
     * If the given {@link VecVersion} is a {@link VecVersion#VEC11X}, it will be converted to a VEC 1.2.X.
     *
     * @param vecVersion                   VecVersion of the InputStream.
     * @param inputStream                  InputStream to read and create the VecContent from.
     * @param eventConsumer                Consumer to consume {@link ValidationEvent}s.
     * @param compatibilityContextConsumer Consumer to consume the {@link CompatibilityContext} of
     *                                     an additionally created {@link CompatibilityWrapper}.
     *                                     Can be used to further adjust the creation of the VecContent.
     */
    public static VecContent createContent(final VecVersion vecVersion, final InputStream inputStream,
                                           final Consumer<ValidationEvent> eventConsumer,
                                           final Consumer<CompatibilityContext> compatibilityContextConsumer) {
        if (VecVersion.VEC11X == vecVersion) {
            final com.foursoft.harness.vec.v113.VecReader vec113Reader =
                    new com.foursoft.harness.vec.v113.VecReader(eventConsumer);
            LOGGER.debug("Trying to read VEC {}.", VecVersion.VEC11X.getCurrentVersion());

            final com.foursoft.harness.vec.v113.VecContent vecContent = vec113Reader.read(inputStream);
            final Vec11XTo12XCompatibilityWrapper vec11XTo12XCompatibilityWrapper =
                    new Vec11XTo12XCompatibilityWrapper();

            final CompatibilityContext compatibilityContext = vec11XTo12XCompatibilityWrapper.getContext();
            if (compatibilityContextConsumer != null) {
                compatibilityContextConsumer.accept(compatibilityContext);
            }

            return compatibilityContext.getWrapperProxyFactory().createProxy(vecContent);
        }

        if (VecVersion.VEC12X == vecVersion) {
            LOGGER.debug("Trying to read VEC {}.", VecVersion.VEC12X.getCurrentVersion());

            final VecReader vec12xReader = new VecReader(eventConsumer);
            return vec12xReader.read(inputStream);
        }

        final String version = vecVersion == null ? "-" : vecVersion.getIdentifier();
        throw new IllegalArgumentException("Not supported VEC version: " + version);
    }

    /**
     * Creates a VEC 1.2.X {@link VecContent} from the given {@link InputStream}.
     * If the given {@link VecVersion} is a {@link VecVersion#VEC11X}, it will be converted to a VEC 1.2.X.
     *
     * @param vecVersion    VecVersion of the InputStream.
     * @param inputStream   InputStream to read and create the VecContent from.
     * @param eventConsumer Consumer to consume {@link ValidationEvent}s.
     */
    public static VecContent createContent(final VecVersion vecVersion, final InputStream inputStream,
                                           final Consumer<ValidationEvent> eventConsumer) {
        return createContent(vecVersion, inputStream, eventConsumer, null);
    }

}
