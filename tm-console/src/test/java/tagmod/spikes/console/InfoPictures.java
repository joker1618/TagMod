package tagmod.spikes.console;

import org.junit.Before;
import org.junit.Test;
import xxx.joker.apps.tagmod.console.common.TmcConfig;
import xxx.joker.apps.tagmod.console.workers.TmcInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class InfoPictures extends CommonTest {

    @Test
    public void printPictureTypes() {
        display(TmcInfo.toStringPictureTypes());
    }
    
}
