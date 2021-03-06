package webCrawler.lyrics;

import org.junit.Test;
import xxx.joker.apps.tagmod.downloader.lyrics.RockItCrawler;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class RockItLauncher {

    @Test
    public void as() {
        String str = "<div id='div-gpt-ad-1511539501692-0'><script>googletag.cmd.push(function(){googletag.display('div-gpt-ad-1511539501692-0');});</script></div></script></div>";
        display(str.replaceAll("<script(.+?)</script>", ""));
        display(str.replaceAll("<scr(.+?)>", ""));
    }

    @Test
    public void getVasco() {
        RockItCrawler crawler = new RockItCrawler();
        crawler.parseWebData("Vasco Rossi", "/vascorossi/discografia");
    }
}
