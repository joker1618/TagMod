package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.main.TmcEngine;
import xxx.joker.libs.core.utils.JkBytes;

import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.core.utils.JkConsole.display;
import static xxx.joker.libs.core.utils.JkStrings.strf;

public class EditTests extends CommonTest {

    @Test
    public void editClear() {
        doTest("edit;clear;files;C:\\Users\\feder\\Desktop\\tmtests\\1985 Cosa succede in città\\04 Toffee.mp3");
    }

    @Test
    public void retry() {
        doTest("edit;files;%s", HOME.resolve("Desktop\\jk music\\Vasco Rossi\\2tagmod\\common\\casoX.mp3"));
//        doTest("delete;title;track;files;%s", HOME.resolve("Desktop\\tmtests\\01 Bollicine.mp3"));
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

    @Test
    public void diffFiles() {
        Path pa = Paths.get("C:\\Users\\feder\\Desktop\\jk music\\Vasco Rossi\\2tagmod\\common\\casoX.mp3.persistChanges");
        Path pb = Paths.get("C:\\Users\\feder\\Desktop\\jk music\\Vasco Rossi\\2tagmod\\common\\casoX.mp3.checkValidity");

        byte[] ba = JkBytes.getBytes(pa);
        byte[] bb = JkBytes.getBytes(pb);

        int counter = 0;
        for(int i = 0; i < ba.length && counter < 10; i++) {
            if(ba[i]!=bb[i]) {
                display("POS %d", i);
                counter++;
            }
        }
    }






    private void doTest(String str, Object... params) {
        TmcArgs tmcArgs = super.parseArgs(strf(str, params), false);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }


}
