import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class OsobeProgram {
	
	private static final LocalDate GRANICA = LocalDate.of(1970, 1, 1);
	
	public static void main(String[] args) {
		String[] nizStringova = Osobe.random(500);
		
		Stream<String> str = Arrays.stream(nizStringova);
		
				str.map(Osoba::new)
				.filter(o -> o.getPlata() < 50_000) //osoba.getDatumRodjenja().compareTo(GRANICA) < 0
				.filter(o -> o.getDatumRodjenja().compareTo(GRANICA) < 0)
				.sorted(Comparator.comparing(Osoba::getPlata))
				.limit(5)
				.flatMap(o -> o.getDeca().stream())
				.sorted()
				.distinct()
				.forEach(System.out::println);
				
				
		
		/*Osoba[] nizOsoba = new Osoba[nizStringova.length];
		for(int i = 0; i < nizStringova.length; i++) {
			nizOsoba[i] = new Osoba(nizStringova[i]);
		}
		
		//50_000
		
		List<Osoba> ispodProseka = new ArrayList<>();
		for(Osoba osoba : nizOsoba) {
			if(osoba.getPlata() < 50_000) {
				ispodProseka.add(osoba);
			}
		}
		
		List<Osoba> pre1970 = new ArrayList<>();
		for(Osoba osoba : ispodProseka) {
			if(osoba.getDatumRodjenja().compareTo(GRANICA) < 0) {
				pre1970.add(osoba);
			}
		}
		
		Collections.sort(pre1970, (x, y) -> x.getPlata() - y.getPlata());
		List<Osoba> najsiromasniji = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			najsiromasniji.add(pre1970.get(i));
		}
		
		List<String> imena = new ArrayList<>();
		for(Osoba osoba :  najsiromasniji) {
			for(String ime :  osoba.getDeca()) {
				imena.add(ime);
			}
		}
		
		Collections.sort(imena);
		String proslo = null;
		Iterator<String> iterator = imena.iterator();
		while(iterator.hasNext()) {
			String ime = iterator.next();
			if(ime.equals(proslo)) {
				iterator.remove();
			} else {
				proslo = ime;
			}
		}
		
		
		for(String ime : imena) {
			System.out.println(ime);
		}*/
		
	}

}
