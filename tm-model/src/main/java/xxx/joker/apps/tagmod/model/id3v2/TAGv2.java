package xxx.joker.apps.tagmod.model.id3v2;


import xxx.joker.apps.tagmod.model.id3v2.frame.ID3v2Frame;

import java.util.List;

/**
 * Created by f.barbano on 27/02/2018.
 */

//<editor-fold desc="Tag HEADER version 2">
/*
    ID3/file identifier   "ID3"
    ID3 version           $02 00
    ID3 flags             %xx000000         unsynch, compressed
    ID3 size              4 * %0xxxxxxx		without header
*/
//</editor-fold>
//<editor-fold desc="Tag HEADER version 3">
/*
    ID3v2/file identifier "ID3"
    ID3v2 version         $03 00
    ID3v2 flags           %abc00000			unsynch, extended header, experimental
    ID3v2 size            4 * %0xxxxxxx     without header
*/
//</editor-fold>
//<editor-fold desc="Tag HEADER version 4">
/*
    ID3v2/file identifier "ID3"
    ID3v2 version         $04 00
    ID3v2 flags           %abcd0000         unsynch, extended header, experimental, footer
    ID3v2 size            4 * %0xxxxxxx		without header + footer
*/
//</editor-fold>

//<editor-fold desc="Tag FOOTER version 4">
/*
    ID3v2/file identifier "3DI"
    ID3v2 version         $04 00
    ID3v2 flags           %abcd0000
    ID3v2 size            4 * %0xxxxxxx

    The footer is a copy of the header, but with different ID
*/
//</editor-fold>

//<editor-fold desc="Extended header structure">
/*
	-----------------------------------------------------------
    VERSION 3
        Extended header size 	$xx xx xx xx	-->  exclude 4 bytes of size
		Extended Flags 			$xx xx
		Size of padding 		$xx xx xx xx
		.....
    -----------------------------------------------------------
    VERSION 4
        Extended header size 	4 * %0xxxxxxx  	-->  include 4 bytes of size (whole extended header)
		Number of flag bytes 	$01
		Extended Flags 			$xx
		.....
    -----------------------------------------------------------
*/
//</editor-fold>

public interface TAGv2 {

	int getVersion();
	int getRevision();
	int getPadding();
	int getTagLength();

	boolean isUnsynch();
	boolean isCompressed();
	boolean isExtendedHeader();
	boolean isExperimental();
	boolean isFooter();

	List<ID3v2Frame> getFrameList();

}
