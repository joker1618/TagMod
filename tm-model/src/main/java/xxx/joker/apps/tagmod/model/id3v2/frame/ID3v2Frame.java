package xxx.joker.apps.tagmod.model.id3v2.frame;


import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.IFrameData;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameName;
import xxx.joker.apps.tagmod.model.id3v2.frame.enums.FrameType;

/**
 * Created by f.barbano on 26/02/2018.
 */

//<editor-fold desc="Frame header V2">
/*
    Frame ID    $xx xx xx
    Size        $xx xx xx       not include 6 bytes of header
*/
//</editor-fold>
//<editor-fold desc="Frame header V3">
/*
    Frame ID    $xx xx xx xx
    Size        $xx xx xx xx       not include 10 bytes of header
    Flags		$xx xx
*/
//</editor-fold>
//<editor-fold desc="Frame header V4">
/*
    Frame ID    $xx xx xx xx
    Size        4 * $0x xx xx xx      not include 10 bytes header
    Flags		$xx xx
*/
//</editor-fold>

public interface ID3v2Frame {

	int getVersion();
	String getFrameId();
	FrameName getFrameName();
	FrameType getFrameType();
	boolean isUnsynch();
	TxtEncoding getEncoding();

	<T extends IFrameData> T getFrameData();

	boolean isFrameDuplicated(ID3v2Frame frame);

}
