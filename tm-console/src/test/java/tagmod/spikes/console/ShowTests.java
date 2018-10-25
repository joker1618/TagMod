package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcHelp;
import xxx.joker.apps.tagmod.console.main.TmcEngine;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.main.TmcMain;

import java.io.IOException;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class ShowTests extends CommonTest {

    @Test
    public void show() {
        doTest("show;files;Metallica");
    }

    @Test
    public void describe() {
        doTest("describe;files;Vasco");
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





    private void doTest(String str) {
        TmcArgs tmcArgs = super.parseArgs(str);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }


}
