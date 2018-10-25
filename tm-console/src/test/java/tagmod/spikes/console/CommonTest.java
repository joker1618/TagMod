package tagmod.spikes.console;

import org.junit.Before;
import xxx.joker.apps.tagmod.console.args.TmcArgType;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.apps.tagmod.console.main.TmcMain;
import xxx.joker.libs.argsparser.InputParserImpl;
import xxx.joker.libs.argsparser.exception.InputParserException;
import xxx.joker.libs.argsparser.exception.InputValueException;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStrings;

import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public abstract class CommonTest {

    protected static final Path TAGMOD_FOLDER = Paths.get(System.getProperty("user.home")).resolve("IdeaProjects\\APPS\\tagmod");
    private static final Path configPath = TAGMOD_FOLDER.resolve("config\\tagmod.config");

    @Before
    public void init() {
        TmcConfig.loadConfigPath(configPath);
    }

    protected TmcArgs parseArgs(String[] argsString) {
        return null;
    }
    protected TmcArgs parseArgs(String argsString) {
        String[] args = parseArgsString(argsString);
        try {
            TmcArgs tmArgs = new TmcArgs();
            InputParserImpl parser = new InputParserImpl(tmArgs, TmcArgType.class, TmcCmd.class, JkFiles.getLauncherPath(TmcMain.class));
            parser.parse(args);
            return tmArgs;

        } catch (InputValueException | InputParserException e) {
            display(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String[] parseArgsString(String str) {
        String[] elems = JkStrings.splitAllFields(str, ";", true);
        String[] toRet = new String[elems.length];
        boolean addPrefix = false;
        for(int i = 0; i < elems.length; i++) {
            toRet[i] = addPrefix ? TAGMOD_FOLDER.resolve("tagmod-samples").resolve(elems[i]).toString() : elems[i];
            addPrefix |= elems[i].equals("files") || elems[i].equals("diff");
        }
        return toRet;
    }

}
