package xxx.joker.apps.tagmod.console.config;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.format.JkColumnFmtBuilder;
import xxx.joker.libs.core.utils.JkConverter;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcConfig {

    public static final int MAX_HALF_DISPLAY_WIDTH = 85;
    public static final TxtEncoding DEFAULT_OUTPUT_ENCODING = TxtEncoding.UTF_16;
    public static final int DEFAULT_OUTPUT_VERSION = 4;
    public static final boolean DEFAULT_OUTPUT_UNSYNCHRONISATION = false;

    public static String toStringConfigurations() {
        JkColumnFmtBuilder builder = new JkColumnFmtBuilder();
        builder.addLines(strf("%s:|%d", "MAX_HALF_DISPLAY_WIDTH", MAX_HALF_DISPLAY_WIDTH));
        builder.addLines(strf("%s:|%s", "DEFAULT_OUTPUT_ENCODING", DEFAULT_OUTPUT_ENCODING.getLabel()));
        builder.addLines(strf("%s:|%d", "DEFAULT_OUTPUT_VERSION", DEFAULT_OUTPUT_VERSION));
        builder.addLines(strf("%s:|%s", "DEFAULT_OUTPUT_UNSYNCHRONISATION", DEFAULT_OUTPUT_UNSYNCHRONISATION));
        return builder.toString("|", 2);
    }

}
