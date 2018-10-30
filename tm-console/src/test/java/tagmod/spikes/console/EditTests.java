package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.main.TmcEngine;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class EditTests extends CommonTest {

    @Test
    public void editClear() {
        doTest("edit;clear;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }

    @Test
    public void retry() {
        doTest("edit;title;maronna;genre;186;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("edit;title;maronna;genre;186;noSign;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("edit;album;ALBUM;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
//        doTest("edit;artist;fe;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
//        doTest("edit;artist;nome artista lungo per aumentare size tag;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
//        doTest("edit;cover;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\cov.jpg;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }


    @Test
    public void show() {
//        doTest("describe;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
//        doTest("diff;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee_ORIG.mp3");
        doTest("describe;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\of");
//        doTest("describe;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\of\\04 Toffee-v2-iso-unsync.mp3");
//        doTest("show;lyrics;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }



    @Test
    public void testOutputFormats() {
        doTest("test;-of;" +
                "title;_auto;track;_auto;album;ALBIIIII;genre;87;year;1618;" +
                "files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
//        doTest("edit;album;ALBIIIII;genre;87;year;1618;title;titolo;track;2;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\of");
//        doTest("edit;title;titolo;track;2;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\of");
    }






    private void doTest(String str, Object... params) {
        TmcArgs tmcArgs = super.parseArgs(strf(str, params), false);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }


}