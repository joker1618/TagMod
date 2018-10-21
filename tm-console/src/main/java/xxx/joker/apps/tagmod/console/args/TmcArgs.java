package xxx.joker.apps.tagmod.console.args;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.libs.argsparser.design.annotation.Opt;
import xxx.joker.libs.argsparser.design.classType.InputOption;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class TmcArgs extends InputOption<TmcCmd> {

	@Opt(name = "show", aliases = {"-show"})
	private Boolean show = false;
	@Opt(name = "describe", aliases = {"descr", "-descr", "-describe"})
	private Boolean describe = false;
	@Opt(name = "diff", aliases = {"-diff"})
	private Path[] diff;
	@Opt(name = "info", aliases = {"-info"})
	private Boolean info = false;
	@Opt(name = "export", aliases = {"-export"})
	private Boolean export = false;
	@Opt(name = "config", aliases = {"-config"})
	private Boolean config = false;
	@Opt(name = "edit", aliases = {"-edit"})
	private String[] edit;
//	@Opt(name = "set", aliases = {"-set"})
//	private Boolean set = false;
//	@Opt(name = "delete", aliases = {"-delete"})
//	private Boolean delete = false;


	@Opt(name = "picType", aliases = {"-pt", "-picType", "pictype", "-pictype"})
	private Boolean picType = false;
	@Opt(name = "pics", aliases = {"-pics", "picture", "-pictures"})
	private Boolean pictures = false;
	@Opt(name = "title", aliases = {"-title"}, classes = {Boolean.class, String.class})
	private Object title;
	@Opt(name = "artist", aliases = {"-artist"}, classes = {Boolean.class, String.class})
	private Object artist;
	@Opt(name = "album", aliases = {"-album"}, classes = {Boolean.class, String.class})
	private Object album;
	@Opt(name = "year", aliases = {"-year"}, classes = {Boolean.class, String.class})
	private Object year;
	@Opt(name = "track", aliases = {"-track"}, classes = {Boolean.class, String.class})
	private Object track;
	@Opt(name = "cdPos", aliases = {"cdpos", "-cdpos", "-cdPos"}, classes = {Boolean.class, String.class})
	private Object cdPos;
	@Opt(name = "genre", aliases = {"-genre"}, classes = {Boolean.class, String.class})
	private Object genre;
	@Opt(name = "lyrics", aliases = {"-lyrics"}, classes = {Boolean.class, String.class})
	private Object lyrics;
	@Opt(name = "cover", aliases = {"-cover"}, classes = {Boolean.class, Path.class})
	private Object cover;

//	@Opt(name = "-all")
//	private Boolean all = false;

	@Opt(name = "clear", aliases = {"-clear"})
	private Boolean clear = false;

	@Opt(name = "enc", aliases = {"-enc", "encoding", "-encoding"})
	private String encoding;
	@Opt(name = "ver", aliases = {"-ver", "version", "-version"})
	private Integer version;
	@Opt(name = "sign", aliases = {"-sign"})
	private Boolean sign = false;
	@Opt(name = "noSign", aliases = {"nosign", "-nosign", "-noSign"})
	private Boolean noSign = false;

	@Opt(name = "files", aliases = {"-f", "-files"})
	private Path[] paths;

	@Opt(name = "help", aliases = {"-h", "--help"})
	private Boolean help = false;

	public boolean isShow() {
		return show;
	}
	public boolean isConfig() {
		return config;
	}
	public List<String> getEdit() {
		return edit == null ? null : Arrays.asList(edit);
	}

	public List<TagmodFile> getTagmodFiles() {
		return toTagmodFiles(paths);
	}
	public List<Path> getDistinctCoverPaths() {
		return new ArrayList<>(Arrays.asList(paths));
	}

	public TagmodFile[] getDiffFiles() {
		List<TagmodFile> tflist = toTagmodFiles(diff);
		return tflist == null ? null : tflist.toArray(new TagmodFile[0]);
	}

	public boolean isPicType() {
		return picType;
	}

	public boolean isPictures() {
		return pictures;
	}

	public boolean isTitle() {
		return title instanceof Boolean && ((Boolean)title);
	}
	public boolean isArtist() {
		return artist instanceof Boolean && ((Boolean)artist);
	}
	public boolean isAlbum() {
		return album instanceof Boolean && ((Boolean)album);
	}
	public boolean isYear() {
		return year instanceof Boolean && ((Boolean)year);
	}
	public boolean isTrack() {
		return track instanceof Boolean && ((Boolean)track);
	}
	public boolean isCdPos() {
		return cdPos instanceof Boolean && ((Boolean)cdPos);
	}
	public boolean isGenre() {
		return genre instanceof Boolean && ((Boolean)genre);
	}
	public boolean isLyrics() {
		return lyrics instanceof Boolean && ((Boolean)lyrics);
	}
	public boolean isCover() {
		return cover instanceof Boolean && ((Boolean)cover);
	}

	public boolean isClear() {
		return clear;
	}

    public String getTitle() {
        return title instanceof String ? (String)title : null;
    }
    public String getArtist() {
        return artist instanceof String ? (String)artist : null;
    }
    public String getAlbum() {
        return album instanceof String ? (String)album : null;
    }
    public String getYear() {
        return year instanceof String ? (String)year : null;
    }
    public String getTrack() {
        return track instanceof String ? (String)track : null;
    }
    public String getCdPos() {
        return cdPos instanceof String ? (String)cdPos : null;
    }
    public String getGenre() {
        return genre instanceof String ? (String)genre : null;
    }
    public String getLyrics() {
        return lyrics instanceof String ? (String)lyrics : null;
    }
    public Path getCover() {
        return cover instanceof Path ? (Path)cover : null;
    }

//	public boolean isAll() {
//		return all;
//	}

	public TxtEncoding getEncoding() {
		return TxtEncoding.fromLabel(encoding);
	}
	public Integer getVersion() {
		return version;
	}
    public boolean isSign() {
        return sign;
    }
    public boolean isNoSign() {
        return noSign;
    }

	private List<TagmodFile> toTagmodFiles(Path[] pathArr) {
		if(pathArr == null)	{
			return null;
		}

		return JkStreams.map(Arrays.asList(pathArr), p  -> {
			try {
				return new TagmodFile(p);
			} catch (Exception e) {
				throw new RuntimeException("Unable to create TagmodFile from "+p);
			}
		});
	}

	// todo delete
	public static void main(String[] args) {
		String[] strings = StringUtils.substringsBetween(getString(), "@Opt(name = \"", "\"");
		Arrays.stream(strings).forEach(str -> {
			display("@OptName");
			display("%s(\"%s\")", str.toUpperCase(), str);
		});

	}
	private static String getString() {
		return "@Opt(name = \"show\", aliases = {\"-show\"})\n" +
				   "\tprivate Boolean show = false;\n" +
				   "\t@Opt(name = \"describe\", aliases = {\"descr\", \"-descr\", \"-describe\"})\n" +
				   "\tprivate Boolean describe = false;\n" +
				   "\t@Opt(name = \"diff\", aliases = {\"-diff\"})\n" +
				   "\tprivate Path[] diff;\n" +
				   "\t@Opt(name = \"info\", aliases = {\"-info\"})\n" +
				   "\tprivate Boolean info = false;\n" +
				   "\t@Opt(name = \"export\", aliases = {\"-export\"})\n" +
				   "\tprivate Boolean export = false;\n" +
				   "\n" +
				   "\n" +
				   "\t@Opt(name = \"picType\", aliases = {\"-pt\", \"-picType\", \"pictype\", \"-pictype\"})\n" +
				   "\tprivate Boolean picType = false;\n" +
				   "\t@Opt(name = \"genre\", aliases = {\"-genre\"})\n" +
				   "\tprivate Boolean genre = false;\n" +
				   "\t@Opt(name = \"cover\", aliases = {\"-cover\"})\n" +
				   "\tprivate Boolean cover = false;\n" +
				   "\t@Opt(name = \"pics\", aliases = {\"-pics\"})\n" +
				   "\tprivate Boolean pics = false;\n" +
				   "\t@Opt(name = \"lyrics\", aliases = {\"-l\", \"-lyrics\"})\n" +
				   "\tprivate Boolean lyrics = false;\n" +
				   "\n" +
				   "\t@Opt(name = \"-r\", aliases = {\"recursive\"})\n" +
				   "\tprivate Boolean recursive = false;\n" +
				   "\n" +
				   "\t@Opt(name = \"files\", aliases = {\"-f\", \"-files\"})\n" +
				   "\tprivate Path[] paths;\n" +
				   "\n" +
				   "\t@Opt(name = \"help\", aliases = {\"-h\", \"--help\"})\n" +
				   "\tprivate Boolean help = false;";
	}
}