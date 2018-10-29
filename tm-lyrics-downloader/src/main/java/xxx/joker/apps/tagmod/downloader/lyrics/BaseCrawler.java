package xxx.joker.apps.tagmod.downloader.lyrics;

import xxx.joker.libs.javalibs.exception.JkRuntimeException;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;
import xxx.joker.libs.javalibs.utils.JkWeb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class BaseCrawler {

    protected static final Path HTML_FOLDER = Paths.get("C:\\Users", System.getProperty("user.name"), ".tempApps", "html");

    protected String getHtml(String url) {
        return JkStreams.join(getHtmlLines(url), "\n");
    }
    protected List<String> getHtmlLines(String url) {
        try {
            String fname = url.replaceAll("[:/.]", "_").replaceAll("_+", "_");
            Path htmlPath = HTML_FOLDER.resolve(fname + ".html");
            List<String> htmlLines;
            if (!Files.exists(htmlPath)) {
                htmlLines = JkWeb.downloadHtmlLines(url);
                JkFiles.writeFile(htmlPath, htmlLines, true);
            } else {
                htmlLines = Files.readAllLines(htmlPath);
            }
            return htmlLines;

        } catch(IOException ex) {
            throw new JkRuntimeException(ex);
        }
    }

}
