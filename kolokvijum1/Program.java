/*
 * Prvi kolokvijum
 * ===============
 * 
 * Napisati Java aplikaciju koja pomocu tokova podataka i lambda izraza
 * obradjuje podatke o fiskalnim racunima izdatim od strane jedne mlekare.
 * 
 * Data je klasa Racun kojom se prestavljaju fiskalni racuni. Svaki racun ima
 * svoj redni broj, datum i vreme kada je izdat, listu stavki na racunu, kao i
 * koliko je gotovine uplaceno kada je racun placen.
 * 
 * Data je i klasa Stavka cije instance predstavljaju stavke racuna, a svaka
 * stavka se sastoji od imena proizvoda, kolicine tog proizvoda, cene po
 * jedinici mere i poreske stope (predstavljene nabrojivim tipom).
 * 
 * Prvi deo (5 poena)
 * ------------------
 * 
 * Dat je tok stringova u vidu metoda Racuni.stringStream(). U njemu je svaki
 * racun predstavljen jednim stringom u XML formatu. Za detalje o formatu
 * pokrenuti program i pogledati kako izgleda svaki od stringova.
 * 
 * Pretvoriti dati tok stringova u tok Racun objekata i ispisati ih.
 * 
 * Drugi deo (5 poena)
 * -------------------
 * 
 * Implementirati metod long ispodHiljadu(int mesec, int godina),
 * pozvati ga u glavnom programu i ispisati rezultat.
 * 
 * Metod vraca ukupan broj racuna, za zadati mesec zadate godine, za koje je
 * uplaceno strogo manje od hiljadu dinara.
 * 
 * Treci deo (5 poena)
 * -------------------
 * 
 * Implementirati metod List<Racun> sortiraniPoVisini(int dan, int mesec, int godina),
 * pozvati ga u glavnom programu i ispisati rezultat.
 * 
 * Metod vraca listu racuna izadtih zadatog dana, sortiranu rastuce po visini
 * racuna.
 * 
 * Cetvrti deo (5 poena)
 * ---------------------
 * 
 * Za svaki proizvod ispisati koliko je puta prodat (ukupan broj racuna na
 * kojima se nalazi taj proizvod), na sledeci nacin:
 * 
 *      Proizvod | Prodato puta
 * --------------+--------------
 *    Mleko 3,2% |         1245
 *    Mleko 2,8% |        12876
 *  Jogurt 1,5kg |         3762
 *              ...
 * 
 * Peti deo (5 poena)
 * ------------------
 * 
 * Za svaku vrstu tvrdog sira, ispisati koliko je kilograma prodato u svakom
 * mesecu 2019. godine, u tabelarnom obliku na sledeci nacin:
 * 
 * Sir         |  Januar | Februar |    Mart | ...
 * ------------+---------+---------+---------+-----
 * Bjuval RF   |  125.32 |  532.30 |    2.34 |
 * Gauda RF    |   12.43 |  125.44 | 1246.32 |
 * Edamer RF   |     ... |     ... |     ... |
 * Tilsiter RF |     ... |     ... |     ... |
 * Trapist RF  |     ... |     ... |     ... |
 * 
 */
