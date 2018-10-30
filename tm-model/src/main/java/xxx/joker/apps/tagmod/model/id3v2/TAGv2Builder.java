package xxx.joker.apps.tagmod.model.id3v2;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;
import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2FrameFactory;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.util.ByteBuilder;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkBytes;

import java.util.ArrayList;
import java.util.List;

public class TAGv2Builder {

    private List<Pair<FrameName, IFrameData>> framePairs;

    public TAGv2Builder() {
        this.framePairs = new ArrayList<>();
    }

    public static byte[] toBytes(TAGv2 tag) {
        ByteBuilder bb = new ByteBuilder();
        for(ID3v2Frame frame : tag.getFrameList()) {
            bb.add(ID3v2FrameFactory.createFrameBytes(frame));
        }
        if(tag.getPadding() > 0) {
            bb.addZeroBytes(tag.getPadding());
        }
        byte[] headerBytes = createTAGv2Header(tag.getVersion(), 0, bb.length(), tag.isUnsynch());
        bb.insertFirst(headerBytes);
        return bb.build();
    }

    public TAGv2Builder addFrameData(FrameName frameName, IFrameData frameData) {
        framePairs.add(Pair.of(frameName, frameData));
        return this;
    }

    public byte[] buildBytes(int version, TxtEncoding encoding, boolean unsynchronized, Integer padding) {
        if(framePairs.isEmpty()) {
            return new byte[0];
        }

        ByteBuilder bb = new ByteBuilder();

        framePairs.forEach(p -> bb.add(ID3v2FrameFactory.createFrameBytes(version, p.getKey(), encoding, p.getValue(), unsynchronized)));

        int pad = padding == null ? computePaddingSize(bb.length()) : padding;
        bb.addZeroBytes(pad);

        byte[] headerBytes = createTAGv2Header(version, 0, bb.length(), unsynchronized);
        bb.insertFirst(headerBytes);

        return bb.build();
    }

    /**
     * ID2v2 tag size <= 1MB, padding is computed so that tag size will be a power of 2
     * ID2v2 tag size >  1MB, padding is 1618
     */
    public static int computePaddingSize(long tagSize) {
        int mb = 1024 * 1024;

        if(tagSize > mb)    return 1618;

        for(int exp = 1; exp <= 20; exp++) {
            double r = Math.pow(2, exp);
            if(tagSize <= r) {
                int pad = (int)(r - tagSize);
                return pad - 10; // Subtract header size
            }
        }

        return 0;
    }

    private static byte[] createTAGv2Header(int version, int revision, int size, boolean unsynch) {
        ByteBuilder bb = new ByteBuilder();
        bb.add(ID3Specs.ID3v2_HEADER_HEADING);
        bb.add(version);
        bb.add(revision);
        bb.add(unsynch ? JkBytes.setBit(7) : 0x00);
        bb.add(TmFormat.numberTo32BitSynchsafe(size));
        return bb.build();
    }

}
