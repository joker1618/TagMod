package xxx.joker.apps.tagmod.downloader.lyrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.javalibs.html.JkTextScanner;
import xxx.joker.libs.javalibs.html.JkTextScannerImpl;

import java.util.ArrayList;
import java.util.List;

public class AngoloTestiCrawler extends BaseCrawler {

    private static final Logger logger = LoggerFactory.getLogger(AngoloTestiCrawler.class);

    private static final String baseUrl = "http://www.angolotesti.it";

    public void parseWebData(String artist, String discographySubUrl) {
        String html = getHtml(baseUrl + discographySubUrl);
        List<WebLyrics> webLyrics = parseDiscography(html);
        webLyrics.forEach(wl -> wl.setArtist(artist));

        logger.info("Parsed {} lyrics", webLyrics.size());

        LyricsModel model = LyricsModel.getInstance();
        model.setAll(webLyrics);
        model.commit();
    }

    private List<WebLyrics> parseDiscography(String html) {
        List<WebLyrics> toRet = new ArrayList<>();

        JkTextScanner scanner = new JkTextScannerImpl(html);

        while(scanner.startCursorAfter("<div class=\"album")) {
            JkTextScanner subsc = scanner.subScannerBetween("<ul class=\"elenco\">", "</ul>");


            String str = subsc.nextValueBetween("<h3>", "</h3>").trim();
            int idx = str.lastIndexOf('(');
            if(idx == -1)    break;

            String album = str.substring(0, idx).trim();
            String year = str.substring(idx+1).replace(")", "").trim();

            logger.info("Parsing album {}", album);

            int numTrack = 1;
            while(subsc.startCursorAfter("<li>")) {
                WebLyrics wl = new WebLyrics();
                wl.setTitle(subsc.nextAttrValue("title").replaceAll("^Testo", "").trim());
                wl.setTrack(numTrack++);
                wl.setAlbum(album);
                wl.setYear(year);
                wl.setLyricsText(parseLyrics(getHtml(baseUrl + subsc.nextAttrValue("href"))));
                toRet.add(wl);
            }
        }

        return toRet;
    }

    private String parseLyrics(String html) {
        JkTextScanner scanner = new JkTextScannerImpl(html);
        scanner.startCursorAfter("<div class=\"testo\" oncopy=\"copy();\">");
        scanner.endCursorAt("<div id=\"copyright\">");
        String str = scanner.toString()
                .replaceAll("<script(.+?)</script>", "")
                .replaceAll("<div(.+?)</div>", "")
                .replaceAll("<(.+?)>", "")
                .trim();
        return str;
    }

}
