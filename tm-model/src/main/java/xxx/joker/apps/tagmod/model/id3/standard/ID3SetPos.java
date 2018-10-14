package xxx.joker.apps.tagmod.model.id3.standard;

import xxx.joker.libs.javalibs.utils.JkConverter;
import xxx.joker.libs.javalibs.utils.JkStrings;

/**
 * Created by f.barbano on 09/05/2018.
 */
public class ID3SetPos {

	private int index;
	private int total;

	public ID3SetPos(int index) {
		this(index, -1);
	}

    public ID3SetPos(int index, int total) {
        this.index = index;
        this.total = total;
    }

    public int getIndex() {
		return index;
	}
	public int getTotal() {
		return total;
	}

	public static ID3SetPos parse(String str) {
		String[] split = JkStrings.splitAllFields(str, "/");
		if(split.length != 1 && split.length != 2) 		return null;

		Integer num = JkConverter.stringToInteger(split[0]);
		if(num == null)		return null;

		if(split.length == 1)	return new ID3SetPos(num);

		Integer tot = JkConverter.stringToInteger(split[1]);
		return tot == null ? null : new ID3SetPos(num, tot);
	}
}