package xxx.joker.apps.tagmod.model.id3.enums;


import xxx.joker.libs.core.utils.JkFiles;
import xxx.joker.libs.core.utils.JkStreams;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by f.barbano on 15/05/2016.
 */
public enum MimeType {

	JPEG {
		@Override
		public String fileExtension() {
			return "jpg";
		}
		@Override
		public List<String> allowedExtensions() {
			return Arrays.asList("jpg", "jpeg");
		}
		@Override
		public String mimeType(int version) {
			return version == 2 ? "JPG" : "image/jpg";
		}
		@Override
		public List<String> allowedMimeTypes() {
			return Arrays.asList("JPG", "image/jpg", "image/jpeg");
		}
	},
	PNG {
		@Override
		public String fileExtension() {
			return "png";
		}
		@Override
		public List<String> allowedExtensions() {
			return Collections.singletonList("png");
		}
		@Override
		public String mimeType(int version) {
			return version == 2 ? "PNG" : "image/png";
		}
		@Override
		public List<String> allowedMimeTypes() {
			return Arrays.asList("PNG", "image/png");
		}
	}
	;

	public abstract String fileExtension();
	public abstract List<String> allowedExtensions();
	public abstract String mimeType(int version);
	public abstract List<String> allowedMimeTypes();

	public static MimeType getByMimeType(String mimeType) {
		return Arrays.stream(values())
				.filter(mt -> mt.allowedMimeTypes().contains(mimeType))
				.findAny()
				.orElse(null);
	}

	public static MimeType getByExtension(Path filePath) {
        String extension = JkFiles.getExtension(filePath);
        return Arrays.stream(values())
				.filter(mt -> !JkStreams.filter(mt.allowedExtensions(), extension::equalsIgnoreCase).isEmpty())
				.findAny()
				.orElse(null);
	}
}
