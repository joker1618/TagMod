package xxx.joker.apps.tagmod.console.common;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
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

	public enum ConfKey {
		MAX_FILENAME_DISPLAY_WIDTH,
		DEFAULT_OUTPUT_ENCODING,
		DEFAULT_OUTPUT_VERSION,
        DEFAULT_OUTPUT_SIGNED,
        DEFAULT_OUTPUT_PADDING,
		;
		public static ConfKey getByName(String name) {
			List<ConfKey> filter = JkStreams.filter(Arrays.asList(values()), n -> StringUtils.equalsIgnoreCase(n.name(), name));
			return filter.isEmpty() ? null : filter.get(0);
		}
	}

	private static Path CONF_PATH;

	private static Map<ConfKey,Conf> confMap;

	static {
	    loadConfigPath(Paths.get("config/tagmod.config"));
    }
	public static void loadConfigPath(Path confPath) {
	    CONF_PATH = confPath;

        confMap = new HashMap<>();
        initDefaultValues();

        try {
            readConfigFile(CONF_PATH);
        } catch (Exception e) {
            throw new RuntimeException("Error reading configuration file " + CONF_PATH, e);
        }
    }

	private static void initDefaultValues() {
		confMap.put(ConfKey.MAX_FILENAME_DISPLAY_WIDTH, new Conf(
			ConfKey.MAX_FILENAME_DISPLAY_WIDTH,
			"Max file path length for 'diff' command",
			"85",
			s -> JkConverter.stringToInteger(s,-1) > 0
		));
		confMap.put(ConfKey.DEFAULT_OUTPUT_ENCODING, new Conf(
			ConfKey.DEFAULT_OUTPUT_ENCODING,
			"Default output encoding. Allowed values: " + JkStreams.map(Arrays.asList(TxtEncoding.values()), TxtEncoding::getLabel).toString(),
			"iso",
			s -> TxtEncoding.getFromLabel(s) != null
		));
		confMap.put(ConfKey.DEFAULT_OUTPUT_VERSION, new Conf(
			ConfKey.DEFAULT_OUTPUT_VERSION,
			"Default output version. Allowed values: " + ID3Specs.ID3v2_SUPPORTED_VERSIONS.toString(),
			"4",
			s -> ID3Specs.ID3v2_SUPPORTED_VERSIONS.contains(JkConverter.stringToInteger(s, -1))
		));
		confMap.put(ConfKey.DEFAULT_OUTPUT_SIGNED, new Conf(
			ConfKey.DEFAULT_OUTPUT_SIGNED,
			"Sign mp3 files",
			"true"
		));
        confMap.put(ConfKey.DEFAULT_OUTPUT_PADDING, new Conf(
            ConfKey.DEFAULT_OUTPUT_PADDING,
            "Default output padding",
            "0",
            s -> JkConverter.stringToInteger(s, -1) >= 0
        ));
	}
	private static void readConfigFile(Path confPath) throws IOException {
		boolean persist = false;
		if(!Files.exists(confPath)) {
			persist = true;
		} else {
			List<String> lines = JkStreams.filter(Files.readAllLines(confPath), l -> l.contains("="));
			Map<String, String> propMap = JkStreams.toMapSingle(lines, line -> line.split("=")[0], line -> line.split("=")[1]);
			for(ConfKey ck : ConfKey.values()) {
				String val = propMap.get(ck.name());
				if(val == null || !confMap.get(ck).valueCheck.test(val)) {
					persist = true;
				} else {
					confMap.get(ck).value = val;
				}
			}
		}

		if(persist) {
			persistConfigurations();
		}

	}

	public static int getMaxFilenameWidth() {
		return JkConverter.stringToInteger(confMap.get(ConfKey.MAX_FILENAME_DISPLAY_WIDTH).value);
	}
	public static TxtEncoding getDefaultOutputEncoding() {
		return TxtEncoding.fromLabel(confMap.get(ConfKey.DEFAULT_OUTPUT_ENCODING).value);
	}
	public static Integer getDefaultOutputVersion() {
		return JkConverter.stringToInteger(confMap.get(ConfKey.DEFAULT_OUTPUT_VERSION).value);
	}
	public static boolean isDefaultOutputSign() {
		return Boolean.parseBoolean(confMap.get(ConfKey.DEFAULT_OUTPUT_SIGNED).value);
	}
    public static Integer getDefaultOutputPadding() {
        return JkConverter.stringToInteger(confMap.get(ConfKey.DEFAULT_OUTPUT_PADDING).value);
    }

	public static void setProperty(String confKeyName, String value) {
		confMap.get(ConfKey.getByName(confKeyName)).value = value;

		try {
			persistConfigurations();
		} catch (IOException e) {
			throw new RuntimeException("Error persisting configuration file " + CONF_PATH);
		}
	}

	public static String toStringConfigurations() {
		return JkStreams.join(confMap.values(), "\n\n", c -> strf("###  %s ###\n%s = %s", c.description, c.confKey.name(), c.value));
	}

	public static Map<ConfKey,Conf> getAllProperties() {
		return confMap;
	}

	public static void persistConfigurations() throws IOException {
		List<String> lines = JkStreams.map(confMap.values(), conf -> strf("%s=%s", conf.confKey.name(), conf.value));
		JkFiles.writeFile(CONF_PATH, lines, true);
	}

	public static class Conf {
		private ConfKey confKey;
		private String description;
		private String value;
		private Predicate<String> valueCheck;

		private Conf(ConfKey confKey, String description, String value) {
			this(confKey, description, value, s -> true);
		}
		private Conf(ConfKey confKey, String description, String value, Predicate<String> valueCheck) {
			this.confKey = confKey;
			this.description = description;
			this.value = value;
			this.valueCheck = valueCheck;
		}

		public ConfKey getConfKey() {
			return confKey;
		}
		public String getDescription() {
			return description;
		}
		public String getValue() {
			return value;
		}
		public Predicate<String> getValueCheck() {
			return valueCheck;
		}
	}
}
