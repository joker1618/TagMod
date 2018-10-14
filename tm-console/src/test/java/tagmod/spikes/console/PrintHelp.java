package tagmod.spikes.console;

import org.junit.Test;
import xxx.joker.apps.tagmod.console.args.TmcHelp;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class PrintHelp {

    @Test
    public void printHelp() {
        display(TmcHelp.HELP);
    }
}
