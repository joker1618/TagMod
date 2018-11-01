package xxx.joker.apps.tagmod.stuff;

import org.apache.commons.lang3.tuple.Pair;
import xxx.joker.libs.javalibs.datetime.JkTime;
import xxx.joker.libs.javalibs.format.JkColumnFmtBuilder;
import xxx.joker.libs.javalibs.utils.JkStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static xxx.joker.libs.javalibs.utils.JkConsole.display;
import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

public class TmcDebug {

    private static List<Pair<String,Long>> pairs = new ArrayList<>();
    private static Long previous;


    public static void addTime(String eventName) {
        addTime(eventName, false);
    }
    public static void addTime(String eventName, boolean display) {
        long ms = System.currentTimeMillis();
        long elapsed = previous == null ? 0L : ms-previous;
        pairs.add(Pair.of(eventName, elapsed));
        previous = ms;
        if(display) {
            String elapsedString = JkTime.of(ms - previous).toStringElapsed(true);
            display("%s\t%s\t%d", eventName, elapsedString, elapsed);
        }
    }

    public static void showRecap(long totalElapsed) {
        JkColumnFmtBuilder b = new JkColumnFmtBuilder();
        b.addLines(strf("Total:|%s|%d", JkTime.of(totalElapsed).toStringElapsed(true), totalElapsed));
        Map<String, List<Long>> eventMap = JkStreams.toMap(pairs, Pair::getKey, Pair::getValue);
        eventMap.forEach((k,v) -> {
            long sum = v.stream().mapToLong(l -> l).sum();
            double perc = (double)sum/totalElapsed;
            b.addLines(strf("%s:|%s|%d|%.2f%s", k, JkTime.of(sum).toStringElapsed(true), sum, perc, "%"));
        });
        display("%s", b.toString("|", 2));
    }
}
