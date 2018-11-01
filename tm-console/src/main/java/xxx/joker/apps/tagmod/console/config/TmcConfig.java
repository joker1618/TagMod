package xxx.joker.apps.tagmod.console.config;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.libs.javalibs.exception.JkRuntimeException;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.utils.JkConverter;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

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

//	private enum ConfKey {
//		MAX_HALF_DISPLAY_WIDTH,
//		DEFAULT_OUTPUT_ENCODING,
//		DEFAULT_OUTPUT_VERSION,
//        DEFAULT_OUTPUT_UNSYNCHRONISATION,
//	}
//
//	private static Path CONF_PATH;
//
//	private static Map<ConfKey,Conf> confMap;
//
//	static {
//        Path launcherPath = JkFiles.getLauncherPath(TmcConfig.class);
//        Path configPath;
//        if(Files.isRegularFile(launcherPath) && JkFiles.getExtension(launcherPath).equals("jar")) {
//            configPath = JkFiles.getParent(launcherPath).resolve("config/tagmod.config");
//        } else {
//            configPath = Paths.get("config/tagmod.config");
//        }
//        loadConfigPath(configPath);
//    }
//	public static void loadConfigPath(Path confPath) {
//	    CONF_PATH = confPath;
//
//        confMap = new HashMap<>();
//        initDefaultValues();
//
//        try {
//            readConfigFile(CONF_PATH);
//        } catch (Exception e) {
//            throw new JkRuntimeException(e, "Error reading configuration file %s", CONF_PATH);
//        }
//    }
//
//    public static Path getConfPath() {
//        return CONF_PATH;
//    }
//
//    private static void initDefaultValues() {
//		confMap.put(ConfKey.MAX_HALF_DISPLAY_WIDTH, new Conf(
//			ConfKey.MAX_HALF_DISPLAY_WIDTH,
//			"Max file path length for 'diff' command",
//			"85",
//			s -> JkConverter.stringToInteger(s,-1) > 0
//		));
//		confMap.put(ConfKey.DEFAULT_OUTPUT_ENCODING, new Conf(
//			ConfKey.DEFAULT_OUTPUT_ENCODING,
//			"Default output encoding. Allowed values: " + JkStreams.map(Arrays.asList(TxtEncoding.values()), TxtEncoding::getLabel).toString(),
//			"utf16",
//			s -> TxtEncoding.getFromLabel(s) != null
//		));
//		confMap.put(ConfKey.DEFAULT_OUTPUT_VERSION, new Conf(
//			ConfKey.DEFAULT_OUTPUT_VERSION,
//			"Default output version. Allowed values: " + ID3Specs.ID3v2_SUPPORTED_VERSIONS.toString(),
//			"4",
//			s -> ID3Specs.ID3v2_SUPPORTED_VERSIONS.contains(JkConverter.stringToInteger(s, -1))
//		));
//        confMap.put(ConfKey.DEFAULT_OUTPUT_UNSYNCHRONISATION, new Conf(
//            ConfKey.DEFAULT_OUTPUT_UNSYNCHRONISATION,
//            "Default output unsynchronisation",
//            "false",
//            s -> true
//        ));
//	}
//	private static void readConfigFile(Path confPath) throws IOException {
//		boolean persist = false;
//		if(!Files.exists(confPath)) {
//			persist = true;
//		} else {
//			List<String> lines = JkStreams.filter(Files.readAllLines(confPath), l -> l.contains("="));
//			Map<String, String> propMap = JkStreams.toMapSingle(lines, line -> line.split("=")[0], line -> line.split("=")[1]);
//			for(ConfKey ck : ConfKey.values()) {
//				String val = propMap.get(ck.name());
//				if(val == null || !confMap.get(ck).valueCheck.test(val)) {
//					persist = true;
//				} else {
//					confMap.get(ck).value = val;
//				}
//			}
//		}
//
//		if(persist) {
//			persistConfigurations();
//		}
//	}
//
//	public static int getMaxHalfDisplayWidth() {
//		return JkConverter.stringToInteger(confMap.get(ConfKey.MAX_HALF_DISPLAY_WIDTH).value);
//	}
//	public static TxtEncoding getDefaultOutputEncoding() {
//		return TxtEncoding.fromLabel(confMap.get(ConfKey.DEFAULT_OUTPUT_ENCODING).value);
//	}
//	public static Integer getDefaultOutputVersion() {
//		return JkConverter.stringToInteger(confMap.get(ConfKey.DEFAULT_OUTPUT_VERSION).value);
//	}
//    public static boolean getDefaultOutputUnsynchronisation() {
//        return JkConverter.stringToBoolean(confMap.get(ConfKey.DEFAULT_OUTPUT_UNSYNCHRONISATION).value, false);
//    }
//
//	public static String toStringConfigurations() {
//		return JkStreams.join(confMap.values(), "\n\n", c -> strf("###  %s ###\n%s = %s", c.description, c.confKey.name(), c.value));
//	}
//
//	private static void persistConfigurations() {
//		List<String> lines = JkStreams.map(confMap.values(), conf -> strf("%s=%s", conf.confKey.name(), conf.value));
//		JkFiles.writeFile(CONF_PATH, lines, true);
//	}
//
//	private static class Conf {
//		private ConfKey confKey;
//		private String description;
//		private String value;
//		private Predicate<String> valueCheck;
//
//		private Conf(ConfKey confKey, String description, String value, Predicate<String> valueCheck) {
//			this.confKey = confKey;
//			this.description = description;
//			this.value = value;
//			this.valueCheck = valueCheck;
//		}
//
//		public ConfKey getConfKey() {
//			return confKey;
//		}
//		public String getDescription() {
//			return description;
//		}
//		public String getValue() {
//			return value;
//		}
//		public Predicate<String> getValueCheck() {
//			return valueCheck;
//		}
//	}
}
