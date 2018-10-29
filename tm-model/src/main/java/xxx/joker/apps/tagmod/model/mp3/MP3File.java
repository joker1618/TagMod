package xxx.joker.apps.tagmod.model.mp3;

import xxx.joker.apps.tagmod.model.struct.FPos;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.libs.javalibs.media.analysis.JkAudioInfo;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by f.barbano on 27/02/2018.
 */
public interface MP3File {

	Path getFilePath();
	long getFileSize();

	JkAudioInfo getAudioInfo();
	FPos getSongDataFPos();

	TAGv1 getTAGv1();
	List<TAGv2> getTAGv2List();

	FPos getDirtyBytes();

}
