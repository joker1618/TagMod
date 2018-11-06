package xxx.joker.apps.tagmod.downloader.lyrics.model;

import xxx.joker.libs.core.repository.JkDataModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

public class LyricsModel extends JkDataModel {

    private static final Path DB_FOLDER = Paths.get(System.getProperty("user.home")).resolve("IdeaProjects\\APPS\\tagmod\\tm-lyrics-downloader\\src\\test\\resources\\db");
    private static final String DB_NAME= "TagModLyricsDB";
    private static final String PKG_TO_SCAN = "xxx.joker.apps.tagmod.downloader.lyrics.model";

    private static final LyricsModel instance = new LyricsModel();

    private LyricsModel() {
        super(DB_FOLDER, DB_NAME, PKG_TO_SCAN);
    }

    public static LyricsModel getInstance() {
        return instance;
    }

    public void setAll(List<WebLyrics> webLyrics) {
        TreeSet<WebLyrics> data = getData(WebLyrics.class);
        data.removeIf(webLyrics::contains);
        data.addAll(webLyrics);
    }

    public void addAll(List<WebLyrics> webLyrics) {
        getData(WebLyrics.class).addAll(webLyrics);
    }

    public TreeSet<WebLyrics> getWebLyrics() {
        return getData(WebLyrics.class);
    }




}
