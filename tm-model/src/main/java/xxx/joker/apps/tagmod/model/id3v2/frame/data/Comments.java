package xxx.joker.apps.tagmod.model.id3v2.frame.data;


import org.apache.commons.lang3.StringUtils;
import xxx.joker.libs.javalibs.language.JkLanguage;

import static xxx.joker.libs.javalibs.utils.JkStrings.strf;

/**
 * Created by f.barbano on 26/02/2018.
 */
public class Comments implements IFrameData {

	private JkLanguage language;
	private String description = "";
	private String text = "";

	public Comments() {
	}

	public Comments(JkLanguage language, String description, String text) {
		this.language = language;
		this.description = description;
		this.text = text;
	}

	public JkLanguage getLanguage() {
		return language;
	}

	public void setLanguage(JkLanguage JkLanguage) {
		this.language = JkLanguage;
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
		if(!(frameData instanceof Comments)) {
			return false;
		}

		Comments other = (Comments) frameData;
		return language == other.language && description.equals(other.description);
	}

	@Override
	public String toStringInline() {
		return strf("%s, *%s*, *%s*",
			language.getLabel(),
			description,
			text
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lyrics)) return false;

		Comments c = (Comments) o;

		if (language != c.language) return false;
		if (!StringUtils.equalsIgnoreCase(description, c.description)) return false;
		return StringUtils.equalsIgnoreCase(text, c.text);
	}

	@Override
	public int hashCode() {
		int result = language != null ? language.hashCode() : 0;
		result = 31 * result + (description != null ? description.toLowerCase().hashCode() : 0);
		result = 31 * result + (text != null ? text.toLowerCase().hashCode() : 0);
		return result;
	}
}
