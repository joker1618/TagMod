package xxx.joker.apps.tagmod.model.id3.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f.barbano on 01/01/2018.
 */
public enum ID3Genre {

	BLUES                   ("Blues",                   0),
	CLASSIC_ROCK            ("Classic Rock",            1),
	COUNTRY                 ("Country",                 2),
	DANCE                   ("Dance",                   3),
	DISCO                   ("Disco",                   4),
	FUNK                    ("Funk",                    5),
	GRUNGE                  ("Grunge",                  6),
	HIP_HOP                 ("Hip-Hop",                 7),
	JAZZ                    ("Jazz",                    8),
	METAL                   ("Metal",                   9),
	NEW_AGE                 ("New Age",                 10),
	OLDIES                  ("Oldies",                  11),
	OTHER                   ("Other",                   12),
	POP                     ("Pop",                     13),
	RHYTHM_AND_BLUES        ("Rhythm and Blues",        14),
	RAP                     ("Rap",                     15),
	REGGAE                  ("Reggae",                  16),
	ROCK                    ("Rock",                    17),
	TECHNO                  ("Techno",                  18),
	INDUSTRIAL              ("Industrial",              19),
	ALTERNATIVE             ("Alternative",             20),
	SKA                     ("Ska",                     21),
	DEATH_METAL             ("Death Metal",             22),
	PRANKS                  ("Pranks",                  23),
	SOUNDTRACK              ("Soundtrack",              24),
	EURO_TECHNO             ("Euro-Techno",             25),
	AMBIENT                 ("Ambient",                 26),
	TRIP_HOP                ("Trip-Hop",                27),
	VOCAL                   ("Vocal",                   28),
	JAZZ_AND_FUNK           ("Jazz & Funk",             29),
	FUSION                  ("Fusion",                  30),
	TRANCE                  ("Trance",                  31),
	CLASSICAL               ("Classical",               32),
	INSTRUMENTAL            ("Instrumental",            33),
	ACID                    ("Acid",                    34),
	HOUSE                   ("House",                   35),
	GAME                    ("Game",                    36),
	SOUND_CLIP              ("Sound Clip",              37),
	GOSPEL                  ("Gospel",                  38),
	NOISE                   ("Noise",                   39),
	ALTERNATIVE_ROCK        ("Alternative Rock",        40),
	BASS                    ("Bass",                    41),
	SOUL                    ("Soul",                    42),
	PUNK                    ("Punk",                    43),
	SPACE                   ("Space",                   44),
	MEDITATIVE              ("Meditative",              45),
	INSTRUMENTAL_POP        ("Instrumental Pop",        46),
	INSTRUMENTAL_ROCK       ("Instrumental Rock",       47),
	ETHNIC                  ("Ethnic",                  48),
	GOTHIC                  ("Gothic",                  49),
	DARKWAVE                ("Darkwave",                50),
	TECHNO_INDUSTRIAL       ("Techno-Industrial",       51),
	ELECTRONIC              ("Electronic",              52),
	POP_FOLK                ("Pop-Folk",                53),
	EURODANCE               ("Eurodance",               54),
	DREAM                   ("Dream",                   55),
	SOUTHERN_ROCK           ("Southern Rock",           56),
	COMEDY                  ("Comedy",                  57),
	CULT                    ("Cult",                    58),
	GANGSTA                 ("Gangsta",                 59),
	TOP_40                  ("Top 40",                  60),
	CHRISTIAN_RAP           ("Christian Rap",           61),
	POP_FUNK                ("Pop/Funk",                62),
	JUNGLE                  ("Jungle",                  63),
	NATIVE_AMERICAN         ("Native American",         64),
	CABARET                 ("Cabaret",                 65),
	NEW_WAVE                ("New Wave",                66),
	PSYCHEDELIC             ("Psychedelic",             67),
	RAVE                    ("Rave",                    68),
	SHOWTUNES               ("Showtunes",               69),
	TRAILER                 ("Trailer",                 70),
	LO_FI                   ("Lo-Fi",                   71),
	TRIBAL                  ("Tribal",                  72),
	ACID_PUNK               ("Acid Punk",               73),
	ACID_JAZZ               ("Acid Jazz",               74),
	POLKA                   ("Polka",                   75),
	RETRO                   ("Retro",                   76),
	MUSICAL                 ("Musical",                 77),
	ROCK_AND_ROLL           ("Rock & Roll",             78),
	HARD_ROCK               ("Hard Rock",               79),
	FOLK                    ("Folk",                    80),
	FOLK_ROCK               ("Folk-Rock",               81),
	NATIONAL_FOLK           ("National Folk",           82),
	SWING                   ("Swing",                   83),
	FAST_FUSION             ("Fast Fusion",             84),
	BEBOP                   ("Bebop",                   85),
	LATIN                   ("Latin",                   86),
	REVIVAL                 ("Revival",                 87),
	CELTIC                  ("Celtic",                  88),
	BLUEGRASS               ("Bluegrass",               89),
	AVANTGARDE              ("Avantgarde",              90),
	GOTHIC_ROCK             ("Gothic Rock",             91),
	PROGRESSIVE_ROCK        ("Progressive Rock",        92),
	PSYCHEDELIC_ROCK        ("Psychedelic Rock",        93),
	SYMPHONIC_ROCK          ("Symphonic Rock",          94),
	SLOW_ROCK               ("Slow Rock",               95),
	BIG_BAND                ("Big Band",                96),
	CHORUS                  ("Chorus",                  97),
	EASY_LISTENING          ("Easy Listening",          98),
	ACOUSTIC                ("Acoustic",                99),
	HUMOUR                  ("Humour",                  100),
	SPEECH                  ("Speech",                  101),
	CHANSON                 ("Chanson",                 102),
	OPERA                   ("Opera",                   103),
	CHAMBER_MUSIC           ("Chamber Music",           104),
	SONATA                  ("Sonata",                  105),
	SYMPHONY                ("Symphony",                106),
	BOOTY_BASS              ("Booty Bass",              107),
	PRIMUS                  ("Primus",                  108),
	PORN_GROOVE             ("Porn groove",             109),
	SATIRE                  ("Satire",                  110),
	SLOW_JAM                ("Slow Jam",                111),
	CLUB                    ("Club",                    112),
	TANGO                   ("Tango",                   113),
	SAMBA                   ("Samba",                   114),
	FOLKLORE                ("Folklore",                115),
	BALLAD                  ("Ballad",                  116),
	POWER_BALLAD            ("Power Ballad",            117),
	RHYTHMIC_SOUL           ("Rhythmic Soul",           118),
	FREESTYLE               ("Freestyle",               119),
	DUET                    ("Duet",                    120),
	PUNK_ROCK               ("Punk Rock",               121),
	DRUM_SOLO               ("Drum Solo",               122),
	A_CAPELLA               ("A capella",               123),
	EURO_HOUSE              ("Euro-House",              124),
	DANCE_HALL              ("Dance Hall",              125),
	GOA                     ("Goa",                     126),
	DRUM_AND_BASS           ("Drum & Bass",             127),
	CLUB_HOUSE              ("Club-House",              128),
	HARDCORE_TECHNO         ("Hardcore Techno",         129),
	TERROR                  ("Terror",                  130),
	INDIE                   ("Indie",                   131),
	BRITPOP                 ("BritPop",                 132),
	AFRO_PUNK               ("Afro-punk",               133),
	POLSK_PUNK              ("Polsk Punk",              134),
	BEAT                    ("Beat",                    135),
	CHRISTIAN_GANGSTA_RAP   ("Christian Gangsta Rap",   136),
	HEAVY_METAL             ("Heavy Metal",             137),
	BLACK_METAL             ("Black Metal",             138),
	CROSSOVER               ("Crossover",               139),
	CONTEMPORARY_CHRISTIAN  ("Contemporary Christian",  140),
	CHRISTIAN_ROCK          ("Christian Rock",          141),
	MERENGUE                ("Merengue",                142),
	SALSA                   ("Salsa",                   143),
	THRASH_METAL            ("Thrash Metal",            144),
	ANIME                   ("Anime",                   145),
	JPOP                    ("Jpop",                    146),
	SYNTHPOP                ("Synthpop",                147),
	ABSTRACT                ("Abstract",                148),
	ART_ROCK                ("Art Rock",                149),
	BAROQUE                 ("Baroque",                 150),
	BHANGRA                 ("Bhangra",                 151),
	BIG_BEAT                ("Big Beat",                152),
	BREAKBEAT               ("Breakbeat",               153),
	CHILLOUT                ("Chillout",                154),
	DOWNTEMPO               ("Downtempo",               155),
	DUB                     ("Dub",                     156),
	EBM                     ("EBM",                     157),
	ECLECTIC                ("Eclectic",                158),
	ELECTRO                 ("Electro",                 159),
	ELECTROCLASH            ("Electroclash",            160),
	EMO                     ("Emo",                     161),
	EXPERIMENTAL            ("Experimental",            162),
	GARAGE                  ("Garage",                  163),
	GLOBAL                  ("Global",                  164),
	IDM                     ("IDM",                     165),
	ILLBIENT                ("Illbient",                166),
	INDUSTRO_GOTH           ("Industro-Goth",           167),
	JAM_BAND                ("Jam Band",                168),
	KRAUTROCK               ("Krautrock",               169),
	LEFTFIELD               ("Leftfield",               170),
	LOUNGE                  ("Lounge",                  171),
	MATH_ROCK               ("Math Rock",               172),
	NEW_ROMANTIC            ("New Romantic",            173),
	NU_BREAKZ               ("Nu-Breakz",               174),
	POST_PUNK               ("Post-Punk",               175),
	POST_ROCK               ("Post-Rock",               176),
	PSYTRANCE               ("Psytrance",               177),
	SHOEGAZE                ("Shoegaze",                178),
	SPACE_ROCK              ("Space Rock",              179),
	TROP_ROCK               ("Trop Rock",               180),
	WORLD_MUSIC             ("World Music",             181),
	NEOCLASSICAL            ("Neoclassical",            182),
	AUDIOBOOK               ("Audiobook",               183),
	AUDIO_THEATRE           ("Audio Theatre",           184),
	NEUE_DEUTSCHE_WELLE     ("Neue Deutsche Welle",     185),
	PODCAST                 ("Podcast",                 186),
	INDIE_ROCK              ("Indie Rock",              187),
	G_FUNK                  ("G-Funk",                  188),
	DUBSTEP                 ("Dubstep",                 189),
	GARAGE_ROCK             ("Garage Rock",             190),
	PSYBIENT                ("Psybient",                191),
	;

	private String genreName;
	private int genreNum;

	ID3Genre(String genreName, int genreNum) {
		this.genreName = genreName;
		this.genreNum = genreNum;
	}

	public String getGenreName() {
		return genreName;
	}

	public int getGenreNum() {
		return genreNum;
	}

	public static ID3Genre getByName(String genreName) {
		if(StringUtils.isNotBlank(genreName)) {
            for(ID3Genre g : values()) {
                if(g.genreName.equals(genreName)) {
                    return g;
                }
            }
        }
		return null;
	}
	public static ID3Genre getByNumber(Integer genreNum) {
	    if(genreNum != null) {
            for (ID3Genre g : values()) {
                if (g.genreNum == genreNum) {
                    return g;
                }
            }
        }
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", genreName, genreNum);
	}

	public static ID3Genre[] valuesNameOrdered() {
		List<ID3Genre> collect = Arrays.stream(values()).sorted(Comparator.comparing(ID3Genre::name)).collect(Collectors.toList());
		return collect.toArray(new ID3Genre[0]);
	}
}
