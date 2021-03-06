package xxx.joker.apps.tagmod.console.args;

import xxx.joker.libs.argsparser.design.annotation.Cmd;
import xxx.joker.libs.argsparser.design.classType.InputCommand;
import xxx.joker.libs.argsparser.functions.ArgsCheck;
import xxx.joker.libs.argsparser.functions.ArgsParse;
import xxx.joker.libs.argsparser.model.CmdOption;
import xxx.joker.libs.argsparser.model.CmdParam;
import xxx.joker.libs.core.utils.JkConverter;

import java.nio.file.Path;
import java.util.List;

import static xxx.joker.apps.tagmod.console.args.TmcArgType.*;

public enum TmcCmd implements InputCommand {

	@Cmd
	CMD_SHOW(
		new CmdParam(new CmdOption(SHOW)),
		new CmdParam(false, new CmdOption(ATTRIBUTE)),
		new CmdParam(false, new CmdOption(AUDIO)),
		new CmdParam(false, new CmdOption(TAGMOD)),
		new CmdParam(false, new CmdOption(TAGv1)),
		new CmdParam(false, new CmdOption(TAGv2)),
		new CmdParam(false, new CmdOption(SIZE)),
		new CmdParam(false, new CmdOption(ALL)),
		new CmdParam(new CmdOption(FILES,
			ArgsParse.windowsPathFormat(),
			TmcArgFunction.expandPaths(),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_SHOW_LYRICS(
		new CmdParam(new CmdOption(SHOW)),
		new CmdParam(new CmdOption(LYRICS, Boolean.class)),
		new CmdParam(new CmdOption(FILES,
			ArgsParse.windowsPathFormat(),
			TmcArgFunction.expandPaths(),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_RECOVER(
		new CmdParam(new CmdOption(RECOVER)),
		new CmdParam(new CmdOption(FILES,
			ArgsParse.windowsPathFormat(),
			TmcArgFunction.expandPaths(),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_CHECK(
		new CmdParam(new CmdOption(CHECK)),
		new CmdParam(new CmdOption(FILES,
			ArgsParse.windowsPathFormat(),
			TmcArgFunction.expandPaths(),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_SUMMARY(
		new CmdParam(new CmdOption(SUMMARY)),
		new CmdParam(false, new CmdOption(TABLE)),
		new CmdParam(new CmdOption(FILES,
			ArgsParse.windowsPathFormat(),
			TmcArgFunction.expandPaths(),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_DIFF(
		new CmdParam(new CmdOption(DIFF,
			ArgsParse.windowsPathFormat(),
			ArgsCheck.numValuesExpected(2),
			TmcArgFunction.validatePaths())
		)
	),

	@Cmd
	CMD_INFO(
		new CmdParam(new CmdOption(INFO)),
		new CmdParam(new CmdOption(PIC_TYPE), new CmdOption(GENRE, Boolean.class))
	),

	@Cmd
	CMD_CONFIG(new CmdParam(new CmdOption(CONFIG))),

    @Cmd
    CMD_EXPORT(
        new CmdParam(new CmdOption(EXPORT)),
        new CmdParam(new CmdOption(PICTURES), new CmdOption(LYRICS, Boolean.class)),
        new CmdParam(new CmdOption(FILES,
                ArgsParse.windowsPathFormat(),
                TmcArgFunction.expandPaths(),
                TmcArgFunction.validatePaths())
        )
    ),

	@Cmd
	CMD_EDIT(
			new CmdParam(new CmdOption(EDIT)),
			new CmdParam(false, new CmdOption(CLEAR)),
			new CmdParam(false, new CmdOption(TITLE, String.class)),
			new CmdParam(false, new CmdOption(ARTIST, String.class)),
			new CmdParam(false, new CmdOption(ALBUM, String.class)),
			new CmdParam(false, new CmdOption(YEAR, String.class, TmcArgFunction.validateYear())),
			new CmdParam(false, new CmdOption(TRACK, String.class, TmcArgFunction.validateSetPos(true))),
			new CmdParam(false, new CmdOption(GENRE, String.class, null, TmcArgFunction.fixGenreName(), TmcArgFunction.validateGenre())),
			new CmdParam(false, new CmdOption(CD_POS, String.class, TmcArgFunction.validateSetPos(false))),
			new CmdParam(false, new CmdOption(COVER, String.class,
					ArgsParse.windowsPathFormat(),
					TmcArgFunction.validateCoverPath()
			)),
			new CmdParam(false, new CmdOption(LYRICS, String.class,
					ArgsParse.windowsPathFormat(),
					TmcArgFunction.validateLyricsPath())
			),
			new CmdParam(false, new CmdOption(ENCODING, TmcArgFunction.validateEncoding())),
			new CmdParam(false, new CmdOption(PADDING, ArgsCheck.intGE(0))),
			new CmdParam(false, new CmdOption(VERSION, TmcArgFunction.validateVersion())),
			new CmdParam(false, new CmdOption(NO_SIGN)),
			new CmdParam(new CmdOption(FILES,
					ArgsParse.windowsPathFormat(),
					TmcArgFunction.expandPaths(),
					TmcArgFunction.validatePaths()
			))
	),

	@Cmd
	CMD_EDIT_AUTO_ALL(
			new CmdParam(new CmdOption(EDIT)),
			new CmdParam(new CmdOption(AUTO_ALL)),
			new CmdParam(false, new CmdOption(CLEAR)),
			new CmdParam(false, new CmdOption(ARTIST, String.class)),
			new CmdParam(false, new CmdOption(GENRE, String.class, null, TmcArgFunction.fixGenreName(), TmcArgFunction.validateGenre())),
			new CmdParam(false, new CmdOption(ENCODING, TmcArgFunction.validateEncoding())),
			new CmdParam(false, new CmdOption(PADDING, ArgsCheck.intGE(0))),
			new CmdParam(false, new CmdOption(VERSION, TmcArgFunction.validateVersion())),
			new CmdParam(false, new CmdOption(NO_SIGN)),
			new CmdParam(new CmdOption(FILES,
					ArgsParse.windowsPathFormat(),
					TmcArgFunction.expandPaths(),
					TmcArgFunction.validatePaths()
			))
	),

	@Cmd
	CMD_TEST_OUTPUT_FORMATS(
		new CmdParam(new CmdOption(TEST)),
		new CmdParam(new CmdOption(OUTPUT_FORMATS)),
		new CmdParam(false, new CmdOption(CLEAR)),
		new CmdParam(false, new CmdOption(TITLE, String.class)),
		new CmdParam(false, new CmdOption(ARTIST, String.class)),
		new CmdParam(false, new CmdOption(ALBUM, String.class)),
		new CmdParam(false, new CmdOption(YEAR, String.class, TmcArgFunction.validateYear())),
		new CmdParam(false, new CmdOption(TRACK, String.class, TmcArgFunction.validateSetPos(true))),
		new CmdParam(false, new CmdOption(GENRE, String.class, null, TmcArgFunction.fixGenreName(), TmcArgFunction.validateGenre())),
		new CmdParam(false, new CmdOption(CD_POS, String.class, TmcArgFunction.validateSetPos(false))),
		new CmdParam(false, new CmdOption(COVER, String.class,
                ArgsParse.windowsPathFormat(),
                TmcArgFunction.validateCoverPath()
        )),
		new CmdParam(false, new CmdOption(LYRICS, String.class,
				ArgsParse.windowsPathFormat(),
				TmcArgFunction.validateLyricsPath()
		)),
        new CmdParam(false, new CmdOption(ENCODING, TmcArgFunction.validateEncoding())),
        new CmdParam(false, new CmdOption(PADDING, ArgsCheck.intGE(0))),
        new CmdParam(false, new CmdOption(VERSION, TmcArgFunction.validateVersion())),
        new CmdParam(false, new CmdOption(NO_SIGN)),
		new CmdParam(new CmdOption(FILES,
                ArgsParse.windowsPathFormat(),
                TmcArgFunction.expandPaths(),
                TmcArgFunction.validatePaths()
		))
	),

	@Cmd
	CMD_DELETE(
		new CmdParam(new CmdOption(DELETE)),
		new CmdParam(false, new CmdOption(TITLE, Boolean.class)),
		new CmdParam(false, new CmdOption(ARTIST, Boolean.class)),
		new CmdParam(false, new CmdOption(ALBUM, Boolean.class)),
		new CmdParam(false, new CmdOption(YEAR, Boolean.class)),
		new CmdParam(false, new CmdOption(TRACK, Boolean.class)),
		new CmdParam(false, new CmdOption(GENRE, Boolean.class)),
		new CmdParam(false, new CmdOption(CD_POS, Boolean.class)),
		new CmdParam(false, new CmdOption(COVER, Boolean.class)),
		new CmdParam(false, new CmdOption(PICTURES)),
		new CmdParam(false, new CmdOption(LYRICS, Boolean.class)),
		new CmdParam(false, new CmdOption(OTHER_LYRICS)),
		new CmdParam(new CmdOption(FILES,
                ArgsParse.windowsPathFormat(),
                TmcArgFunction.expandPaths(),
                TmcArgFunction.validatePaths(
		)))
	),

	@Cmd
	CMD_HELP(
		new CmdParam(false, new CmdOption(HELP))
	),


	;

	public static final String AUTO_VALUE = "_auto";

	private List<CmdParam> paramList;

	TmcCmd(CmdParam... params) {
		this.paramList = JkConverter.toArrayList(params);
	}

	@Override
	public List<CmdParam> paramList() {
		return paramList;
	}

}
