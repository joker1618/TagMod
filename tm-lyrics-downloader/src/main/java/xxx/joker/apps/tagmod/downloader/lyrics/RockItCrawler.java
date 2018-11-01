package xxx.joker.apps.tagmod.downloader.lyrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.javalibs.html.JkTextScanner;
import xxx.joker.libs.javalibs.html.JkTextScannerImpl;
import xxx.joker.libs.javalibs.utils.JkConverter;
import xxx.joker.libs.javalibs.utils.JkStrings;
import xxx.joker.libs.javalibs.utils.JkTests;

import java.util.ArrayList;
import java.util.List;

public class RockItCrawler extends BaseCrawler {

    private static final Logger logger = LoggerFactory.getLogger(RockItCrawler.class);

    private static final String baseUrl = "https://www.rockit.it";

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
        scanner.startCursorAfter("<div class=\"box-blocco clearfix\">");

        while(scanner.startCursorAfter("<div class=\"post\">", "<h2 class=\"titolo\">")) {
            String albumUrl = baseUrl + scanner.nextAttrValue("href");
            String album = scanner.nextValueBetween("<span class=\"artista\">", "</span>").trim();
            String year = scanner.nextValueBetween("<span class=\"anno\">", "</span>").trim();

            if(JkTests.isInteger(year)) {
                logger.info("Parsing album: {}", album);
                List<WebLyrics> wls = parseAlbum(getHtml(albumUrl));
                wls.forEach(wl -> {
                    wl.setAlbum(album);
                    wl.setYear(year);
                });
                toRet.addAll(wls);

            } else {
                break;
            }
        }

        return toRet;
    }

    private List<WebLyrics> parseAlbum(String html) {
        List<WebLyrics> toRet = new ArrayList<>();

        JkTextScanner scanner = new JkTextScannerImpl(html);
        scanner.startCursorAfter("<div class=\"elenco-brani griglia\"", "<ul>");
        scanner.endCursorAt("<div class=\"box-rece\">");

        while(scanner.startCursorAfter("<li class=\"title\">")) {
            String lyricsUrl = baseUrl + scanner.nextAttrValue("href");
            String strName = scanner.nextValueBetween(">", "</a>");
            String[] split = JkStrings.splitAllFields(strName, ".", true);

            WebLyrics wl = new WebLyrics();
            wl.setTrack(JkConverter.stringToInteger(split[0]));
            wl.setTitle(split[1]);
            wl.setLyricsText(parseLyrics(getHtml(lyricsUrl)));
            toRet.add(wl);
        }

        return toRet;
    }

    private String parseLyrics(String html) {
        JkTextScanner scanner = new JkTextScannerImpl(html);
        scanner.startCursorAfter("<h2>Testo della canzone</h2>");
        String str = scanner.nextValueBetween("<p>", "</p>");
        return str.replaceAll("<br(.+?)>", "").trim();
    }

}
