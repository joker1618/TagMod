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

import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public abstract class CommonTest {

    @Before
    public void init() {
        Path configPath = Paths.get("C:\\Users\\f.barbano\\IdeaProjects\\apps\\tagmod\\tm-console\\src\\main\\resources\\config\\tagmod.config");
        TmcConfig.loadConfigPath(configPath);
    }

    protected TmcArgs parseArgs(String[] args) {
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

}
