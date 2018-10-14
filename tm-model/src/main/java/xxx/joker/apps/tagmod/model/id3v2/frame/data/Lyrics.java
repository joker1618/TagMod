package xxx.joker.apps.tagmod.model.id3v2.frame.data;

import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.javalibs.language.JkLanguage;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class Lyrics implements IFrameData {

	private JkLanguage language;
	private String description = "";
	private String text = "";

	public Lyrics() {
	}

	public Lyrics(JkLanguage language, String description, String text) {
		this.language = language;
		this.description = description;
		this.text = text;
	}

	public JkLanguage getLanguage() {
		return language;
	}

	public void setLanguage(JkLanguage language) {
		this.language = language;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return toStringInline();
	}

	@Override
	public boolean isDataDuplicated(IFrameData frameData) {
		if(!(frameData instanceof Lyrics)) {
			return false;
		}

		Lyrics other = (Lyrics) frameData;
		return language == other.language && description.equals(other.description);
	}

	@Override
	public String toStringInline() {
		return strf("%s, *%s*, size=%d",
			language.getLabel(),
			description,
			text.length()
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lyrics)) return false;

		Lyrics lyrics = (Lyrics) o;

		if (language != lyrics.language) return false;
		if (!StringUtils.equalsIgnoreCase(description, lyrics.description)) return false;
		return StringUtils.equalsIgnoreCase(text, lyrics.text);
	}

	@Override
	public int hashCode() {
		int result = language != null ? language.hashCode() : 0;
		result = 31 * result + (description != null ? description.toLowerCase().hashCode() : 0);
		result = 31 * result + (text != null ? text.toLowerCase().hashCode() : 0);
		return result;
	}
}
