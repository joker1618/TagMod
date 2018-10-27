package xxx.joker.apps.tagmod.model.id3v2;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3.standard.ID3Specs;
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

    public TAGv2Builder addFrameData(FrameName frameName, IFrameData frameData) {
        framePairs.add(Pair.of(frameName, frameData));
        return this;
    }

    public byte[] buildBytes(int version, TxtEncoding encoding, boolean unsynchronized, int padding) {
        if(framePairs.isEmpty()) {
            return new byte[0];
        }

        ByteBuilder bb = new ByteBuilder();

        framePairs.forEach(p -> bb.add(ID3v2FrameFactory.createFrameBytes(version, p.getKey(), encoding, p.getValue(), unsynchronized)));

        if(padding > 0) {
            bb.addZeroBytes(padding);
        }

        byte[] headerBytes = createTAGv2Header(version, 0, bb.length(), unsynchronized);
        bb.insertFirst(headerBytes);

        return bb.build();
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
