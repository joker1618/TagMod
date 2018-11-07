package common;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtil {

    public static final Path HOME = Paths.get(System.getProperty("user.home"));
    public static final Path RESOURCES_TEST_FOLDER = HOME.resolve("IdeaProjects\\APPS\\tagmod\\tm-lyrics-downloader\\src\\test\\resources");
}
