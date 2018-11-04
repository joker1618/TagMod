package spikes.files;

import org.junit.Test;
import tagmod.spikes.console.CommonTest;
import xxx.joker.libs.core.datetime.JkTime;
import xxx.joker.libs.core.utils.JkFiles;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.format.DateTimeFormatter;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class WriteFile extends CommonTest  {

    @Test
    public void updateFile() throws Exception {
//        Path path = TAGMOD_FOLDER.resolve("tm-console\\src\\test\\resources\\song.mp3");
        Path path = TAGMOD_FOLDER.resolve("tm-console\\src\\test\\resources\\simpleFileUpdate.txt");
        BasicFileAttributes attrBefore = getAttributes(path);

        try(
                RandomAccessFile raf1 = new RandomAccessFile(path.toFile(), "rw");
                RandomAccessFile raf2 = new RandomAccessFile(path.toFile(), "rw");
                FileChannel ch1 = raf1.getChannel();
                FileChannel ch2 = raf2.getChannel()
        ) {
            ch2.position(10);
            display("CH1:%d\tCH2:%d\tSIZE:%d", ch1.position(), ch2.position(), ch1.size());

            display("Transfered %d bytes", ch1.transferTo(3, ch1.size(), ch2));

            ch1.position(2);
            raf1.write("INSERT".getBytes());
//            ch1.truncate(16);
            display("CH1:%d\tCH2:%d\tSIZE:%d", ch1.position(), ch2.position(), ch1.size());
        }

        BasicFileAttributes attrAfter = getAttributes(path);
        display("File %s", path.getFileName());
        printAttrDiff(attrBefore, attrAfter);

    }

    @Test
    public void rewriteFile() throws Exception {
        Path path = TAGMOD_FOLDER.resolve("tm-console\\src\\test\\resources\\simpleFileOverwrite.txt");
        BasicFileAttributes attrBefore = getAttributes(path);

        Files.deleteIfExists(path);
        JkFiles.writeFile(path, "RISCRITTO", true);

        path = TAGMOD_FOLDER.resolve("tm-console\\src\\test\\resources\\simpleFileOverwrite.txt");
        BasicFileAttributes attrAfter = getAttributes(path);
        display("File %s", path.getFileName());
        printAttrDiff(attrBefore, attrAfter);

    }

    private BasicFileAttributes getAttributes(Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
    }
    private void printAttrDiff(BasicFileAttributes before, BasicFileAttributes after) {
        display("%20s  %20s", strTime(before.creationTime()), strTime(before.lastModifiedTime()));
        display("%20s  %20s", strTime(after.creationTime()), strTime(after.lastModifiedTime()));
    }
    private String strTime(FileTime fileTime) {
        return JkTime.of(fileTime.toInstant()).getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
