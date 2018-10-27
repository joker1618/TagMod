package xxx.joker.apps.tagmod.model.beans;

/**
 * Created by f.barbano on 01/05/2018.
 */
public class FPos {

	private long begin;
	private long end;

	public FPos() {
	}

    public FPos(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getLength() {
        return end - begin;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
