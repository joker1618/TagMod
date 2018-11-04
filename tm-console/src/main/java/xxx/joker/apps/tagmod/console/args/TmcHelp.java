package xxx.joker.apps.tagmod.console.args;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.argsparser.design.annotation.Opt;
import xxx.joker.libs.core.format.JkColumnFmtBuilder;
import xxx.joker.libs.core.utils.JkReflection;
import xxx.joker.libs.core.utils.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcHelp {

	public static final String HELP;
	public static final String ALIASES;
	static {
		List<String> helpLines = Arrays.asList(
            "tagmod;show;[attr] [t1] [t2] [audio] [tm] [size] [all]  files <MP3_FILES>",
            "tagmod;show;lyrics  files <MP3_FILES>",
            "tagmod;summary;[table]  files <MP3_FILES>",
            "tagmod;diff;<MP3_FILE_1> <MP3_FILE_2>",
            "tagmod;info;(picType|genre)",
            "tagmod;config",
          	"tagmod;export;(pics|lyrics)  files <MP3_FILES>",
			"tagmod;edit;[EDIT OPTIONS] [OUTPUT_FORMAT]  files <MP3_FILES>",
			"tagmod;test;outputFormats  [EDIT OPTIONS]  [OUTPUT_FORMAT]  files <MP3_FILES>",
			"tagmod;delete;[DELETE OPTIONS...]  files <MP3_FILES>",
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
            "  [noSign]",
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
			"",
			"DELETE OPTIONS:",
			"  [title]  [artist]  [album]   [year]  [track]  [genre]",
			"  [cdPos]  [cover]   [lyrics]  [pics]  [otherLyrics]",
			""
		);

		// Alias list
        String strHelp = JkStreams.join(helpLines, StringUtils.LF);
        String strMan = JkStreams.join(manLines, StringUtils.LF).replace("_auto", TmcCmd.AUTO_VALUE);
        HELP = strf("USAGE:\n%s\n\n%s", strHelp, strMan).trim();

        StringBuilder sb = new StringBuilder();
        getNameAndAliases().forEach((k,v) -> {
            sb.append(k);
            v.forEach(al -> sb.append(", "+al));
            sb.append("\n");
        });

        String padded = JkStrings.leftPadLines(sb.toString().trim(), "  ", 1);
        ALIASES = strf("ALIAS LIST\n%s", padded);

    }


    public static Map<String, List<String>> getNameAndAliases() {
        List<Field> fields = JkReflection.getFieldsByAnnotation(TmcArgs.class, Opt.class);
        List<Opt> list = JkStreams.map(fields, field -> field.getAnnotation(Opt.class));
        Map<String, List<String>> map = new TreeMap<>();
        list.forEach(o -> map.put(o.name(), Arrays.asList(o.aliases())));
        return map;
    }

}
