package xxx.joker.apps.tagmod.model.mp3;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f.barbano on 27/12/2017.
 */

//<editor-fold desc="MP3_EXT frame header structure">
//	4 bytes of header
//	AAAAAAAA AAABBCCD EEEEFFGH IIJJKLMM
//</editor-fold>

class MP3FrameHeader {

	// 11111111 111 				// AAAAAAAA AAA
	private double mpegVersion;    	// BB
	private int layer;       		// CC
	private boolean crcProtection; 	// D
	private int bitRate;        	// EEEE
	private int samplingRate;		// FF
	private boolean padding;		// G,  if set there is one extra byte at end of frame
	private boolean privateBit;		// H
	private int channelMode;		// II
	private int modeExtension;		// JJ, only used in 'Joint stereo' channel mode (01)
	private boolean copyright;		// K
	private boolean original;		// L
	private int emphasis;			// MM

	// Values allowed
	private static final int[] MPEG_VERSION = {0, 2, 3};   		// 2.5, 2, 1
	private static final int[] LAYER_DESCRIPTION = {1, 2, 3};   // 3, 2, 1
	private static final Integer[][] BIT_RATE = new Integer[][] {
		new Integer[] {null, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448, null},  // v1 l1
		new Integer[] {null, 32, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, null},     // v1 l2
		new Integer[] {null, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, null},      // v1 l3
		new Integer[] {null, 32, 48, 56, 64, 80, 96, 112, 128, 144, 160, 176, 192, 224, 256, null},     // v2 l1
		new Integer[] {null, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, null}           // v2 (l2 l3)
	};
	private static final Integer[][] SAMPLING_RATE = new Integer[][] {
		new Integer[] {44100, 48000, 32000},  // v1
		new Integer[] {22050, 24000, 16000},  // v2
		new Integer[] {11025, 12000, 8000}    // v2.5
	};


	private MP3FrameHeader(byte[] data) {
		// BYTE 2 - AAABBCCD
		int BB = getNumberFromBitRange(data[1], 3, 5);
		mpegVersion = BB == 0 ? 2.5 : (BB == 2 ? 2 : 1);

		int CC = getNumberFromBitRange(data[1], 5, 7);
		layer = 4 - CC;

		int D = getNumberFromBitRange(data[1], 7, 8);
		crcProtection = D == 0 ? true : false;

		// BYTE 1 - EEEEFFGH
		int EEEE = getNumberFromBitRange(data[2], 0, 4);
		int matrixIndex = getBitRateMatrixIndex();
		bitRate = BIT_RATE[matrixIndex][EEEE];

		int FF = getNumberFromBitRange(data[2], 4, 6);
		matrixIndex = getSamplingRateMatrixIndex();
		samplingRate = SAMPLING_RATE[matrixIndex][FF];

		int G = getNumberFromBitRange(data[2], 6, 7);
		padding = G == 0 ? false : true;

		int H = getNumberFromBitRange(data[2], 7, 8);
		privateBit = H == 0 ? false : true;

		// BYTE 0 - IIJJKLMM
		int II = getNumberFromBitRange(data[3], 0, 2);
		channelMode = II;

		int JJ = getNumberFromBitRange(data[3], 2, 4);
		modeExtension = JJ;

		int K = getNumberFromBitRange(data[3], 4, 5);
		copyright = K == 0 ? false : true;

		int L = getNumberFromBitRange(data[3], 5, 6);
		original = L == 0 ? false : true;

		int MM = getNumberFromBitRange(data[3], 6, 8);
		emphasis = MM;
	}

	static MP3FrameHeader parseMP3FrameHeader(byte[] data) {
		return !isValidFrame(data) ? null : new MP3FrameHeader(data);
	}

	int frameSize() {
		if(layer == 1) {
			return 4 * (12 * bitRate * 1000 / samplingRate + (padding ? 1 : 0));
		} else {
			return 144 * bitRate * 1000 / samplingRate + (padding ? 1 : 0);
		}
	}

	private static boolean isValidFrame(byte[] data) {
		if(data.length != 4) {
			return false;
		}

		// BYTE 3 - AAAAAAAA
		if( (((int)data[0]) & 0xFF) != 0xFF ) {
			return false;
		}

		// BYTE 2 - AAABBCCD
		int AAA = getNumberFromBitRange(data[1], 0, 3);
		if(AAA != 0x07) {
			return false;
		}

		int BB = getNumberFromBitRange(data[1], 3, 5);
		if(!toList(MPEG_VERSION).contains(BB)) {
			return false;
		}

		int CC = getNumberFromBitRange(data[1], 5, 7);
		if(!toList(LAYER_DESCRIPTION).contains(CC)) {
			return false;
		}

		// BYTE 1 - EEEEFFGH
		int EEEE = getNumberFromBitRange(data[2], 0, 4);
		if(!(EEEE > 0 && EEEE < 15)) {
			return false;
		}

		int FF = getNumberFromBitRange(data[2], 4, 6);
		if(!(FF >= 0 && FF <= 2)) {
			return false;
		}

		return true;
	}

	private static List<Integer> toList(int[] arr) {
		return Arrays.stream(arr).boxed().collect(Collectors.toList());
	}

	private int getBitRateMatrixIndex() {
		if(mpegVersion == 1d) {
			return layer - 1;
		} else {
			if(layer == 1)	return 3;
			else			return 4;
		}
	}
	private int getSamplingRateMatrixIndex() {
		return mpegVersion == 1d ? 0 : (mpegVersion == 2d ? 1 : 2);
	}

	private static int getNumberFromBitRange(byte b, int startBitPos, int endBitPos) {
		int num = 0;
		int start = 7 - startBitPos;
		int numBits = endBitPos - startBitPos;
		int end = start - numBits;
		for(int i = start, exp = numBits - 1; i > end; i--, exp--) {
			num += isBitSet(b, i) ? Math.pow(2, exp) : 0;
		}
		return num;
	}

	private static boolean isBitSet(byte b, int bit) {
		int expected = 0x01 << bit;
		return (b & (0x01 << bit)) == expected;
	}
}