package k1g1_2023;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Program {
	
	private static Stream<Racun> racuni;

	public static void main(String[] args) {

//		Racuni.stringStream(5000)
//				.forEach(System.out::println);

//		Racuni.racuniStream(5000)
//				.forEach(System.out::println);
//		ucitajRacune(5000).forEach(System.out::println);
		System.out.println(ispodHiljadu(2,2018));
		sortiraniPoVisini(5, 2, 2018).forEach(x -> System.out.print("Broj racuna: "+x.getBroj()+" Iznos: "+x.getUplaceno()+"\n"));
		statistikaKupovineProizvoda();
		statistikaTvrdogSira(2019);
	}
	
	public static Stream<Racun> ucitajRacune(int n) {
		return Racuni.stringStream(n).map(x -> parseRacunFromString(x));
	}
	
	public static Racun parseRacunFromString(String racun) {
		Pattern pattern = Pattern.compile("<broj>(?<broj>.+)</broj>\n\s*<datum>(?<datum>.*)</datum>\n\s*<vreme>(?<vreme>.*)</vreme>");
		Matcher matcher = pattern.matcher(racun);
		LocalDateTime vreme;
		int broj;
		if(matcher.find()) {
			try {
				vreme = LocalDateTime.parse(matcher.group("datum") + " " + matcher.group("vreme"), 
						DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"));
			} catch(DateTimeParseException e) {
				System.out.println("Los datum: " + matcher.group("datum") + " " + matcher.group("vreme"));
				return null;
			}
			broj = Integer.parseInt(matcher.group("broj"));
		} else {
			System.out.println("Nije pronadjen Main Info pattern.");
			return null;
		}
		
		pattern = Pattern.compile("<uplaceno>(?<uplaceno>.+)</uplaceno>");
		matcher = pattern.matcher(racun);
		int uplaceno;
		if(matcher.find()) {
			uplaceno = Integer.parseInt(matcher.group("uplaceno"));
		} else {
			System.out.println("Nije pronadjena uplata.");
			return null;
		}
		
		pattern = Pattern.compile("<proizvod>(?<proizvod>.+)</proizvod>\n\s*<kolicina>(?<kolicina>.+)</kolicina>\n\s*<cena>(?<cena>.+)</cena>\n\s*<stopa>(?<stopa>.+)</stopa>");
		List<Stavka> stavke = new ArrayList<Stavka>();
		for(String s: racun.split("<stavka>")) {
			matcher = pattern.matcher(s);
			if(matcher.find()) {
				String proizvod = matcher.group("proizvod");
				double kolicina = Double.parseDouble(matcher.group("kolicina"));
				double cena = Double.parseDouble(matcher.group("cena"));
				PoreskaStopa stopa = PoreskaStopa.valueOf(matcher.group("stopa").toUpperCase());;
				Stavka stavka = new Stavka(proizvod, kolicina, cena, stopa);
				stavke.add(stavka);
			}
		}
		return new Racun(broj, vreme, uplaceno, stavke);
	}
	
	private static long ispodHiljadu(int mesec, int godina) {
//		Metod vraca ukupan broj racuna, za zadati mesec zadate godine, za koje je
//		uplaceno strogo manje od hiljadu dinara.
		return ucitajRacune(5000).filter(x -> x.getUplaceno() < 1000 && 
					x.getVreme().getMonthValue() == mesec && 
					x.getVreme().getYear() == godina)
				.count();
	}
	
	private static List<Racun> sortiraniPoVisini(int dan, int mesec, int godina){
//		Metod vraca listu racuna izadtih zadatog dana, sortiranu rastuce po visini racuna.
		return ucitajRacune(5000).filter(x -> x.getVreme().getDayOfMonth() < dan && 
					x.getVreme().getMonthValue() == mesec && 
					x.getVreme().getYear() == godina)
				.sorted(Comparator.comparingInt(x -> x.getUplaceno()))
				.collect(Collectors.toList());
	}
	
	private static void statistikaKupovineProizvoda() {
		System.out.println("                   Proizvod | Prodato puta  ");
		System.out.println("----------------------------+---------------");
		ucitajRacune(5000).flatMap(x->x.getStavke().stream())
			.collect(Collectors.groupingBy(Stavka::getProizvod, Collectors.counting()))
			.forEach((proizvod, kolicina) -> System.out.println(String.format("%27s | %d", proizvod, kolicina)));
	}
	
	private static void statistikaTvrdogSira(int godina) {
//		 * Sir         |  Januar | Februar |    Mart | ...
//		 * ------------+---------+---------+---------+-----
//		 * Bjuval RF   |  125.32 |  532.30 |    2.34 |
//		 * Gauda RF    |   12.43 |  125.44 | 1246.32 |
//		 * Edamer RF   |     ... |     ... |     ... |
//		 * Tilsiter RF |     ... |     ... |     ... |
//		 * Trapist RF  |     ... |     ... |     ... |
		String[] listaSireva = {"bjuval rf", "gauda rf", "edamer rf", "tilsiter rf", "trapist rf"};
		Map<Integer, List<Racun>> lista = ucitajRacune(5000).filter(x->x.getVreme().getYear() == godina)
				.collect(Collectors.groupingBy(x -> x.getVreme().getMonthValue()));
		Map<String, Map<Integer, Double>> stat = new HashMap<String, Map<Integer, Double>>();
		for(int i: lista.keySet()) {
			Map<String, Double> s = lista.get(i).stream()
				.flatMap(x-> x.getStavke().stream())
				.filter(x-> Stream.of(listaSireva).anyMatch(x.getProizvod().toLowerCase()::equals))
				.sorted(Comparator.comparing(x -> x.getProizvod()))
				.collect(Collectors.groupingBy(Stavka::getProizvod, 
							Collectors.reducing(0.0, x -> x.getKolicina(), (x, y)-> x + y)));
			
			for(Entry<String, Double> ls: s.entrySet()) {
				Map<Integer, Double> localStat = new HashMap<Integer, Double>();
				localStat.put(i, ls.getValue());
				stat.put(ls.getKey(), localStat);
			}
		}
		String format = "%12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s | %12s ";
		System.out.println(String.format(format, "Sir", "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktorbar", "Novembar", "Decembar"));
		stat.entrySet()
		.stream()
		.forEachOrdered(x -> {
			System.out.print(String.format("%12s ", x.getKey()));
			x.getValue()
			.entrySet()
			.stream()
			.sorted(Comparator.comparingInt(s -> s.getKey()))
			.forEachOrdered(s -> System.out.print(String.format("| %12s ", s.getValue())));
			System.out.println();
			});
	}
}
