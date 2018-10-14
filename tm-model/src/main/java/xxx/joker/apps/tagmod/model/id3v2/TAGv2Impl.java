package xxx.joker.apps.tagmod.model.id3v2;


import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;

import java.util.List;

/**
 * Created by f.barbano on 27/02/2018.
 */
class TAGv2Impl implements TAGv2 {

	private int version;
	private int revision;
	private int size;
	private int padding;

	private boolean unsynch;
	private boolean compressed;
	private boolean extendedHeader;
	private boolean experimental;
	private boolean footer;

	private int extendedHeaderLength;

	private List<ID3v2Frame> frameList;
	

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getRevision() {
		return revision;
	}

	protected int getSize() {
		return size;
	}

	@Override
	public int getTagLength() {
		int tagSize = ID3Specs.ID3v2_HEADER_LENGTH;
		tagSize += size;
		tagSize += isFooter() ? ID3Specs.ID3v2_FOOTER_LENGTH: 0;
		return tagSize;
	}

	@Override
	public int getPadding() {
		return padding;
	}

	@Override
	public boolean isUnsynch() {
		return unsynch;
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	@Override
	public boolean isExtendedHeader() {
		return extendedHeader;
	}

	@Override
	public boolean isExperimental() {
		return experimental;
	}

	@Override
	public boolean isFooter() {
		return footer;
	}

	@Override
	public List<ID3v2Frame> getFrameList() {
		return frameList;
	}

	protected void setVersion(int version) {
		this.version = version;
	}
	protected void setRevision(int revision) {
		this.revision = revision;
	}
	protected void setSize(int size) {
		this.size = size;
	}
	protected void setPadding(int padding) {
		this.padding = padding;
	}
	protected void setUnsynch(boolean unsynch) {
		this.unsynch = unsynch;
	}
	protected void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}
	protected void setExtendedHeader(boolean extendedHeader) {
		this.extendedHeader = extendedHeader;
	}
	protected void setExperimental(boolean experimental) {
		this.experimental = experimental;
	}
	protected void setFooter(boolean footer) {
		this.footer = footer;
	}
	protected void setExtendedHeaderLength(int extendedHeaderLength) {
		this.extendedHeaderLength = extendedHeaderLength;
	}
	protected void setFrameList(List<ID3v2Frame> frameList) {
		this.frameList = frameList;
	}
}
