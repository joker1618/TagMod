package webCrawler.covers;

import common.TestUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import xxx.joker.libs.core.html.JkTextScanner;
import xxx.joker.libs.core.html.JkTextScannerImpl;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkWeb;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class VascoRossiCover {

    private static Path coverFolder = TestUtil.RESOURCES_TEST_FOLDER.resolve("covers");

    @Test
    public void downloadCovers() throws IOException {
        String[] urls = {
            "https://www.vascorossi.net/it/discografia/discografia-album/521-31539.html",
            "https://www.vascorossi.net/it/discografia/discografia-album/521-31540.html"
        };

        for(String url : urls) {
            String html = JkWeb.downloadHtml(url);
            List<Pair<String, String>> albumUrls = parseAlbumUrls(html);
            for(Pair<String, String> pair : albumUrls) {
                display("Parse album %s", pair.getKey());
                String albumHtml = JkWeb.downloadHtml(pair.getValue());
                JkTextScanner sc = new JkTextScannerImpl(albumHtml);
                sc.startCursorAfter("<div class=\"imgrechts\"", "</script>", "<a");
                String coverUrl = sc.nextAttrValue("href");
                byte[] coverBytes = JkWeb.downloadResource(coverUrl);
                Path outPath = coverFolder.resolve("Vasco Rossi").resolve(pair.getKey()+".jpg");
                JkFiles.writeFile(outPath, coverBytes, true);
            }
        }

        display("END");
    }

    private List<Pair<String, String>> parseAlbumUrls(String html) {
        JkTextScanner scanner = new JkTextScannerImpl(html);
        List<Pair<String, String>> list = new ArrayList<>();
        while(scanner.startCursorAfter("<a class=\"album\"")) {
            String url = scanner.nextAttrValue("href");
            String name = scanner.nextAttrValue("title");
            list.add(Pair.of(escapeHtml(name), url));
        }
        return list;
    }

    private String escapeHtml(String html) {
        return html.replace("&apos;", "'").replace("&amp;", "&");
    }
}
