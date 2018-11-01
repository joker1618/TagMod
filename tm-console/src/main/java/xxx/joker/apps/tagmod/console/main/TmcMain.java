package xxx.joker.apps.tagmod.console.main;

import xxx.joker.apps.tagmod.console.args.TmcArgType;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.stuff.TmcDebug;
import xxx.joker.libs.argsparser.InputParserImpl;
import xxx.joker.libs.javalibs.datetime.JkTime;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.util.Arrays;
import java.util.stream.Collectors;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class TmcMain {

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            String[] fixedArgs = Arrays.stream(args).filter(arg -> !"-debug".equalsIgnoreCase(arg)).toArray(String[]::new);

            // Parse console input
            TmcArgs tmArgs = new TmcArgs();
            InputParserImpl parser = new InputParserImpl(tmArgs, TmcArgType.class, TmcCmd.class, JkFiles.getLauncherPath(TmcMain.class));
            parser.parse(fixedArgs);

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
