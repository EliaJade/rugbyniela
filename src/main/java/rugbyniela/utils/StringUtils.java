package rugbyniela.utils;

import java.text.Normalizer;

public class StringUtils {
	public static String normalize(String value) {
		return value == null
				? null
				: Normalizer.normalize(value, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.toLowerCase()
				.trim()
				.replaceAll("\\s+", " ");
	}
}
