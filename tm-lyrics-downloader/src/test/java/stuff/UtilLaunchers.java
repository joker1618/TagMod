package stuff;

import common.TestUtil;
import org.junit.Test;
import xxx.joker.libs.javalibs.utils.JkFiles;
import xxx.joker.libs.javalibs.utils.JkWeb;

import java.io.IOException;
import java.nio.file.Path;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;

public class UtilLaunchers {

    @Test
    public void getResource() throws IOException {
        String resourceURL = "https://goo.gl/images/pWGQFj";
        String subPath = "various/googleImage.jpg";
        Path outPath = TestUtil.RESOURCES_TEST_FOLDER.resolve(subPath);
        byte[] bytes = JkWeb.downloadResource(resourceURL);
        JkFiles.writeFile(outPath, bytes, true);
        display("Saved resource %s [from %s]", subPath, resourceURL);
    }
}
