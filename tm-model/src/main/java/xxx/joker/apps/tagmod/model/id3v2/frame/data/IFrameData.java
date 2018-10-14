package xxx.joker.apps.tagmod.model.id3v2.frame.data;

/**
 * Created by f.barbano on 26/02/2018.
 */
public interface IFrameData {

	boolean isDataDuplicated(IFrameData frameData);

	String toStringInline();

}
