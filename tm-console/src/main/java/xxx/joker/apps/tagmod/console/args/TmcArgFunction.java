package xxx.joker.apps.tagmod.console.args;

import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3SetPos;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkConverter;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
            Integer year = JkConverter.stringToInteger(syear);
            if(year == null || year < 0) {
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
            ID3Genre genre = ID3Genre.getByName(sgenre);
            if(genre == null) {
                genre = ID3Genre.getByNumber(JkConverter.stringToInteger(sgenre));
            }
            return new String[]{genre == null ? sgenre : genre.getGenreName()};
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
