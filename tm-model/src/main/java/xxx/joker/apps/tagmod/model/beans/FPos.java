package xxx.joker.apps.tagmod.model.beans;

/**
 * Created by f.barbano on 01/05/2018.
 */
public class FPos {

	private int begin;
	private int end;

	public FPos() {
	}

	public FPos(int begin, int end) {

		this.begin = begin;
		this.end = end;
	}

	public int getLength() {
		return end - begin;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
