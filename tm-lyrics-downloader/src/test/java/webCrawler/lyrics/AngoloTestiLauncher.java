package webCrawler.lyrics;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.AngoloTestiCrawler;
import xxx.joker.apps.tagmod.downloader.lyrics.model.LyricsModel;
import xxx.joker.apps.tagmod.downloader.lyrics.model.WebLyrics;

import java.util.List;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class AngoloTestiLauncher {

    String[][] urls = {
            {"AC DC", "/A/testi_canzoni_ac_dc_1789/"},
            {"Alborosie", "/A/testi_canzoni_alborosie_43328/"},
            {"Articolo 31", "/A/testi_canzoni_articolo_31_32/"},
            {"Bob Marley", "/B/testi_canzoni_bob_marley_55/"},
            {"Guns'N'Roses", "/G/testi_canzoni_gunsnroses_148/"},
            {"Infected Mushroom", "/I/testi_canzoni_infected_mushroom_26498/"},
            {"Iron Maiden", "/I/testi_canzoni_iron_maiden_155/"},
            {"Led Zeppelin", "/L/testi_canzoni_led_zeppelin_1733/"},
            {"Limp Bizkit", "/L/testi_canzoni_limp_bizkit_803/"},
            {"Linkin Park", "/L/testi_canzoni_linkin_park_1041/"},
            {"Litfiba", "/L/testi_canzoni_litfiba_3512/"},
            {"Max Pezzali", "/M/testi_canzoni_max_pezzali_2570/"},
            {"Metallica", "/M/testi_canzoni_metallica_219/"},
            {"Michael Jackson", "/M/testi_canzoni_michael_jackson_220/"},
            {"Motel Connection", "/M/testi_canzoni_motel_connection_231/"},
            {"Nirvana", "/N/testi_canzoni_nirvana_247/"},
            {"Notorius B.I.G.", "/N/testi_canzoni_notorious_big_2256/"},
            {"Oasis", "/O/testi_canzoni_oasis_253/"},
            {"Police", "/P/testi_canzoni_police_332/"},
            {"Prodigy", "/P/testi_canzoni_prodigy_268/"},
            {"Queen", "/Q/testi_canzoni_queen_271/"},
            {"Red Hot Chili Peppers", "/R/testi_canzoni_red_hot_chili_peppers_1444/"},
            {"Sting", "/S/testi_canzoni_sting_316/"},
            {"The Beatles", "/B/testi_canzoni_beatles_the_44/"},
            {"The Darkness", "/D/testi_canzoni_darkness_the_884/"},
            {"The Offspring", "/O/testi_canzoni_offspring_the_254/"},
            {"Tool", "/T/testi_canzoni_tool_1951/"},
            {"Vasco Rossi", "/V/testi_canzoni_vasco_rossi_1563/"}
    };

    @Test
    public void getArtist() {
        String artist = "Vasco Rossi";
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
                display("%s", arr[0]);
                crawler.parseWebData(arr[0], arr[1]);
            }
        }
    }

}
