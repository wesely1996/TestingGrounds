import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDebugger {

	private static final Pattern REGEX = Pattern.compile(
//			"tekst" 
//			"\\"
//			"\\\\"
//			"."
//			"\\."
//			"ana"--
//			"(?i)ana"--
//			"b*"
//			"ab?"
//			"a."
//			"ab{2,4}"
//			"b{2,}"
			"[abAB]+"
//			"[^aeiou]"
//			"[^0-9A-Za-z]"
//			"\\d"
//			"^a"
//			"a$"
//			"\\ba"
//			"a.*a"
//			"a.*?a"
//			"a.*+a"
//			" (...) (...) "
//			"(\\w{3})\\s{1}(\\w{3})"
//			"\\bt.*?.?\\b"
//			"\\b(t).*?(.?)\\b"
//			"\\b(?<prvoslovo>t).*?(?<poslednjeslovo>.?)\\b"
//			"(ab|ic)a"
//			"(?:ab|ic)a"
//			"( . )"
//			"\\Q( . )\\E"
//			"\\b([A-Z]{2}) (\\d{3}-[A-Z]{2})\\b"
	);

	private static final String[] INPUT = {
			"Neki kratak tekst u kojem   se rec tekst javlja vise puta. Takodje, tekst sadrzi i specijalne znake poput poput tacke i zagrada: ( . ) % $ [ , ] \\ /",
			"Ana voli Milovana!",
			"abba baba ceca caca 12345 cica aca ica aba abbba abbbbba",
			"NS 123-AB | 061/234-56-78 | +381 21 123 456 | 194 | noreply@somedomain.com | studentska@pmf.uns.ac.rs | ime.prezime@domen.rs | 123-456",
	};

	public static void main(String[] args) {
		for (String input : INPUT) {
			debug(input);
		}
	}

	private static void debug(String input) {
		Matcher matcher = REGEX.matcher(input);
		if (matcher.matches()) {
			System.out.println(input.replaceAll(".", "_"));
			System.out.println(input);
			System.out.println(input.replaceAll(".", "="));
		} else if (matcher.find(0)) {
			StringBuilder builder = new StringBuilder(input.length());
			for (int i = 0; i < input.length(); i++) {
				builder.append(' ');
			}
			StringBuilder builder2 = new StringBuilder(input.length());
			for (int i = 0; i < input.length(); i++) {
				builder2.append(' ');
			}
			do {
				for (int i = matcher.start(); i < matcher.end(); i++) {
					builder.setCharAt(i, '_');
				}
				for (int j = 1; j <= matcher.groupCount(); j++) {
					for (int i = matcher.start(j); i < matcher.end(j); i++) {
						builder2.setCharAt(i, (char) (j + '0'));
					}
				}
			} while (matcher.find());
			System.out.println(builder);
			System.out.println(input);
			System.out.println(builder2);
		} else {
			System.out.println();
			System.out.println(input);
			System.out.println();
		}
	}
}
