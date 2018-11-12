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

        TreeSet<WebLyrics> webLyrics = model.getWebLyrics();
        int size = webLyrics.size();
        webLyrics.removeIf(wl -> wl.getArtist().equals("Vasco Rossi") && wl.getAlbum().equals("Rewind") && wl.getTrack() == 20);
        webLyrics.removeIf(wl -> wl.getArtist().equals("The Offspring") && wl.getAlbum().equals("Rise And Fall, Rage And Grace") && wl.getTitle().equals("O.c. Life"));

        int changed = size - webLyrics.size();
        for(WebLyrics wl : webLyrics) {
            if(wl.getArtist().equals("Vasco Rossi")) {
                if(wl.getAlbum().equals("Ma Cosa Vuoi Che Sia Una Canzone")) {
                    changed++;
                    wl.setAlbum("...Ma cosa vuoi che sia una canzone");
                }
                if(wl.getAlbum().equals("Nessun Pericolo Per Te")) {
                    changed++;
                    wl.setAlbum("Nessun Pericolo... Per Te");
                }
                if(wl.getAlbum().equals("10.7.90 San Siro") && wl.getYear().equals("1991")) {
                    changed++;
                    wl.setYear("1990");
                }
            }
            if(wl.getArtist().equals("The Offspring")) {
                if(wl.getAlbum().equals("Ignition") && wl.getYear().equals("1993")) {
                    changed++;
                    wl.setYear("1992");
                }
            }

            String oldTitle = wl.getTitle();
            wl.setTitle(oldTitle.replace("!", "").replace("?", ""));
            changed += !oldTitle.equals(wl.getTitle()) ? 1 : 0;
        }

        model.commit();

        display("Changed %d elements", changed);
    }



}
