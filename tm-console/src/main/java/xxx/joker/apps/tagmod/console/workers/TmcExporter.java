package xxx.joker.apps.tagmod.console.workers;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.facade.TagmodAttributes;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.libs.javalibs.utils.JkFiles;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcExporter {

	public static List<Path> exportPictures(TagmodFile tmFile) {

        TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();
        List<Picture> pics = new ArrayList<>(tmAttribs.getFramesDataCasted(MP3Attribute.PICTURES));
        Picture cover = tmAttribs.getFrameDataCasted(MP3Attribute.COVER);
		if(cover != null) {
			pics.add(0, cover);
		}

		List<Path> plist = new ArrayList<>();
		for(Picture pic : pics) {
			Path p = getPicturePath(tmFile.getMp3File().getFilePath(), pic, "");
			int counter = 0;
			while(plist.contains(p)) {
				p = getPicturePath(tmFile.getMp3File().getFilePath(), pic, (++counter)+"");
			}
			JkFiles.writeFile(p, pic.getPicData(), true);
			plist.add(p);
		}

		return plist;
	}

	public static List<Path> exportLyrics(TagmodFile tmFile) {
        TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();
        List<Lyrics> lyricsList = new ArrayList<>(tmAttribs.getFramesDataCasted(MP3Attribute.OTHER_LYRICS));
        Lyrics lyr = tmAttribs.getFrameDataCasted(MP3Attribute.LYRICS);
        if(lyr != null) {
            lyricsList.add(0, lyr);
        }


		List<Path> plist = new ArrayList<>();
		for(Lyrics lyrics : lyricsList) {
			Path p = getLyricsPath(tmFile.getMp3File().getFilePath(), lyrics, "");
			int counter = 0;
			while(plist.contains(p)) {
				p = getLyricsPath(tmFile.getMp3File().getFilePath(), lyrics, (++counter)+"");
			}
			JkFiles.writeFile(p, lyrics.getText(), true);
			plist.add(p);
		}

		return plist;
	}

	private static Path getLyricsPath(Path tmFilePath, Lyrics lyrics, String suffix) {
		String lyricsName = strf("%s.%s.%s%s.lyrics",
			JkFiles.getFileName(tmFilePath),
			lyrics.getDescription(),
			lyrics.getLanguage().getLabel().toUpperCase(),
			StringUtils.isBlank(suffix) ? "" : "."+suffix
		);

		return JkFiles.getParent(tmFilePath).resolve(lyricsName);
	}

	private static Path getPicturePath(Path tmFilePath, Picture pic, String suffix) {
		String coverName = strf("%s.%d.%s.%s%s.%s",
			JkFiles.getFileName(tmFilePath),
			pic.getPicType().pictureNumber(),
			pic.getPicType().name(),
		    pic.getDescription(),
			StringUtils.isBlank(suffix) ? "" : "."+suffix,
		    pic.getMimeType().fileExtension()
		);

		return JkFiles.getParent(tmFilePath).resolve(coverName);
	}

}
