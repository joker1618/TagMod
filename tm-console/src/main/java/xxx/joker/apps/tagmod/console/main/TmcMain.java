package xxx.joker.apps.tagmod.console.main;

import xxx.joker.apps.tagmod.console.args.TmcArgType;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.stuff.TmcDebug;
import xxx.joker.libs.argsparser.InputParserImpl;
import xxx.joker.libs.core.datetime.JkTime;
import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.util.Arrays;
import java.util.stream.Collectors;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class TmcMain {

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            String[] fixedArgs = Arrays.stream(args).filter(arg -> !"-debug".equalsIgnoreCase(arg)).toArray(String[]::new);

            // Parse console input
            InputParserImpl parser = new InputParserImpl(TmcArgs.class, TmcArgType.class, TmcCmd.class, JkFiles.getLauncherPath(TmcMain.class));
            TmcArgs tmArgs = parser.parse(fixedArgs);

            // Execute command
            TmcEngine.execute(tmArgs);

            if(fixedArgs.length != args.length) {
                display("\n%s", TmcDebug.toStringRecap(System.currentTimeMillis() - startTime));
            }

        } catch (Exception e) {
            display("%s", e);
            display("User input:  tagmod %s", JkStreams.join(Arrays.asList(args), " ", s -> s.contains(" ") ? "\""+s+"\"" : s));
            display("Test cmd -> [%s]", JkStreams.join(Arrays.asList(args), ";"));
        }
    }
}
