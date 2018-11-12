package xxx.joker.apps.tagmod.console.args;

import xxx.joker.apps.tagmod.model.facade.TagmodFile;
import xxx.joker.apps.tagmod.model.id3.enums.TxtEncoding;
import xxx.joker.libs.argsparser.design.annotation.Opt;
import xxx.joker.libs.argsparser.design.classType.InputOption;
import xxx.joker.libs.core.exception.JkRuntimeException;
import xxx.joker.libs.core.utils.JkStreams;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TmcArgs extends InputOption<TmcCmd> {

	@Opt(name = "show", aliases = {"-show"})
	private Boolean show = false;
	@Opt(name = "diff", aliases = {"-diff"})
	private Path[] diff;
	@Opt(name = "info", aliases = {"-info"})
	private Boolean info = false;
	@Opt(name = "export", aliases = {"-export"})
	private Boolean export = false;
	@Opt(name = "config", aliases = {"-config"})
	private Boolean config = false;
	@Opt(name = "edit", aliases = {"-edit"})
	private Boolean edit = false;
	@Opt(name = "test", aliases = {"-test"})
	private Boolean test = false;
	@Opt(name = "delete", aliases = {"-delete", "del", "-del"})
	private Boolean delete = false;
	@Opt(name = "summary", aliases = {"-summary", "sum", "-sum"})
	private Boolean summary = false;
	@Opt(name = "check", aliases = {"-check"})
	private Boolean check = false;
	@Opt(name = "recover", aliases = {"-recover"})
	private Boolean recover = false;

	@Opt(name = "picType", aliases = {"-pt", "-picType", "pictype", "-pictype"})
	private Boolean picType = false;
	@Opt(name = "pics", aliases = {"-pics", "picture", "-pictures"})
	private Boolean pictures = false;
	@Opt(name = "otherLyrics", aliases = {"-ol", "ol", "-otherLyrics"})
	private Boolean otherLyrics = false;

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
	@Opt(name = "cover", aliases = {"-cover"}, classes = {Boolean.class, String.class})
	private Object cover;

	@Opt(name = "clear", aliases = {"-clear"})
	private Boolean clear = false;

    @Opt(name = "attr", aliases = {"-attr"})
    private Boolean attribute = false;
    @Opt(name = "tm", aliases = {"-tm", "tagmod", "-tagmod"})
    private Boolean tm = false;
    @Opt(name = "size", aliases = {"-size"})
    private Boolean size = false;
    @Opt(name = "t2", aliases = {"-t2"})
    private Boolean t2 = false;
    @Opt(name = "t1", aliases = {"-t1"})
    private Boolean t1 = false;
    @Opt(name = "audio", aliases = {"-audio"})
    private Boolean audio = false;
    @Opt(name = "all", aliases = {"-all"})
    private Boolean all = false;
    @Opt(name = "autoAll", aliases = {"-autoAll", "autoall", "-autoall", "aa", "-aa"})
    private Boolean autoAll = false;
    @Opt(name = "table", aliases = {"-table"})
    private Boolean table = false;

    @Opt(name = "outputFormats", aliases = {"-of", "of"})
	private Boolean outputFormats = false;

	@Opt(name = "enc", aliases = {"-enc", "encoding", "-encoding"})
	private String encoding;
    @Opt(name = "ver", aliases = {"-ver", "version", "-version"})
    private Integer version;
    @Opt(name = "padding", aliases = {"-pad", "pad", "--padding"})
    private Integer padding;
	@Opt(name = "noSign", aliases = {"nosign", "-nosign", "-noSign"})
	private Boolean noSign = false;

	@Opt(name = "files", aliases = {"-f", "-files"})
	private Path[] paths;

	@Opt(name = "help", aliases = {"-h", "--help"})
	private Boolean help = false;

	public List<TagmodFile> getTagmodFiles() {
		return toTagmodFiles(paths);
	}
	public List<Path> getFilePaths() {
		return new ArrayList<>(Arrays.asList(paths));
	}

	public TagmodFile[] getDiffFiles() {
		List<TagmodFile> tflist = toTagmodFiles(diff);
		return tflist == null ? null : tflist.toArray(new TagmodFile[0]);
	}

	public boolean isPicType() {
		return picType != null && picType;
	}

	public boolean isPictures() {
		return pictures != null && pictures;
	}
	public boolean isOtherLyrics() {
		return otherLyrics != null && otherLyrics;
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
		return clear != null && clear;
	}

    public boolean isTagmod() {
        return tm != null && tm;
    }
    public boolean isSize() {
        return size != null && size;
    }
    public boolean isTAGv2() {
        return t2 != null && t2;
    }
    public boolean isTAGv1() {
        return t1 != null && t1;
    }
    public boolean isAudio() {
        return audio != null && audio;
    }
    public boolean isAll() {
        return all != null && all;
    }
    public boolean isAutoAll() {
        return autoAll != null && autoAll;
    }
    public boolean isAttribute() {
        return attribute != null && attribute;
    }
    public boolean isTable() {
        return table != null && table;
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
    public String getCover() {
        return cover instanceof String ? (String)cover : null;
    }

	public TxtEncoding getEncoding() {
		return TxtEncoding.fromLabel(encoding);
	}
	public Integer getVersion() {
		return version;
	}
    public boolean isOutputFormats() {
        return outputFormats != null && outputFormats;
    }

    public Integer getPadding() {
        return padding;
    }
    public boolean isNoSign() {
        return noSign != null && noSign;
    }

	private List<TagmodFile> toTagmodFiles(Path[] pathArr) {
		if(pathArr == null)	{
			return null;
		}

		return JkStreams.map(Arrays.asList(pathArr), p  -> {
			try {
				return new TagmodFile(p);
			} catch (Exception e) {
				throw new JkRuntimeException(e, "Unable to create TagmodFile from "+p);
			}
		});
	}

}
