package xxx.joker.apps.tagmod.console.workers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.facade.TagmodAttributes;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Picture;
import xxx.joker.apps.tagmod.model.mp3.MP3Attribute;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.core.utils.JkEncryption;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class TmcExporter {

	public static int exportPictures(List<TagmodFile> tmFiles) {
		Map<Path, Map<Path, Picture>> coverParentMap = new HashMap<>();
		Map<Path, Picture> picsMap = new HashMap<>();

		for(TagmodFile tmFile : tmFiles) {
			TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();

			Path fpath = tmFile.getMp3File().getFilePath();
			List<Picture> pics = new ArrayList<>(tmAttribs.getFramesDataCasted(MP3Attribute.PICTURES));
			pics.forEach(pic -> picsMap.put(getPicturePath(fpath, pic), pic));
			Picture cover = tmAttribs.getFrameData(MP3Attribute.COVER);

			if(cover != null) {
				Path parent = JkFiles.getParent(fpath);
				coverParentMap.putIfAbsent(parent, new HashMap<>());
				coverParentMap.get(parent).put(fpath, cover);
			}
		}

		Map<Path, Picture> coversMap = new HashMap<>();
		for(Path parent : coverParentMap.keySet()) {
			int totMp3 = JkFiles.findFiles(parent, false, TmFormat::isMP3File).size();
			Map<Path, Picture> folderCovers = coverParentMap.get(parent);
			boolean groupCovers = false;
			if(totMp3 == folderCovers.size()) {
				long numDistinct = folderCovers.values().stream().map(pic -> JkEncryption.getMD5(pic.getPicData())).distinct().count();
				groupCovers = numDistinct == 1;
			}
			for(Path fpath : folderCovers.keySet()) {
				Picture cover = folderCovers.get(fpath);
				if(groupCovers) {
					Path cpath = getAlbumCoverPath(fpath, cover);
					coversMap.put(cpath, cover);
					break;
				} else {
					Path cpath = getPicturePath(fpath, cover);
					coversMap.put(cpath, cover);
				}
			}
		}

		picsMap.forEach((k,v) -> JkFiles.writeFile(k, v.getPicData(), true));
		coversMap.forEach((k,v) -> JkFiles.writeFile(k, v.getPicData(), true));

		return picsMap.size() + coversMap.size();
	}

	public static int exportLyrics(TagmodFile tmFile) {
        TagmodAttributes tmAttribs = tmFile.getTagmodAttributes();

        List<Lyrics> lyricsList = new ArrayList<>(tmAttribs.getFramesDataCasted(MP3Attribute.OTHER_LYRICS));
        Lyrics lyr = tmAttribs.getFrameData(MP3Attribute.LYRICS);
        if(lyr != null) {
            lyricsList.add(0, lyr);
        }

        int num = 0;
		for(Lyrics lyrics : lyricsList) {
			Path p = getLyricsPath(tmFile.getMp3File().getFilePath(), lyrics);
			JkFiles.writeFile(p, lyrics.getText(), true);
			num++;
		}

		return num;
	}

	private static Path getLyricsPath(Path tmFilePath, Lyrics lyrics) {
		String lyricsName = strf("%s.%s.%s.lyrics",
			JkFiles.getFileName(tmFilePath),
			lyrics.getDescription(),
			lyrics.getLanguage().getLabel().toUpperCase()
		);

		return JkFiles.getParent(tmFilePath).resolve(lyricsName);
	}

	private static Path getPicturePath(Path tmFilePath, Picture pic) {
		String coverName = strf("%s.%d.%s.%s.%s",
			JkFiles.getFileName(tmFilePath),
			pic.getPicType().pictureNumber(),
			pic.getPicType().name(),
		    pic.getDescription(),
		    pic.getMimeType().fileExtension()
		);

		return JkFiles.getParent(tmFilePath).resolve(coverName);
	}
	private static Path getAlbumCoverPath(Path tmFilePath, Picture pic) {
		String coverName = strf("AlbumCover.%s", pic.getMimeType().fileExtension());
		return JkFiles.getParent(tmFilePath).resolve(coverName);
	}

}
