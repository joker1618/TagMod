package xxx.joker.apps.tagmod.console.workers;

import javafx.scene.text.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.facade.TagmodAttributes;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.facade.TagmodSign;
import xxx.joker.apps.tagmod.model.id3.enums.ID3Genre;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.model.mp3.MP3AudioInfo;
import xxx.joker.apps.tagmod.model.mp3.MP3File;
import xxx.joker.libs.core.datetime.JkTime;
import xxx.joker.libs.core.format.JkColumnFmtBuilder;
import xxx.joker.libs.core.format.JkOutputFmt;
import xxx.joker.libs.core.utils.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcChecker {

	public static final String LEFT_PAD_PREFIX = "  ";
	public static final int COLUMNS_DISTANCE = 3;
	public static final String NO_VALUE = "-";
	public static final DateTimeFormatter DEFAULT_DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


	public static String toStringCheckStatus(List<TagmodFile> tmFiles) {
		List<String> lines = new ArrayList<>();

		for (TagmodFile tmfile : tmFiles) {
			TagmodAttributes tmattr = tmfile.getTagmodAttributes();

			List<MP3Attribute> missings = JkStreams.filter(MP3Attribute.orderedValues(), a -> a.isRequired() && tmattr.getFramesData(a).isEmpty());
			String strMissing = missings.isEmpty() ? NO_VALUE : JkStreams.join(missings, ", ", MP3Attribute::name);

			TagmodSign tagmodSign = tmfile.getTagmodSign();
			String strSign;
			if (tagmodSign == null) {
				strSign = "NO";
			} else {
				strSign = tagmodSign.isValid() ? DEFAULT_DTF.format(tagmodSign.getCreationTime()) : "INVALID";
			}

			String strStatus = missings.isEmpty() && tmfile.isTagmodSignValid() ? "OK" : "WRONG";
			String strPath = tmfile.getMp3File().getFilePath().getFileName().toString();

			lines.add(strf("%s|%s|%s|%s", strPath, strStatus, strSign, strMissing));
		}

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		outb.addLines("FILE|STATUS|SIGN|MISSING ATTRIBUTES");
		outb.addLines(lines);
		String checkString = outb.toString("|", COLUMNS_DISTANCE);

		return checkString;
	}

}