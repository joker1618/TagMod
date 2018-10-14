package xxx.joker.apps.tagmod.model.mp3;


import xxx.joker.apps.tagmod.model.beans.FPos;
import xxx.joker.apps.tagmod.model.id3v1.TAGv1;
import xxx.joker.apps.tagmod.model.id3v2.TAGv2;
import xxx.joker.libs.javalibs.media.analysis.JkAudioInfo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by f.barbano on 27/02/2018.
 */
class MP3FileImpl implements MP3File {

	private Path filePath;
	private List<TAGv2> tagv2List;
	private TAGv1 tagv1;
	private JkAudioInfo audioInfo;
	private FPos songDataFPos;
	private FPos dirtyBytes;


	protected MP3FileImpl() {
		this.tagv2List = new ArrayList<>();
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public JkAudioInfo getAudioInfo() {
		return audioInfo;
	}

	@Override
	public FPos getSongDataFPos() {
		return songDataFPos;
	}

	@Override
	public TAGv1 getTAGv1() {
		return tagv1;
	}

	@Override
	public List<TAGv2> getTAGv2List() {
		return tagv2List;
	}

	@Override
	public FPos getDirtyBytes() {
		return dirtyBytes;
	}


	protected void setFilePath(Path filePath) {
		this.filePath = filePath;
	}
	protected void setTAGv1(TAGv1 tagv1) {
		this.tagv1 = tagv1;
	}
	protected void setAudioInfo(JkAudioInfo audioInfo) {
		this.audioInfo = audioInfo;
	}
	protected void setSongDataFPos(FPos songDataFPos) {
		this.songDataFPos = songDataFPos;
	}
	protected void setDirtyBytes(FPos dirtyBytes) {
		this.dirtyBytes = dirtyBytes;
	}
}
