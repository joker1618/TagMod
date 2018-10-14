package tagmod.spikes.console;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import xxx.joker.apps.tagmod.console.workers.TmcViewer;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class Tests {

	@Test
	public void test() throws IOException {
//		Path path = Paths.get("C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1989 Liberi liberi\\02 Ormai è tardi.mp3");
		Path path = Paths.get("C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1989 Liberi liberi\\04 Vivere senza te.mp3");
		TagmodFile tmfile = new TagmodFile(path);

		display(path.toAbsolutePath().toString());
//		display("\n\n\n");
		display(TmcViewer.toStringMP3Attributes(tmfile));
//		display("\n\n\n");
//		display(TmcViewer.toStringMainDetails(tmfile));
//		display("\n\n\n");
//		display(JkStreams.join(TmcViewer.toStringLyrics(tmfile), "\n-----------------------------------------\n"));
//		display("\n\n\n");
//		tmfile.getMp3File().getTAGv2List().forEach(tag -> display(TmcViewer.toStringTAGv2(tag)));
//		display("\n\n\n");
//		display(TmcViewer.toStringTAGv1(tmfile.getMp3File().getTAGv1()));
	}
	@Test
	public void aatest() throws IOException {

		String tr  = "@Opt(name = \"set\", aliases = {\"-set\"})" +
			"private Boolean set = false;";

		List<String> list = Arrays.asList("title", "artist", "album", "year", "track", "genre", "lyrics", "cover", "lyricsTranslation");
		list.forEach(l -> {
			display("@Opt(name = \"%s\", aliases = {\"-%s\"}, classes = {Boolean.class, String.class})", l, l);
			display("private Object %s;", l);
		});
		list.forEach(l -> {
			display("public boolean is%s() {\n\treturn %s instanceof Boolean && ((Boolean)%s) == Boolean.TRUE;\n}", StringUtils.capitalize(l), l, l);
			display("public String get%s() {\n\treturn %s instanceof String ? (tring)%s : null;\n}", StringUtils.capitalize(l), l, l);
		});
		list.forEach(l -> {
			display("@OptName");
			display("%s(\"%s\"),", l.toUpperCase(), l);
		});
	}
}
