package tagmod.spikes.console;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import xxx.joker.libs.core.datetime.JkTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xxx.joker.libs.core.utils.JkConsole.display;

public class Tests extends CommonTest {

	@Test
	public void test() throws IOException {
        String str = "1986 giovanni maledetto  [CD 2]";
        Pattern pattern = Pattern.compile("^([0-9]{4}) (.*) .?CD *([0-9]).?$");
        Matcher matcher = pattern.matcher(str);
        display("%s\t%s", str, matcher.matches());
        display("  %s", matcher.group(1));
        display("  %s", matcher.group(2));
        display("  %s", matcher.group(3));

	}
	@Test
	public void aatest() throws IOException {
        Path path = TAGMOD_FOLDER.resolve("tm-console\\src\\test\\resources\\simpleFileUpdate.txt");
        BasicFileAttributes fattrs = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        display(ToStringBuilder.reflectionToString(fattrs, ToStringStyle.MULTI_LINE_STYLE));
        display("'%s'", fattrs.creationTime().toString());
        display("'"+fattrs.creationTime().toInstant()+"'");
        display("'"+LocalDateTime.ofInstant(fattrs.creationTime().toInstant(), ZoneId.systemDefault()) +"'");
        display("'"+fattrs.creationTime().toMillis()+"'");

//        display("Now:    %s", LocalDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        String str = fattrs.creationTime().toString();
//        Math.minstr.indexOf('.')+4, str.length())
        display("Parsed: %s", LocalDateTime.parse(fattrs.creationTime().toString().replaceAll("Z$", ""), DateTimeFormatter.ISO_DATE_TIME));
        display("Parsed: %s", LocalDateTime.parse(fattrs.creationTime().toString().replaceAll("Z$", ""), DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ISO_LOCAL_TIME));
        display(JkTime.of(fattrs.creationTime().toInstant()).toStringElapsed(true).replaceAll(".* ", ""));
        display(JkTime.of(fattrs.creationTime().toInstant()).getLocalDateTime().toString());
        display(strTime(Files.readAttributes(path, BasicFileAttributes.class).lastAccessTime()));

    }
    private void printAttrDiff(BasicFileAttributes before, BasicFileAttributes after) {
        display("%20s  %20s  %20s", strTime(before.creationTime()), strTime(before.lastAccessTime()), strTime(before.lastModifiedTime()));
        display("%20s  %20s  %20s", strTime(after.creationTime()), strTime(after.lastAccessTime()), strTime(after.lastModifiedTime()));
    }
    private String strTime(FileTime fileTime) {
        return JkTime.of(fileTime.toInstant()).getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
