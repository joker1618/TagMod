package stuff;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.apps.tagmod.model.id3v2.frame.data.Lyrics;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ModelChanges {

    @Test
    public void seeAlbum() {
        LyricsModel model = LyricsModel.getInstance();
        List<WebLyrics> wlList = JkStreams.filter(model.getWebLyrics(),
                wl -> wl.getArtist().startsWith("Vasco") && Integer.parseInt(wl.getYear()) == 1978
        );
        display("");
        wlList.forEach(wl -> display("%s", wl));
        display("");
        wlList.forEach(wl -> display("%s", wl.getTitle()));
    }

    @Test
    public void change() {
        LyricsModel model = LyricsModel.getInstance();

        int changed = 0;
        for(WebLyrics wl : model.getWebLyrics()) {
            if(wl.getArtist().startsWith("Vasco")) {
                if(wl.getAlbum().equals("Ma Cosa Vuoi Che Sia Una Canzone")) {
                    changed++;
                    wl.setAlbum("...Ma cosa vuoi che sia una canzone");
                }
                if(wl.getAlbum().equals("Nessun Pericolo Per Te")) {
                    changed++;
                    wl.setAlbum("Nessun Pericolo... Per Te");
                }
            }
        }

        model.commit();

        display("Changed %d elements", changed);
    }



}
