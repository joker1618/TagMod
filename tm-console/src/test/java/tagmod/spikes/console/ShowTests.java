package tagmod.spikes.console;

import org.junit.Before;
import org.junit.Test;
import xxx.joker.apps.tagmod.console.TmcEngine;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.apps.tagmod.console.workers.TmcInfo;
import xxx.joker.apps.tagmod.console.workers.TmcViewer;
import xxx.joker.apps.tagmod.model.facade.TagmodFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class ShowTests extends CommonTest {

    @Test
    public void showAttributesFile() throws IOException {
        String[] args = {"show", "files", "C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1989 Liberi liberi\\06 Liberi liberi.mp3"};
        TmcArgs tmcArgs = super.parseArgs(args);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }

    @Test
    public void showAttributesFolder() throws IOException {
        String[] args = {"show", "files", "C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1989 Liberi liberi"};
        TmcArgs tmcArgs = super.parseArgs(args);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }

    @Test
    public void showLyricsFile() throws IOException {
        String[] args = {"show", "lyrics", "files", "C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1989 Liberi liberi\\06 Liberi liberi.mp3"};
        TmcArgs tmcArgs = super.parseArgs(args);
        TmcEngine.execute(tmcArgs);
        display("\n\nEND");
    }

}
