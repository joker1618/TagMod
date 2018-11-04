package xxx.joker.apps.tagmod.model.id3.standard;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.core.utils.JkConverter;
import xxx.joker.libs.core.utils.JkStrings;

/**
 * Created by f.barbano on 09/05/2018.
 */
public class ID3SetPos {

	private int index;
	private Integer total;

	public ID3SetPos(int index) {
        this.index = index;
        this.total = null;
	}

    public ID3SetPos(int index, Integer total) {
        this.index = index;
        this.total = total;
    }

    public int getIndex() {
		return index;
	}
	public Integer getTotal() {
		return total;
	}
    public void setTotal(Integer total) {
        this.total = total;
    }

    public static ID3SetPos parse(String str) {
	    if(StringUtils.isBlank(str)) {
	        return null;
        }

		String[] split = JkStrings.splitAllFields(str, "/");
		if(split.length != 1 && split.length != 2) 		return null;

		Integer num = JkConverter.stringToInteger(split[0]);
		if(num == null)		return null;

		if(split.length == 1)	return new ID3SetPos(num);

		Integer tot = JkConverter.stringToInteger(split[1]);
		return tot == null ? null : new ID3SetPos(num, tot);
	}

	@Override
	public String toString() {
	    return index + (total == null ? "" : "/" + total);
    }
}
