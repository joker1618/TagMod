package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.util.TmFormat;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class CopyStuff {

    @Test
    public void copyOneForEachAlbum() {
        Path base = Paths.get("C:\\Users\\feder\\Desktop\\music");
        Path outFolder = Paths.get("C:\\Users\\feder\\Desktop\\tagmod-one-each");

        List<Path> files = JkFiles.findFiles(base, true, TmFormat::isMP3File);
        files = JkStreams.map(files, p -> p.toAbsolutePath().normalize());
        display("Found %d MP3 in folder %s", files.size(), base);

        Map<Path, Path> map = new HashMap<>();
        files.forEach(f -> map.putIfAbsent(JkFiles.getParent(f), f));

        map.forEach((k,v)-> display("%s\t%s", v, outFolder.resolve(v.getFileName())));
        display("Num: %d", map.size());
    }
}
