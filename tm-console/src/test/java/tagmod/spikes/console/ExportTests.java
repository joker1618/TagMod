package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.main.TmcEngine;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class ExportTests extends CommonTest {

    @Test
    public void exportLyrics() {
        doTest("export;lyrics;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }

    @Test
    public void exportPics() {
        doTest("export;pics;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }



    private void doTest(String str) {
        TmcArgs tmcArgs = super.parseArgs(str, false);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }


}
