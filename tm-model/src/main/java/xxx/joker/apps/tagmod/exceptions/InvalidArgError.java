package xxx.joker.apps.tagmod.exceptions;

/**
 * Created by f.barbano on 08/01/2018.
 */

public class InvalidArgError extends RuntimeException {

	public InvalidArgError(Throwable t, String mex, Object... params) {
		super(String.format(mex, params), t);
	}
	public InvalidArgError(String format, Object... params) {
		super(String.format(format, params));
	}
	public InvalidArgError(Throwable t) {
		super(t);
	}

}