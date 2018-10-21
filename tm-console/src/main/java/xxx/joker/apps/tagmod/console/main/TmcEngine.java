package xxx.joker.apps.tagmod.console.main;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.tagmod.common.TagmodConfig;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.console.args.TmcHelp;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.apps.tagmod.console.workers.TmcExporter;
import xxx.joker.apps.tagmod.console.workers.TmcInfo;
import xxx.joker.apps.tagmod.console.workers.TmcViewer;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.language.JkLanguage;
import xxx.joker.libs.javalibs.language.JkLanguageDetector;
import xxx.joker.libs.javalibs.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xxx.joker.apps.tagmod.model.mp3.MP3Attribute.*;
import static xxx.joker.libs.javalibs.utils.JkConsole.display;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcEngine {
    private static final Logger logger = LoggerFactory.getLogger(TmcEngine.class);

	public static void execute(TmcArgs inputArgs)  {
		switch (inputArgs.getSelectedCommand()) {
			case CMD_SHOW:
				manageShow(inputArgs);
				break;
			case CMD_DESCRIBE:
				manageDescribe(inputArgs);
				break;
			case CMD_DIFF:
				manageDiff(inputArgs);
				break;
			case CMD_INFO:
				manageInfo(inputArgs);
				break;
			case CMD_CONFIG:
				manageConfig();
				break;
			case CMD_EXPORT:
				manageExport(inputArgs);
				break;
            case CMD_EDIT:
				manageEdit(inputArgs);
				break;

			case CMD_HELP:
				manageHelp();
				break;
			default:
				display("Not yet implemented logic for command %s", inputArgs.getSelectedCommand());
				break;
		}
	}

	private static void manageShow(TmcArgs inputArgs) {
		List<String> lines = new ArrayList<>();

		for (TagmodFile tmFile : inputArgs.getTagmodFiles()) {
			String str;
			if(inputArgs.isLyrics())	str = JkStreams.join(TmcViewer.toStringLyrics(tmFile), "\n\n");
			else 						str = TmcViewer.toStringMP3Attributes(tmFile);
			lines.add(strf("File:  %s\n%s", tmFile.getMp3File().getFilePath(), str));
		}

		display(JkStreams.join(lines, getLineSeparator(100)));
	}

	private static void manageDescribe(TmcArgs inputArgs) {
		List<String> lines = new ArrayList<>();
		for (TagmodFile tmFile : inputArgs.getTagmodFiles()) {
			lines.add(strf("File:  %s\n%s", tmFile.getMp3File().getFilePath(), TmcViewer.describe(tmFile)));
		}
		display(JkStreams.join(lines, getLineSeparator(100)));
	}

	private static void manageDiff(TmcArgs inputArgs) {
		TagmodFile[] diff = inputArgs.getDiffFiles();

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		String colSep = "#####";

		String[] sarr = new String[2];
		for(int i = 0; i < 2; i++) {
			String fn = diff[i].getMp3File().getFilePath().toString();
			if(fn.length() > TmcConfig.getMaxHalfDisplayWidth()-7) {
				String tmp = "..." + StringUtils.substring(fn, fn.length() - (TmcConfig.getMaxHalfDisplayWidth()-10));
				fn = tmp;
			}
			sarr[i] = "File:  " + fn;
		}

		List<Pair<String, String>> pairs = new ArrayList<>();
		pairs.add(Pair.of(sarr[0], sarr[1]));
		pairs.add(Pair.of(TmcViewer.toStringMainDetails(diff[0]), TmcViewer.toStringMainDetails(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringMP3Attributes(diff[0]), TmcViewer.toStringMP3Attributes(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringTAGv2(diff[0]), TmcViewer.toStringTAGv2(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringTAGv1(diff[0]), TmcViewer.toStringTAGv1(diff[1])));

		String join = JkStreams.join(pairs, "\n" + colSep + "\n", p -> JkStrings.mergeLines(p.getKey(), p.getValue(), colSep));
		outb.addLines(join);

		display(outb.toString(colSep, "  |  ", false));
	}

	private static void manageConfig() {
        display("TAGMOD CONFIGURATIONS\n\n%s", TmcConfig.toStringConfigurations());
	}

	private static void manageExport(TmcArgs inputArgs) {
        for (TagmodFile tmFile : inputArgs.getTagmodFiles()) {
            Path tmFilePath = tmFile.getMp3File().getFilePath();

            try {
                if (inputArgs.isLyrics()) {
                    List<Path> paths = TmcExporter.exportLyrics(tmFile);
                    if (paths.isEmpty()) display("%s\t--> no lyrics", tmFilePath);
                    else paths.forEach(p -> display("%s --> %s", tmFilePath, p));

                } else if (inputArgs.isPictures()) {
                    List<Path> paths = TmcExporter.exportPictures(tmFile);
                    if (paths.isEmpty()) display("%s\t--> no pictures", tmFilePath);
                    else paths.forEach(p -> display("%s --> %s", tmFilePath, p));
                }
            } catch (Exception ex) {
                logger.error("Error exporting from " + tmFilePath, ex);
            }
        }
	}

	private static void manageInfo(TmcArgs inputArgs) {
		String str = "";
		if(inputArgs.isPicType())	str = TmcInfo.toStringPictureTypes();
		if(inputArgs.isGenre())		str = TmcInfo.toStringID3Genres();
		display(str);
	}

	private static void manageEdit(TmcArgs args) {
//        TxtEncoding enc = args.getEncoding() != null ? args.getEncoding() : TmcConfig.getDefaultOutputEncoding();
//        Integer version = args.getVersion() != null ? args.getVersion() : TmcConfig.getDefaultOutputVersion();
//        boolean unsynch = false;
//        int padding = TmcConfig.getDefaultOutputPadding();
//        LocalDateTime signTime = sign ? LocalDateTime.now() : null;
//
//        for(TagmodFile tmFile : args.getTagmodFiles()) {
//
//            Path fpath = tmFile.getMp3File().getFilePath();
//
//            try {
//                if (args.isClear()) {
//                    tmFile.clearAllAttributes();
//                }
//
//                AutoAttributes autoAttributes = new AutoAttributes(fpath);
//
//                setTextAttrib(tmFile, TITLE, args.getTitle(), autoAttributes);
//                setTextAttrib(tmFile, ARTIST, args.getArtist(), null);
//                setTextAttrib(tmFile, ALBUM, args.getAlbum(), null);
//                setTextAttrib(tmFile, YEAR, args.getYear(), null);
//                setTextAttrib(tmFile, TRACK, args.getTrack(), autoAttributes);
//                setTextAttrib(tmFile, GENRE, args.getGenre(), null);
//                setTextAttrib(tmFile, CD_POS, args.getCdPos(), null);
//
//                if (args.getCover() != null) {
//                    MimeType mt = MimeType.getByExtension(args.getCover());
//                    byte[] picData = JkBytes.getBytes(args.getCover());
//                    Picture cover = new Picture(mt, TagmodConfig.COVER_TYPE, TagmodConfig.COVER_DESCR, picData);
//                    tmFile.setCover(cover);
//                }
//
//                if (args.getLyrics() != null) {
//                    Path lpath;
//                    if (TmcCmd.AUTO_VALUE.equalsIgnoreCase(args.getLyrics())) {
//                        String slyr = autoAttributes.getAutoValue(LYRICS);
//                        lpath = slyr == null ? null : Paths.get(slyr);
//                    } else {
//                        lpath = Paths.get(args.getLyrics());
//                    }
//
//                    if (lpath != null && Files.exists(lpath)) {
//                        String lyrics = JkStreams.join(Files.readAllLines(lpath), "\n");
//                        JkLanguage lan = JkLanguageDetector.detectLanguage(lyrics);
//                        Lyrics l = new Lyrics(lan, TagmodConfig.LYRICS_DESCR, lyrics);
//                        tmFile.setLyrics(l);
//                    }
//                }
//
//                byte[] editBytes = tmFile.toBytes(version, enc, unsynch, padding, signTime);
//                JkFiles.writeFile(fpath, editBytes, true);
//                display("File %s edit complete", fpath);
//
//            } catch (Exception ex) {
//                logger.error("Error editing " + fpath, ex);
//            }
//        }
    }
    private static void setTextAttrib(TagmodFile tmFile, MP3Attribute attr, String inputValue, AutoAttributes autoAttributes) {
        if(inputValue != null) {
            String value;
            if(autoAttributes == null || !TmcCmd.AUTO_VALUE.equalsIgnoreCase(inputValue)) {
                value = inputValue;
            } else {
                value = autoAttributes.getAutoValue(attr);
            }

            if(value != null) {
                tmFile.setTextInfoAttribute(attr, value);
            }
        }
    }

	private static void manageHelp() {
		display("USAGE:\n%s", TmcHelp.HELP);
	}

	private static String getLineSeparator(int length) {
		return strf("\n%s\n", StringUtils.repeat("-", length));
	}

	private static class AutoAttributes {
	    private static final Logger logger = LoggerFactory.getLogger(AutoAttributes.class);

	    static Map<Path,Integer> totMap = new HashMap<>();
	    static Map<Path,List<Path>> lyricsMap = new HashMap<>();

	    final Path mp3Path;
	    Map<MP3Attribute,String> autoValues;

        public AutoAttributes(Path mp3Path) {
            this.mp3Path = mp3Path;
        }

        private void init() {
            autoValues = new HashMap<>();
            autoValues.put(TITLE, null);
            autoValues.put(TRACK, null);
            autoValues.put(LYRICS, null);

            String strfn = JkFiles.getFileName(mp3Path);
            int idx = strfn.indexOf(' ');
            if(idx == -1) {
                autoValues.put(TITLE, strfn);
            } else {
                Integer trackNum = JkConverter.stringToInteger(strfn.substring(0, idx));
                if(trackNum == null) {
                    autoValues.put(TITLE, strfn);
                } else {
                    String strTrack;
                    try {
                        strTrack = strf("%d/%d", trackNum, getTotTrack());
                    } catch (Exception e) {
                        strTrack = strf("%d", trackNum);
                    }
                    autoValues.put(TITLE, strfn.substring(idx+1).trim());
                    autoValues.put(TRACK, strTrack);
                }
            }

            try {
                autoValues.put(LYRICS, getLyricsPath());
            } catch (Exception e) {
                logger.warn("Error finding lyrics path for {}", mp3Path);
            }
        }

        private int getTotTrack() {
            Path folder = JkFiles.getParent(mp3Path);
            Integer tot = totMap.get(folder);
            if(tot == null) {
                tot = JkFiles.findFiles(folder, false, TmFormat::isMP3File).size();
                totMap.put(folder, tot);
            }
            return tot;
        }

        private String getLyricsPath()  {
            Path folder = JkFiles.getParent(mp3Path);
            List<Path> lpaths = lyricsMap.get(folder);
            if(lpaths == null) {
                lpaths = JkFiles.findFiles(folder, false, TmFormat::isMP3Lyrics);
                lyricsMap.put(folder, lpaths);
            }

            String strMp3Path = mp3Path.getFileName().toString();
            for(Path p : lpaths) {
                String lfn = JkFiles.getFileName(p);
                if(StringUtils.startsWithIgnoreCase(strMp3Path, lfn)) {
                    return p.toString();
                }
            }

            return null;
        }

        public String getAutoValue(MP3Attribute attr) {
            if(autoValues == null) {
                init();
            }

            return autoValues.get(attr);
        }
    }
}
