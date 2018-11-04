package xxx.joker.apps.tagmod.console.workers;

import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3.enums.PictureType;
import xxx.joker.libs.core.format.JkColumnFmtBuilder;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcInfo {

	public static String toStringID3Genres() {
		List<ID3Genre> glist = Arrays.asList(ID3Genre.values());
		int nameWidth = glist.stream().mapToInt(g -> g.getGenreName().length()).max().orElse(0);
		List<String> strList = JkStreams.map(glist, g -> strf("%3d.  %-" + nameWidth + "s", g.getGenreNum(), g.getGenreName()));
		int lineHeight = 40;
		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		for(int start = 0; start < strList.size(); start += lineHeight) {
			List<String> lines = strList.subList(start, Math.min(strList.size(), start + lineHeight));
			outb.addColumnRight(";", lines);
		}
		outb.insertPrefix(TmcViewer.LEFT_PAD_PREFIX);
		String str = outb.toString(";", TmcViewer.COLUMNS_DISTANCE);

		return "ID3 GENRE LIST\n" + str;
	}

	public static String toStringPictureTypes() {
		List<String> lines = new ArrayList<>();
		lines.add("PICTURE TYPE LIST");
		for(PictureType pt : PictureType.values()) {
			lines.add(strf("%2d.  %s", pt.pictureNumber(), pt.normalizedName()));
		}
		return JkStreams.join(lines, "\n");
	}
}
