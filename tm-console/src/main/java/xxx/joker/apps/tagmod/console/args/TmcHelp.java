package xxx.joker.apps.tagmod.console.args;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.utils.JkStreams;
import xxx.joker.libs.javalibs.utils.JkStrings;

import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcHelp {

	public static final String HELP;
	static {
		List<String> helpLines = Arrays.asList(
            "tagmod;show;[lyrics]  files <MP3_FILES>",
            "tagmod;describe;files <MP3_FILES>",
            "tagmod;diff;<MP3_FILE_1> <MP3_FILE_2>",
            "tagmod;info;(picType|genre)",
            "tagmod;config",
          	"tagmod;export;(pics|lyrics)  files <MP3_FILES>",
			"tagmod;edit;[EDIT OPTIONS] [OUTPUT_FORMAT]  files <MP3_FILES>",
			"tagmod;test;outputFormats  [EDIT OPTIONS]  [OUTPUT_FORMAT]  files <MP3_FILES>",
//			"tagmod;delete;[DELETE OPTIONS...]  [OUTPUT_FORMAT]  files <MP3_FILES>",
//			"tagmod  add  pic  <PIC_TYPE>;<DESCRIPTION>;<PICTURE_PATH>   [OUTPUT_FORMAT]  files <MP3_FILES>",
//			"tagmod  add  lyrics  <DESCRIPTION>;<LYRICS_PATH>  [OUTPUT_FORMAT]  files <MP3_FILES>",
            "tagmod;[help]"
		);
        helpLines = new JkColumnFmtBuilder().addLines(helpLines).toLines(";", 2);
		helpLines = JkStrings.leftPadLines(helpLines, "  * ", 1);
		
		List<String> manLines = Arrays.asList(
			"OUTPUT FORMATS:",
			"  [enc ENCODING]",
            "  [ver VERSION]",
            "  [unsync]",
            "  [padding NUMBER]",
//            "  [sign|noSign]",
			"",
			"EDIT OPTIONS:",
			"  [clear]",
			"  [title  TITLE|_auto]",
			"  [artist ARTIST]",
			"  [album  ALBUM]",
			"  [year   YYYY]",
			"  [track  X[/Y]|_auto]",
			"  [genre  NUM|NAME]",
			"  [cdPos  X[/Y]]",
			"  [cover  COVER_PATH]",
			"  [lyrics LYRICS_PATH|_auto]",
//			"",
//			"DELETE OPTIONS:",
//			"  [title]  [artist]  [album]   [year]  [track]  [genre]",
//			"  [cdPos]  [cover]   [lyrics]  [pics]  [otherLyrics]",
			""
		);

        String strHelp = JkStreams.join(helpLines, StringUtils.LF);
        String strMan = JkStreams.join(manLines, StringUtils.LF).replace("_auto", TmcCmd.AUTO_VALUE);
        HELP = strf("USAGE:\n%s\n\n%s", strHelp, strMan).trim();
	}


}
