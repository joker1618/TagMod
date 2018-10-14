package xxx.joker.apps.tagmod.console.main;

import xxx.joker.apps.tagmod.console.TmcEngine;
import xxx.joker.apps.tagmod.console.args.TmcArgType;
import xxx.joker.apps.tagmod.console.args.TmcArgs;
import xxx.joker.apps.tagmod.console.args.TmcCmd;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.libs.argsparser.InputParserImpl;
import xxx.joker.libs.argsparser.exception.InputParserException;
import xxx.joker.libs.argsparser.exception.InputValueException;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkStrings;

import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class TmcMain {

	public static void main(String[] args) {
	    display(TmcConfig.getMaxFilenameWidth()+"");
//        Path configPath = Paths.get("C:\\Users\\f.barbano\\IdeaProjects\\apps\\tagmod\\tm-console\\src\\main\\resources\\config\\tagmod.config");
//	    TmcConfig.loadConfigPath(configPath);
		// review delete mock
		String strInput = "describe;files;C:\\Users\\f.barbano\\Desktop\\finalMusic\\Vasco Rossi\\1981 Siamo solo noi\\07 Incredibile romantica.mp3";
//		String strInput = "help";
//		String strInput = "distinct;cover;files;C:\\Users\\f.barbano\\Music\\Oasis\\album\\1997 Be Here Now\\03 Magic Pie.mp3";
		args = JkStrings.splitAllFields(strInput, ";");

		TmcArgs tmArgs = new TmcArgs();
		InputParserImpl parser = new InputParserImpl(tmArgs, TmcArgType.class, TmcCmd.class, JkFiles.getLauncherPath(TmcMain.class));
		try {
			parser.parse(args);
		} catch (InputValueException | InputParserException e) {
			display(e.getMessage());
			System.exit(1);
		}

		TmcEngine.execute(tmArgs);
	}
}
