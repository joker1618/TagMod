package stuff;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.core.utils.JkConsole;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class DisplayStuff {

    @Test
    public void displayValues() {
        String year = "";
        String artist = "The Offspring";
        String album = "";

        LyricsModel model = LyricsModel.getInstance();

        List<WebLyrics> llist = model.getWebLyrics().stream()
                .filter(wl -> artist.isEmpty() || wl.getArtist().equalsIgnoreCase(artist))
                .filter(wl -> year.isEmpty() || wl.getYear().equalsIgnoreCase(year))
                .filter(wl -> album.isEmpty() || wl.getAlbum().equalsIgnoreCase(album))
                .sorted()
                .collect(Collectors.toList());

        display("%s - %s - %s", artist, year, album);

        llist.forEach(l -> display("%s", l));
//        llist.forEach(l -> display("%s", l.getTitle()));
//        llist.stream().filter(t -> t.getTitle().contains("!")).forEach(wl -> display("%s", wl));

//        llist.stream().sorted(Comparator.comparing(WebLyrics::getTrack)).map(WebLyrics::getAlbum).distinct().forEach(JkConsole::display);
    }


    @Test
    public void displayRecap() {
        LyricsModel model = LyricsModel.getInstance();

        Map<String, List<WebLyrics>> map = JkStreams.toMap(model.getWebLyrics(), WebLyrics::getArtist);
        for(String artist : JkStreams.sorted(map.keySet())) {
            Map<String, List<WebLyrics>> albumMap = JkStreams.toMap(map.get(artist), wl -> strf("%s %s", wl.getYear(), wl.getAlbum()));
            display("%s :  %d", artist, albumMap.size());
//            display("\n%s", artist);
//            for(String alb : JkStreams.sorted(albumMap.keySet())) {
//                List<WebLyrics> wlalb = albumMap.get(alb);
//                display("  %s  -->  %d", alb, wlalb.size());
////                JkStreams.sorted(wlalb).forEach(wl -> display("    %02d %s", wl.getTrack(), wl.getTitle()));
//            }
        }
    }


}
