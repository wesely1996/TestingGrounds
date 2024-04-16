package domaci2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Douniti progam sledecim metodama:
 * 
 * Set<String> sveZemlje() koja izdvaja u skup sve zemlje koje su se takmicile na olimpijadi
 * 
 * Map<LocalDate, List<Atleticar>> istiRodjendani() koja grupise atleticare sa istim datumom
 * 					rodjenja
 * 
 * List<Atleticar> zemljaIDisciplina(String zemlja, String disciplina) koja u listu 
 *                	izdvaja sve atleticare koji se takmice za prosledjenu zemlju u 
 *                	prosledjenoj diciplini
 *           
 * Map<String, Long> zemljaIDiciplina2(String zemlja, String disciplina) koja mapira
 * 					zemlje iz toka na broj atleticara koji je predstavljaju u prosledjenoj
 * 					disciplini
 * 
 * Map<String, String> zemljaIDisciplina3() koja mapira sve discipline
 * 					na zemlju koja ima najvise predstavnika u toj disciplini
 * 
 * Ispisati sledecu tablelu (podaci u ovom tekstu su ilustrativne prirode i ne poredstavljaju
 * 					stvarne podatke)
 * 
 * Zemlja | Broj predstavnika | Broj predstavnica
 *------------------------------------------------
 * Serbia | 17                | 34
 *------------------------------------------------
 * Hugary | 23                | 23
 * ...
 * 
 * 
 * 
 */

public class GlavniProgram {
	
	private static List<Atleticar> atleticari; 
	
	public static List<Atleticar> ucitaj(String imef) {
		List<Atleticar> atleticari = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(imef))) {
			String linija;
			linija = br.readLine();
			while ((linija = br.readLine()) != null) {
				Atleticar a = Atleticar.fromString(linija);
				if (a != null) {
					atleticari.add(a);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return atleticari;
	}
	
	
	public static void main(String...args) {
		atleticari = ucitaj("athletes.csv");
		//svi(atleticari);
		sviIzSrbije(atleticari);
		System.out.println("Ukupno takmicara iz Srbije: " + sviIzSrbijeBr(atleticari));
		
		//System.out.println(sveZemlje());
		//System.out.println(istiRodjendani());
		//System.out.println(zemljaIDisciplina("Serbia", "Basketball"));
		//System.out.println(zemljaIDisciplina2("Serbia", "Basketball"));
		//System.out.println(zemljaIDisciplina3());
		zemljePoPolovima();
	}
	
	
	private static void svi(List<Atleticar> lista) {
		lista.stream()
			.forEach(System.out::println);
	}
	
	private static void sviIzSrbije(List<Atleticar> lista) {
		lista.stream()
			.filter(a -> a.getZemlja().equals("Serbia"))
			.forEach(System.out::println);
	}
	
	private static long sviIzSrbijeBr(List<Atleticar> lista) {
		return lista.stream()
			.filter(a -> a.getZemlja().equals("Serbia"))
			.count();
	}
	
	private static Set<String> sveZemlje(){
		return atleticari.stream()
				.map(Atleticar::getZemlja)
				.distinct()
				.collect(Collectors.toSet());
	}
	
	private static Map<LocalDate, List<Atleticar>> istiRodjendani(){
		return atleticari.stream()
				.collect(Collectors.groupingBy(Atleticar::getDatumRodjenja));
	}

	public static List<Atleticar> zemljaIDisciplina(String zemlja, String disciplina){
		return atleticari.stream()
				.filter(x -> x.getZemlja().equals(zemlja) && x.getDisciplina().equals(disciplina))
				.collect(Collectors.toList());
	}
	
	public static Map<String, Long> zemljaIDisciplina2(String zemlja, String disciplina){
		return atleticari.stream()
				.filter(x -> x.getZemlja().equals(zemlja) && x.getDisciplina().equals(disciplina))
				.collect(Collectors.groupingBy(Atleticar::getDisciplina, Collectors.counting()));
	}
	
	public static Map<String, String> zemljaIDisciplina3(){
		return atleticari.stream()
				.collect(Collectors
						.groupingBy(Atleticar::getDisciplina, 
								Collectors.collectingAndThen(
										Collectors.groupingBy(Atleticar::getZemlja, 
												Collectors.counting()), 
										map -> map.entrySet().stream()
										.max(Comparator.comparingLong(Map.Entry::getValue))
										.map(Map.Entry::getKey)
										.orElse(null))));
	}
	
	public static void zemljePoPolovima() {
		Map<String, Map<String, Long>> result = atleticari.stream()
				.collect(Collectors.groupingBy(Atleticar::getZemlja, 
						Collectors.groupingBy(Atleticar::getPol, Collectors.counting())));
		String brakeline = "-".repeat(77);
		System.out.printf("%-36s| %-20s| %-21s%n", "Zemlja", "Broj predstavnika", "Broj predstavnica");
		System.out.println(brakeline);

		for (Map.Entry<String, Map<String, Long>> entry : result.entrySet()) {
		    String name = entry.getKey();
		    Long males = entry.getValue().getOrDefault("Male", (long) 0);
		    Long females = entry.getValue().getOrDefault("Female", (long) 0);

		    System.out.printf("%-36s| %-20d| %-21d%n", name, males, females);
		    System.out.println(brakeline);
		}
	}
}
