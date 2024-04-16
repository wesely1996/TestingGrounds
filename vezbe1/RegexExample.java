import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExample {

	public static void main(String[] args) {

		String tekst = "Neki kratak tekst. Ukratko, u njemu se rec tekst javlja vise puta.";
		System.out.println(tekst);
		System.out.println();

		String[] reci = tekst.split(" ");
		for (String rec : reci) {
			System.out.println(rec);
		}
		System.out.println();

		String tekst2 = new String("Neki kratak tekst. Ukratko, u njemu se rec tekst javlja vise puta.");
		if (tekst == tekst2) {
			System.out.println("Stringovi su isti objekat");
		} else {
			System.out.println("Stringovi NISU isti objekat");
		}
		if (tekst.equals(tekst2)) {
			System.out.println("Stringovi su jednaki");
		} else {
			System.out.println("Stringovi NISU jednaki");
		}
		System.out.println();

		String rec = "kratko";
		if (tekst.contains(rec)) {
			System.out.println("Sadrzi rec");
		} else {
			System.out.println("NE sadrzi rec");
		}
		System.out.println();

		tekst = tekst.replaceAll("a", "e");
		System.out.println(tekst);
		tekst = tekst.replaceAll(".", "!"); // \, ^, $, ., |, ?, *, +, (, ), [, {
		System.out.println(tekst);
		System.out.println();

		tekst = "Neki tekst";
		Pattern pattern = Pattern.compile("e");
		Matcher matcher = pattern.matcher(tekst);
		while (matcher.find()) {
			int pos = matcher.start() + 1;
			System.out.println(tekst.substring(0, pos) + " - " + pos);
		}
		System.out.println();
		
		Pattern pattern2 = Pattern.compile("Neki");
		Matcher matcher2 = pattern2.matcher(tekst);
		System.out.print("Da li se poklapa ceo string? ");
		if (matcher2.matches()) {
			System.out.println("Da");
		} else {
			System.out.println("Ne");
		}

		Pattern pattern3 = Pattern.compile("Neki");
		Matcher matcher3 = pattern3.matcher(tekst);
		System.out.print("Da li je na pocetku stringa? ");
		if (matcher3.lookingAt()) {
			System.out.println("Da");
		} else {
			System.out.println("Ne");
		}

	}
}
