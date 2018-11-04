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
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;
import xxx.joker.libs.core.utils.JkStrings;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcSummary {

	public static final String LEFT_PAD_PREFIX = "  ";
	public static final int COLUMNS_DISTANCE = 3;
	public static final String NO_VALUE = "-";
	public static final DateTimeFormatter DEFAULT_DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


	public static String toStringSummaryAttributes(List<TagmodFile> tmFiles) {
        Map<MP3Attribute, Set<String>> mapAttr = new HashMap<>();
        Map<Boolean, Integer> mapSign = new HashMap<>();
        long sumLen = 0L;
        long sumSize = 0L;

        for(TagmodFile tmFile : tmFiles) {
            sumSize += tmFile.getMp3File().getFileSize();
            sumLen += tmFile.getMp3File().getAudioInfo().getDurationMs();

            Boolean signStatus = tmFile.getTagmodSign() == null ? null : tmFile.getTagmodSign().isValid();
            mapSign.put(signStatus, mapSign.getOrDefault(signStatus, 0) + 1);

            TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();
            for(MP3Attribute attr : MP3Attribute.orderedValues()) {
                mapAttr.putIfAbsent(attr, new TreeSet<>());
                if(attr.isMultiValue()) {
                    List<IFrameData> framesData = tmAttribs.getFramesData(attr);
                    framesData.forEach(frameData -> mapAttr.get(attr).add(frameData.toStringInline()));
                } else {
                    IFrameData frameData = tmAttribs.getFrameData(attr);
                    mapAttr.get(attr).add(strf("%s", frameData == null ? NO_VALUE : frameData.toStringInline()));
                }
            }
        }

        List<String> strSign = new ArrayList<>();
        if(mapSign.containsKey(true))   strSign.add(strf("%d (VALID)", mapSign.get(true)));
        if(mapSign.containsKey(false))  strSign.add(strf("%d (INVALID)", mapSign.get(false)));
        if(mapSign.containsKey(null))   strSign.add(strf("%d (NO)", mapSign.get(null)));

        List<String> lines = new ArrayList<>();
        lines.add(strf("NUM FILES:;%d", tmFiles.size()));
        lines.add(strf("TOT SIZE:;%s", JkOutputFmt.humanSize(sumSize)));
        lines.add(strf("TOT LEN:;%s", JkTime.of(sumLen).toStringElapsed(false)));
        lines.add(strf("SIGNED:;%s", JkStreams.join(strSign, ", ")));
        lines.add("");

        for(MP3Attribute attr : MP3Attribute.orderedValues()) {
            String[] values = mapAttr.getOrDefault(attr, Collections.emptySet()).toArray(new String[0]);
            String val = values.length == 0 ? NO_VALUE : values.length == 1 ? values[0] : strf("<%d values>", values.length);
            lines.add(strf("%s:;%s", attr, val));
		}

		JkColumnFmtBuilder outb = new JkColumnFmtBuilder();
		outb.addLines(lines);
		outb.insertPrefix(LEFT_PAD_PREFIX);
		String summaryString = outb.toString(";", COLUMNS_DISTANCE);

		return "SUMMARY\n" + summaryString;
	}

	public static String toStringSummaryTable(List<TagmodFile> tmFiles) {
        JkColumnFmtBuilder b = new JkColumnFmtBuilder();
        b.addLines(strf("FILE|%s|LENGTH|SIGNED", JkStreams.join(MP3Attribute.orderedValues(), "|", MP3Attribute::name)));

        for(TagmodFile tmFile : tmFiles) {
            List<String> row = new ArrayList<>();
            row.add(tmFile.getMp3File().getFilePath().getFileName().toString());
            TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();
            for(MP3Attribute attr : MP3Attribute.orderedValues()) {
                if(attr.isMultiValue()) {
                    List<IFrameData> framesData = tmAttribs.getFramesData(attr);
                    row.add(framesData.isEmpty() ? NO_VALUE : framesData.size()+"");
                } else {
                    IFrameData frameData = tmAttribs.getFrameData(attr);
                    if(Arrays.asList(MP3Attribute.COVER, MP3Attribute.LYRICS).contains(attr)) {
                        row.add(frameData == null ? "N" : "Y");
                    } else {
                        row.add(frameData == null ? NO_VALUE : frameData.toStringInline().replaceAll("^\\*", "").replaceAll("\\*$", ""));
                    }
                }
            }
            row.add(JkTime.of(tmFile.getMp3File().getAudioInfo().getDurationMs()).toStringElapsed(false));
            row.add(!tmFile.isTagmodSignValid() ? "NO" : tmFile.getTagmodSign().isValid() ? "VALID" : "INVALID");
            b.addLines(JkStreams.join(row, "|"));
        }

		return "SUMMARY TABLE\n\n" + b.toString("|", 2);
	}


}
