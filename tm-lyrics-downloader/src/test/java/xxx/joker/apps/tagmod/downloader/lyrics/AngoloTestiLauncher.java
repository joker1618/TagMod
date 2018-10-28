package xxx.joker.apps.tagmod.downloader.lyrics;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class AngoloTestiLauncher {

    String[][] urls = {
            {"Linkin Park", "/L/testi_canzoni_linkin_park_1041/"},
            {"Vasco Rossi", "/V/testi_canzoni_vasco_rossi_1563/"},
            {"Oasis", "/O/testi_canzoni_oasis_253/"},
            {"The Beatles", "/B/testi_canzoni_beatles_the_44/"},
            {"Nirvana", "/N/testi_canzoni_nirvana_247/"},
            {"The Offspring", "/O/testi_canzoni_offspring_the_254/"},
            {"Michael Jackson", "/M/testi_canzoni_michael_jackson_220/"},
            {"Metallica", "/M/testi_canzoni_metallica_219/"},
            {"Iron Maiden", "/I/testi_canzoni_iron_maiden_155/"},
            {"Litfiba", "/L/testi_canzoni_litfiba_3512/"},
            {"Red Hot Chili Peppers", "/R/testi_canzoni_red_hot_chili_peppers_1444/"},
            {"AC DC", "/A/testi_canzoni_ac_dc_1789/"}
    };

    @Test
    public void getArtist() {
        String artist = "Oasis";
        for(String[] arr : urls) {
            if(artist.equalsIgnoreCase(arr[0])) {
                AngoloTestiCrawler crawler = new AngoloTestiCrawler();
                crawler.parseWebData(arr[0], arr[1]);
                break;
            }
        }
    }

    @Test
    public void getAll() {
        for(String[] arr : urls) {
            AngoloTestiCrawler crawler = new AngoloTestiCrawler();
            crawler.parseWebData(arr[0], arr[1]);
        }
    }

    @Test
    public void setMissing() {
        LyricsModel model = LyricsModel.getInstance();
        List<String> skipList = model.getWebLyrics().stream().map(WebLyrics::getArtist).distinct().collect(Collectors.toList());
        AngoloTestiCrawler crawler = new AngoloTestiCrawler();
        for(String[] arr : urls) {
            if(!skipList.contains(arr[0])) {
                crawler.parseWebData(arr[0], arr[1]);
            }
        }
    }

}
