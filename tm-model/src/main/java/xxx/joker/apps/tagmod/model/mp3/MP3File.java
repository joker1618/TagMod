package xxx.joker.apps.tagmod.model.mp3;

import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.apps.tagmod.model.struct.FPos;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by f.barbano on 27/02/2018.
 */
public interface MP3File {

	Path getFilePath();
	long getFileSize();

    MP3AudioInfo getAudioInfo();
	FPos getSongDataFPos();

	TAGv1 getTAGv1();
	List<TAGv2> getTAGv2List();

	FPos getDirtyBytes();

}
