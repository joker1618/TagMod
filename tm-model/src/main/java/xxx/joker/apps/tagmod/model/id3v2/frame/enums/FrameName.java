package xxx.joker.apps.tagmod.model.id3v2.frame.enums;

/**
 * Created by f.barbano on 26/02/2018.
 */
public enum FrameName {

	AENC  ("CRA",  "AENC",  "AENC"),
	APIC  ("PIC",  "APIC",  "APIC"),
	ASPI  (null,   null,    "ASPI"),
	COMM  ("COM",  "COMM",  "COMM"),
	COMR  (null,   "COMR",  "COMR"),
	CRM   ("CRM",  null,    null  ),
	ENCR  (null,   "ENCR",  "ENCR"),
	EQU2  ("EQU",  "EQUA",  "EQU2"),
	ETCO  ("ETC",  "ETCO",  "ETCO"),
	GEOB  ("GEO",  "GEOB",  "GEOB"),
	GRID  (null,   "GRID",  "GRID"),
	IPLS  ("IPL",  "IPLS",   null ),
	LINK  ("LNK",  "LINK",  "LINK"),
	MCDI  ("MCI",  "MCDI",  "MCDI"),
	MLLT  ("MLL",  "MLLT",  "MLLT"),
	OWNE  (null,   "OWNE",  "OWNE"),
	PCNT  ("CNT",  "PCNT",  "PCNT"),
	POPM  ("POP",  "POPM",  "POPM"),
	POSS  (null,   "POSS",  "POSS"),
	PRIV  (null,   "PRIV",  "PRIV"),
	RBUF  ("BUF",  "RBUF",  "RBUF"),
	RVA2  ("RVA",  "RVAD",  "RVA2"),
	RVRB  ("REV",  "RVRB",  "RVRB"),
	SEEK  (null,   null,    "SEEK"),
	SIGN  (null,   null,    "SIGN"),
	SYLT  ("SLT",  "SYLT",  "SYLT"),
	SYTC  ("STC",  "SYTC",  "SYTC"),
	TALB  ("TAL",  "TALB",  "TALB"),
	TBPM  ("TBP",  "TBPM",  "TBPM"),
	TCOM  ("TCM",  "TCOM",  "TCOM"),
	TCON  ("TCO",  "TCON",  "TCON"),
	TCOP  ("TCR",  "TCOP",  "TCOP"),
	TDEN  (null,   null,    "TDEN"),
	TDLY  ("TDY",  "TDLY",  "TDLY"),
	TDOR  ("TOR",  "TORY",  "TDOR"),
	TDRC  (null,   null,    "TDRC"),
	TIME  ("TIM",  "TIME",  null  ),
	TRDA  ("TRD",  "TRDA",  null  ),
	TDAT  ("TDA",  "TDAT",  null  ),
	TYER  ("TYE",  "TYER",  null  ),
	TDRL  (null,   null,    "TDRL"),
	TDTG  (null,   null,    "TDTG"),
	TENC  ("TEN",  "TENC",  "TENC"),
	TEXT  ("TXT",  "TEXT",  "TEXT"),
	TFLT  ("TFT",  "TFLT",  "TFLT"),
	TIPL  (null,   null  ,  "TIPL"),
	TIT1  ("TT1",  "TIT1",  "TIT1"),
	TIT2  ("TT2",  "TIT2",  "TIT2"),
	TIT3  ("TT3",  "TIT3",  "TIT3"),
	TKEY  ("TKE",  "TKEY",  "TKEY"),
	TLAN  ("TLA",  "TLAN",  "TLAN"),
	TLEN  ("TLE",  "TLEN",  "TLEN"),
	TMCL  (null,   null  ,  "TMCL"),
	TMED  ("TMT",  "TMED",  "TMED"),
	TMOO  (null,   null,    "TMOO"),
	TOAL  ("TOT",  "TOAL",  "TOAL"),
	TOFN  ("TOF",  "TOFN",  "TOFN"),
	TOLY  ("TOL",  "TOLY",  "TOLY"),
	TOPE  ("TOA",  "TOPE",  "TOPE"),
	TOWN  (null,   "TOWN",  "TOWN"),
	TPE1  ("TP1",  "TPE1",  "TPE1"),
	TPE2  ("TP2",  "TPE2",  "TPE2"),
	TPE3  ("TP3",  "TPE3",  "TPE3"),
	TPE4  ("TP4",  "TPE4",  "TPE4"),
	TPOS  ("TPA",  "TPOS",  "TPOS"),
	TPRO  (null,   null,    "TPRO"),
	TPUB  ("TPB",  "TPUB",  "TPUB"),
	TRCK  ("TRK",  "TRCK",  "TRCK"),
	TRSN  (null,   "TRSN",  "TRSN"),
	TRSO  (null,   "TRSO",  "TRSO"),
	TSIZ  ("TSI",  "TSIZ",  null  ),
	TSOA  (null,   null,    "TSOA"),
	TSOP  (null,   null,    "TSOP"),
	TSOT  (null,   null,    "TSOT"),
	TSRC  ("TRC",  "TSRC",  "TSRC"),
	TSSE  ("TSS",  "TSSE",  "TSSE"),
	TSST  (null,   null,    "TSST"),
	TXXX  ("TXX",  "TXXX",  "TXXX"),
	UFID  ("UFI",  "UFID",  "UFID"),
	USER  (null,   "USER",  "USER"),
	USLT  ("ULT",  "USLT",  "USLT"),
	WCOM  ("WCM",  "WCOM",  "WCOM"),
	WCOP  ("WCP",  "WCOP",  "WCOP"),
	WOAF  ("WAF",  "WOAF",  "WOAF"),
	WOAR  ("WAR",  "WOAR",  "WOAR"),
	WOAS  ("WAS",  "WOAS",  "WOAS"),
	WORS  (null,   "WORS",  "WORS"),
	WPAY  (null,   "WPAY",  "WPAY"),
	WPUB  ("WPB",  "WPUB",  "WPUB"),
	WXXX  ("WXX",  "WXXX",  "WXXX"),

	;

	private String v2;
	private String v3;
	private String v4;

	FrameName(String v2, String v3, String v4) {
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}

	public String getFrameId(int version) {
		switch (version) {
			case 2:	return v2;
			case 3:	return v3;
			case 4:	return v4;
		}
		return null;
	}

	public static FrameName getByFrameId(int version, String frameId) {
		if(frameId != null) {
			for (FrameName fname : values()) {
				if (frameId.equals(fname.getFrameId(version))) {
					return fname;
				}
			}
		}
		return null;
	}

}
