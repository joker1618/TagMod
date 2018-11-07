package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcHelp;
import xxx.joker.apps.tagmod.console.main.TmcEngine;
import xxx.joker.apps.tagmod.stuff.TmcDebug;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class ShowTests extends CommonTest {

    @Test
    public void show() {
//        doTest("show;all;files;%s", HOME.resolve("Desktop\\tmtests"));
//        doTest("sum;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("show;audio;files;%s;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"), HOME.resolve("Desktop\\tmtests\\04 Toffee.mp3"));
        doTest("sum;table;files;%s", HOME.resolve("Desktop\\tmtests\\2000 - Hybrid Theory"));
//        doTest("sum;files;%s", HOME.resolve("Desktop\\tmtests\\1985 Cosa succede in città"));
//        doTest("show;lyrics;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("show;files;%s;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"), HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("show;lyrics;files;%s;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"), HOME.resolve("Desktop\\tmtests\\04 Toffee.mp3"));
    }

    @Test
    public void check() {
        doTest("check;files;%s", HOME.resolve("Desktop\\ttttt"));
    }

    @Test
    public void describe() {
        doTest("describe;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
    }

    @Test
    public void diff() {
        doTest("diff;Vasco/1993 Gli spari sopra/01 Lo show.mp3;Vasco/1993 Gli spari sopra/02 Non appari mai.mp3");
    }

    @Test
    public void infoPicType() {
        doTest("info;picType");
    }

    @Test
    public void infoGenre() {
        doTest("info;genre");
    }

    @Test
    public void configShow() {
        doTest("config");
    }

    @Test
    public void printHelp() {
        display(TmcHelp.HELP);
    }
    @Test
    public void printAlias() {
        display(TmcHelp.ALIASES);
    }





    private void doTest(String str, Object... params) {
        long start = System.currentTimeMillis();
        TmcArgs tmcArgs = super.parseArgs(strf(str, params), true);
        TmcEngine.execute(tmcArgs);
        long elapsed = System.currentTimeMillis() - start;
        display("\n%s", TmcDebug.toStringRecap(elapsed));
    }


}
