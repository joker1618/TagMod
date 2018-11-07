package stuff;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.List;

import static xxx.joker.libs.core.utils.JkConsole.display;

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
        List<WebLyrics> wlList = JkStreams.filter(model.getWebLyrics(),
                wl -> wl.getArtist().startsWith("Vasco") && wl.getAlbum().equals("Ma Cosa Vuoi Che Sia Una Canzone")
        );
        wlList.forEach(wl -> wl.setAlbum("...Ma cosa vuoi che sia una canzone"));
        model.commit();
        wlList.forEach(wl -> display("%s", wl));
    }
}
