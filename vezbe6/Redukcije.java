import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Redukcije {

	public static void main(String[] args) {

		// Prosta suma prvih 10 pozitivnih brojeva
		System.out.println("S1:");
		int s1 = IntStream.range(1, 11)
			.sum();
		System.out.println(s1);

		// Suma kvadrata
		System.out.println();
		System.out.println("S2:");
		int s2 = IntStream.range(1, 11)
			.map(x -> x * x)
			.sum();
		System.out.println(s2);
		
		int s21 = IntStream.range(1,  11)
				.reduce(0, (x, y) -> x + y*y);
		System.out.println("S12: " + s21);

		// Ispis svih prostih brojeva
		System.out.println();
		System.out.println("Prosti brojevi:");
		IntStream.range(2, 21)
			.filter(x -> IntStream.range(2, x).noneMatch(y -> x % y == 0))
			.forEach(System.out::println);

		// Suma pomocu .reduce() umesto .sum()
		System.out.println();
		System.out.println("S3:");
		int s3 = IntStream.range(1, 11)
			.reduce(0, (x, y) -> x + y);
		System.out.println(s3);

		// Racunanje sume na isti nacina samo sa porukama koje ilustruju redosled poziva operacije
		System.out.println();
		System.out.println("S4:");
		int s4 = IntStream.range(1, 11)
			.reduce(0, (x, y) -> { int r = x + y; System.out.printf("%d + %d = %d%n", x, y, r); return r; });
		System.out.println(s4);

		// Skupljanje elemenata u listu
		// OVAKO NE TREBA RADITI posto moze dovesti do problema i netacnih rezultata
		System.out.println();
		System.out.println("L1:");
		List<Integer> l1 = new ArrayList<>();
		IntStream.range(1, 41).parallel()
			.forEach(x -> l1.add(x));
		l1.sort((x, y) -> x - y);
		System.out.println(l1);
		System.out.println(l1.size());

		// Pravilan nacin skupljanja elemenata u listu pomocu redukcije
		// U ovom slucaju koristimo .collect() koji radi promenljivu (mutable) redukciju
		// Koristi se jednom napravljena lista i u nju se dodaju elementi
		System.out.println();
		System.out.println("L2:");
		List<Integer> l2 = IntStream.range(1, 41).parallel()
			.collect(ArrayList::new,
				(l, x) -> l.add(x),
				(la, lb) -> la.addAll(lb));
		System.out.println(l2);
		System.out.println(l2.d);

		// Prikaz skupljanja elemenata u listu pomocu .reduce() koji radi nepromenljivu redukciju
		// Ovo je neefikasnije resenje jer u svakom koraku pravi nove liste posto ne smemo da menjamo postojece
		System.out.println();
		System.out.println("L3:");
		List<Integer> l3 = IntStream.range(1, 21).parallel()
			.mapToObj(x -> {
				List<Integer> l = new ArrayList<>();
				l.add(x);
				return l;
			})
			.reduce(new ArrayList<>(), (la, lb) -> {
				List<Integer> l = new ArrayList<>();
				l.addAll(la);
				l.addAll(lb);
				return l;
			});
		System.out.println(l3);
		
		

		// Neefikasan nacin za brojanje posebno parnih i posebno neparnih brojeva
		// Prolazimo dva puta kroz tok
		long par = IntStream.of(1, 2, -3, 5, 7, 3, 8, -5, -2, 10, 11)
			.peek(System.out::println)
			.filter(x -> x % 2 == 0)
			.count();
		long nepar = IntStream.of(1, 2, -3, 5, 7, 3, 8, -5, -2, 10, 11)
			.peek(System.out::println)
			.filter(x -> x % 2 != 0)
			.count();
		System.out.println("Parnih: " + par);
		System.out.println("Neparnih: " + nepar);

		class ParNepar {
			public long par;
			public long nepar;
		}

		// Efikasniji nacin posto samo jednom prolazimo kroz tok
		ParNepar pn = IntStream.of(1, 2, -3, 5, 7, 3, 8, -5, -2, 10, 11)
			.peek(System.out::println)
			.collect(ParNepar::new,
					(pn1, x) -> {
						if (x % 2 == 0) {
							pn1.par++;
						} else {
							pn1.nepar++;
						}
					},
					(pn1, pn2) -> {
						pn1.par += pn2.par;
						pn1.nepar += pn2.nepar;
					});
		System.out.println("Parnih: " + pn.par);
		System.out.println("Neparnih: " + pn.nepar);

		// Za domaci zadatak pronaci i minimalni i maksimalni element pomocu
		// .collect samo jednim prolaskom kroz tok

	}
}
