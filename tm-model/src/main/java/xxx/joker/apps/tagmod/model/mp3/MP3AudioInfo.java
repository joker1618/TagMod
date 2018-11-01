package xxx.joker.apps.tagmod.model.mp3;

public class MP3AudioInfo {

    private MP3FrameHeader frameHeader;
    private long durationMs;

    public MP3AudioInfo(long songByteLength, MP3FrameHeader frameHeader) {
        this.frameHeader = frameHeader;
        this.durationMs = (long)(((double) songByteLength * 8d) / frameHeader.getBitRate());
    }

    public long getDurationMs() {
        return durationMs;
    }

    public double getMpegVersion() {
        return frameHeader.getMpegVersion();
    }

    public int getMpegLayer() {
        return frameHeader.getLayer();
    }

    public int getBitRate() {
        return frameHeader.getBitRate();
    }

    public int getSamplingRate() {
        return frameHeader.getSamplingRate();
    }


}
