package xxx.joker.apps.tagmod.model.id3v2.frame;

import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameType;

/**
 * Created by f.barbano on 27/02/2018.
 */
class ID3v2FrameImpl implements ID3v2Frame {
	
	private int version;
	private String frameId;
	private FrameName frameName;
	private FrameType frameType;
	private int size;
	private boolean unsynch;
	private TxtEncoding encoding;
	private IFrameData frameData;

	public ID3v2FrameImpl(int version, String frameId) {
		this.version = version;
		this.frameId = frameId;
		this.frameName = FrameName.getByFrameId(version, frameId);
		this.frameType = FrameType.getByFrameId(version, frameId);
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public String getFrameId() {
		return frameId;
	}

	@Override
	public FrameName getFrameName() {
		return frameName;
	}

	@Override
	public FrameType getFrameType() {
		return frameType;
	}

	@Override
	public boolean isUnsynch() {
		return unsynch;
	}

	@Override
	public TxtEncoding getEncoding() {
		return encoding;
	}

	@Override
	public <T extends IFrameData> T getFrameData() {
		return (T) frameData;
	}

	@Override
	public boolean isFrameDuplicated(ID3v2Frame frame) {
		return frameId.equals(frame.getFrameId()) && frameData.isDataDuplicated(frame.getFrameData());
	}

	@Override
	public String toString() {
		return frameId;
	}

	protected int getSize() {
		return size;
	}

	protected void setSize(int size) {
		this.size = size;
	}
	protected void setUnsynch(boolean unsynch) {
		this.unsynch = unsynch;
	}
	protected void setEncoding(TxtEncoding encoding) {
		this.encoding = encoding;
	}
	protected void setFrameData(IFrameData frameData) {
		this.frameData = frameData;
	}
}
