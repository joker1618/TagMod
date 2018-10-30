package xxx.joker.apps.tagmod.console.main;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.tagmod.common.TagmodConst;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.console.args.TmcHelp;
import xxx.joker.apps.tagmod.console.config.TmcConfig;
import xxx.joker.apps.tagmod.console.workers.TmcEditor;
import xxx.joker.apps.tagmod.console.workers.TmcExporter;
import xxx.joker.apps.tagmod.console.workers.TmcInfo;
import xxx.joker.apps.tagmod.console.workers.TmcViewer;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.MimeType;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3SetPos;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.libs.javalibs.exception.JkRuntimeException;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.language.JkLanguage;
import xxx.joker.libs.javalibs.language.JkLanguageDetector;
import xxx.joker.libs.javalibs.utils.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcEngine {
    private static final Logger logger = LoggerFactory.getLogger(TmcEngine.class);

	public static void execute(TmcArgs inputArgs)  {
		switch (inputArgs.getSelectedCommand()) {
			case CMD_SHOW:
				manageShow(inputArgs);
				break;
            case CMD_SHOW_LYRICS:
				manageShowLyrics(inputArgs);
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
            case CMD_TEST_OUTPUT_FORMATS:
				manageTestOutputFormats(inputArgs);
				break;
			case CMD_HELP:
				manageHelp();
				break;
			default:
				display("Not yet implemented logic for command %s", inputArgs.getSelectedCommand());
				break;
		}
	}

    private static void manageShowLyrics(TmcArgs inputArgs) {
		List<String> lines = new ArrayList<>();

		for (TagmodFile tmFile : inputArgs.getTagmodFiles()) {
            List<String> lyrList = TmcViewer.toStringLyrics(tmFile);
            String str = lyrList.isEmpty() ? "No lyrics" : JkStreams.join(lyrList, "\n\n");
			lines.add(strf("FILE:  %s\n%s", tmFile.getMp3File().getFilePath(), str));
		}

		display(JkStreams.join(lines, getLineSeparator(100)));
	}

    private static void manageShow(TmcArgs inputArgs) {
		List<String> lines = new ArrayList<>();

		for (TagmodFile tmFile : inputArgs.getTagmodFiles()) {
            List<String> tlines = new ArrayList<>();
            if(inputArgs.isTagmod() || inputArgs.isAll())       tlines.add(TmcViewer.toStringTagmodDetails(tmFile));
            if(inputArgs.isAudio() || inputArgs.isAll())        tlines.add(TmcViewer.toStringAudioDetails(tmFile));
            if(inputArgs.isAttribute() || inputArgs.isAll())    tlines.add(TmcViewer.toStringMP3Attributes(tmFile));
            if(inputArgs.isTAGv2() || inputArgs.isAll())        tlines.add(TmcViewer.toStringTAGv2(tmFile));
            if(inputArgs.isTAGv1() || inputArgs.isAll())        tlines.add(TmcViewer.toStringTAGv1(tmFile));
            if(inputArgs.isSize() || inputArgs.isAll())         tlines.add(TmcViewer.toStringSizeDetails(tmFile));
            // Default
            if(tlines.isEmpty())       tlines.add(TmcViewer.toStringMP3Attributes(tmFile));

			lines.add(strf("FILE:  %s\n%s", tmFile.getMp3File().getFilePath(), JkStreams.join(tlines, StringUtils.LF)));
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
			sarr[i] = "FILE:  " + fn;
		}

		List<Pair<String, String>> pairs = new ArrayList<>();
		pairs.add(Pair.of(sarr[0], sarr[1]));
		pairs.add(Pair.of(TmcViewer.toStringAudioDetails(diff[0]), TmcViewer.toStringAudioDetails(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringSizeDetails(diff[0]), TmcViewer.toStringSizeDetails(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringMP3Attributes(diff[0]), TmcViewer.toStringMP3Attributes(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringTAGv2(diff[0]), TmcViewer.toStringTAGv2(diff[1])));
		pairs.add(Pair.of(TmcViewer.toStringTAGv1(diff[0]), TmcViewer.toStringTAGv1(diff[1])));

		String join = JkStreams.join(pairs, "\n" + colSep + "\n", p -> JkStrings.mergeLines(p.getKey(), p.getValue(), colSep));
		outb.addLines(join);

		display(outb.toString(colSep, "  |  ", false));
	}

	private static void manageConfig() {
        display("TAGMOD CONFIGURATIONS  (%s)\n\n%s", TmcConfig.getConfPath().toAbsolutePath().normalize(), TmcConfig.toStringConfigurations());
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

	private static void manageTestOutputFormats(TmcArgs args) {
        TmcEditor editor = createEditor(args);

        int padding = args.getPadding() == null ? TmcConfig.getDefaultOutputPadding() : args.getPadding();
        Path outp = null;
        int counter = 1;

        List<TagmodFile> tagmodFiles = args.getTagmodFiles();
        for(int i = 0; i < tagmodFiles.size(); i++) {
            Path filePath = tagmodFiles.get(i).getMp3File().getFilePath();
            try {
                TxtEncoding[] encs = args.getEncoding() != null ? new TxtEncoding[]{args.getEncoding()} : TxtEncoding.values();
                for(TxtEncoding enc : encs) {
                    List<Integer> vers = args.getVersion() == null ? ID3Specs.ID3v2_SUPPORTED_VERSIONS : Arrays.asList(args.getVersion());
                    for(int ver : vers) {
                        for(boolean unsync : Arrays.asList(true, false)) {
                            String fn = strf("%s-v%d-%s%s.%s",
                                    JkFiles.getFileName(filePath),
                                    ver, enc.getLabel(),
                                    unsync ? "-unsync" : "",
                                    JkFiles.getExtension(filePath)
                            );
                            outp = JkFiles.getParent(filePath).resolve(fn);
                            JkFiles.copyFile(filePath, outp, true, true);

                            TagmodFile newTmFile = new TagmodFile(outp);
                            boolean changed = editor.editTagmodFile(newTmFile, ver, enc, unsync, padding);
                            display("%d\tFile %s %s", counter++, outp, changed?"modified":"not changed");
                        }
                    }
                }

            } catch (Exception ex) {
                display("%d\tERROR editing file %s", counter++, outp);
                logger.error("ERROR editing file " + filePath, ex);
            }
        }
    }

	private static void manageEdit(TmcArgs args) {
        TmcEditor editor = createEditor(args);

        int counter = 1;
        List<TagmodFile> tagmodFiles = args.getTagmodFiles();
        for(int i = 0; i < tagmodFiles.size(); i++) {
            TagmodFile tmFile = tagmodFiles.get(i);
            try {
                boolean changed = editor.editTagmodFile(tmFile, args.getVersion(), args.getEncoding(), args.getUnsynchronized(), args.getPadding());
                display("%d\tFile %s %s", counter++, tmFile.getMp3File().getFilePath(), changed?"modified":"not changed");
            } catch (Exception ex) {
                display("%d\tERROR editing file %s", counter++, tmFile.getMp3File().getFilePath());
                logger.error("ERROR editing file " + tmFile.getMp3File().getFilePath(), ex);
            }
        }
    }

    private static TmcEditor createEditor(TmcArgs args) {
        TmcEditor editor = new TmcEditor();

        editor.setClear(args.isClear());
        editor.setNoSign(args.isNoSign());

        if(TmcCmd.AUTO_VALUE.equals(args.getTitle())) {
            editor.setAutoTiTle(true);
        } else {
            editor.setTitle(args.getTitle());
        }

        editor.setArtist(args.getArtist());
        editor.setAlbum(args.getAlbum());
        editor.setYear(JkConverter.stringToInteger(args.getYear()));

        if(TmcCmd.AUTO_VALUE.equals(args.getTrack())) {
            editor.setAutoTrack(true);
        } else {
            editor.setTrack(ID3SetPos.parse(args.getTrack()));
        }

        ID3Genre genre = ID3Genre.getByName(args.getGenre());
        genre = genre == null ? ID3Genre.getByNumber(JkConverter.stringToInteger(args.getGenre())) : genre;
        editor.setGenre(genre);

        editor.setCdPos(ID3SetPos.parse(args.getCdPos()));

        try {
            if (args.getCover() != null) {
                MimeType mt = MimeType.getByExtension(args.getCover());
                byte[] picData = JkBytes.getBytes(args.getCover());
                Picture cover = new Picture(mt, TagmodConst.COVER_TYPE, TagmodConst.COVER_DESCR, picData);
                editor.setCover(cover);
            }

        } catch (Exception ex) {
            logger.error("ERROR parsing cover", ex);
            throw new JkRuntimeException(ex, "ERROR parsing cover %s", args.getCover());
        }

        try {
            if(TmcCmd.AUTO_VALUE.equals(args.getLyrics())) {
                editor.setAutoLyrics(true);
            } else if(StringUtils.isNotBlank(args.getLyrics())) {
                Path lpath = Paths.get(args.getLyrics());
                if (Files.exists(lpath)) {
                    String lyrics = JkStreams.join(Files.readAllLines(lpath), "\n");
                    JkLanguage lan = JkLanguageDetector.detectLanguage(lyrics);
                    Lyrics l = new Lyrics(lan, TagmodConst.LYRICS_DESCR, lyrics);
                    editor.setLyrics(l);
                }
            }

        } catch (Exception ex) {
            logger.error("ERROR parsing lyrics", ex);
            throw new JkRuntimeException(ex, "ERROR parsing lyrics %s", args.getLyrics());
        }

        return editor;
    }

	private static void manageHelp() {
		display("%s", TmcHelp.HELP);
	}

	private static String getLineSeparator(int length) {
		return strf("\n%s\n", StringUtils.repeat("-", length));
	}

}
