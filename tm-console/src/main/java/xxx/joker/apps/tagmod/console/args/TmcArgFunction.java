package xxx.joker.apps.tagmod.console.args;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3SetPos;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static xxx.joker.apps.tagmod.console.common.TmcConfig.Conf;
import static xxx.joker.apps.tagmod.console.common.TmcConfig.ConfKey;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

class TmcArgFunction {

	static UnaryOperator<Object[]> expandPaths() {
		return objArr -> {
			List<Path> finalList = new ArrayList<>();
			for(Path p : (Path[]) objArr) {
				if(Files.isRegularFile(p)) {
					finalList.add(p);
				} else if(Files.isDirectory(p)) {
					finalList.addAll(JkFiles.findFiles(p, true, TmFormat::isMP3File));
				}
			}
			return JkStreams.distinctSorted(finalList).toArray(new Path[0]);
		};
	}

	static Function<Object[],String> validatePaths() {
		return objArr -> {
			if(objArr.length == 0) {
				return "No input MP3 files found";
			}

			Path[] paths = (Path[]) objArr;
			for(Path path : paths) {
				if(!TmFormat.isMP3File(path)) {
					return strf("File '%s' is not an MP3 file", path);
				}
			}
			return null;
		};
	}

	static Function<Object[],String> validateConfigEdit() {
		return objArr -> {
			if(objArr.length == 0) {
				return "No properties found";
			}

			String[] strArr = (String[]) objArr;
			Map<Boolean, List<String>> map = JkStreams.toMap(Arrays.asList(strArr), s -> s.contains("="));
			if(map.get(false) != null) {
				return strf("Wrong input property [%s]", map.get(false).get(0));
			}

			Map<String, String> inputProps = JkStreams.toMapSingle(map.get(true), l -> JkStrings.splitAllFields(l, "=", true)[0], l -> JkStrings.splitAllFields(l, "=", true)[1]);
			Map<ConfKey, Conf> allConfig = TmcConfig.getAllProperties();
			for(Map.Entry<String,String> entry : inputProps.entrySet()) {
				List<Conf> filtered = JkStreams.filter(allConfig.values(), c -> StringUtils.equalsIgnoreCase(entry.getKey(), c.getConfKey().name()));
				if(filtered.isEmpty()) {
					return strf("Property '%s' does not exists", entry.getKey());
				}
				if(JkTests.duplicatesPresents(JkStreams.map(filtered, c -> c.getConfKey().name()))) {
					return strf("Duplicated input properties found");
				}
				Conf conf = filtered.get(0);
				if(!conf.getValueCheck().test(entry.getValue())) {
					return strf("Wrong value [%s] for property '%s'", entry.getValue(), entry.getKey());
				}
			}

			return null;
		};
	}

	static Function<Object[],String> validateDistinctCoverPaths() {
		return objArr -> {
			if(objArr.length == 0) {
				return "No cover files found";
			}

			Path[] parr = (Path[]) objArr;
			List<Path> wrongs = Arrays.stream(parr).filter(p -> MimeType.getByExtension(p) == null).collect(Collectors.toList());
			if(!wrongs.isEmpty()) {
				String extAllowed = Arrays.stream(MimeType.values()).flatMap(mt -> mt.allowedExtensions().stream()).collect(Collectors.joining(", "));
				return strf("Wrong picture %s. Allowed images: %s", wrongs, extAllowed);
			}

			return null;
		};
	}

	static Function<Object[],String> validateCoverPath() {
		return objArr -> {
			if(objArr.length != 1) {
				return strf("Only one cover path expected (found %d)", objArr.length);
			}

			Path[] parr = (Path[]) objArr;
			if(MimeType.getByExtension(parr[0]) == null) {
				String extAllowed = Arrays.stream(MimeType.values()).flatMap(mt -> mt.allowedExtensions().stream()).collect(Collectors.joining(", "));
				return strf("Wrong cover %s. Allowed images: %s", parr[0], extAllowed);
			}

			return null;
		};
	}

    static final Function<Object[],String> validateYear() {
        return oarr -> {
            String syear = ((String[]) oarr)[0];
            if(!TmcCmd.AUTO_VALUE.equalsIgnoreCase(syear) && !JkTests.isInteger(syear)) {
                return strf("Invalid year %s", syear);
            }
            return null;
        };
    }

    static final Function<Object[],String> validateSetPos(boolean autoAllowed) {
        return oarr -> {
            String spos = ((String[]) oarr)[0];
            if((!autoAllowed || !TmcCmd.AUTO_VALUE.equalsIgnoreCase(spos)) && ID3SetPos.parse(spos) == null) {
                return strf("Invalid set pos %s", spos);
            }
            return null;
        };
    }

    static final Function<Object[],String> validateLyricsPath() {
        return oarr -> {
            String spath = ((String[]) oarr)[0];
            if(!TmcCmd.AUTO_VALUE.equalsIgnoreCase(spath) && !Files.isRegularFile(Paths.get(spath))) {
                return strf("Invalid lyrics path %s", spath);
            }
            return null;
        };
    }

    static final Function<Object[],String> validateGenre() {
        return oarr -> {
            String spos = ((String[]) oarr)[0];
            Integer genreNum = JkConverter.stringToInteger(spos);
            ID3Genre genre = genreNum == null ? ID3Genre.getByName(spos) : ID3Genre.getByNumber(genreNum);
            return genre == null ? strf("Invalid genre %s", spos) : null;
        };
    }
    static final UnaryOperator<Object[]> fixGenreName() {
        return oarr -> {
            String sgenre = ((String[]) oarr)[0];
            if(ID3Genre.getByName(sgenre) == null) {
                String gname = ID3Genre.getByNumber(JkConverter.stringToInteger(sgenre)).getGenreName();
                return new String[]{gname};
            } else {
                return oarr;
            }
        };
    }

    static final Function<Object[],String> validateEncoding() {
        return oarr -> {
            String[] sarr = (String[]) oarr;
            String strEnc = sarr.length > 0 ? sarr[0] : "";
            TxtEncoding enc = TxtEncoding.fromLabel(strEnc);
            return enc == null ? strf("Invalid encoding '%s'", strEnc) : null;
        };
    }

    static final Function<Object[],String> validateVersion() {
        return oarr -> {
            Integer[] iarr = (Integer[]) oarr;
            if(!ID3Specs.ID3v2_SUPPORTED_VERSIONS.contains(iarr[0])) {
                return strf("Invalid version %d", iarr[0]);
            }
            return null;
        };
    }
}
