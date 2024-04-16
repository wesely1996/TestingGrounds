package domaci2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Atleticar {

	private String punoIme;
	private LocalDate datumRodjenja;
	private String pol;
	private String zemlja;
	private String disciplina;
	
	public Atleticar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Atleticar(String punoIme, LocalDate datumRodjenja, String pol, String zemlja, String disciplina) {
		super();
		this.punoIme = punoIme;
		this.datumRodjenja = datumRodjenja;
		this.pol = pol;
		this.zemlja = zemlja;
		this.disciplina = disciplina;
	}

	public String getPunoIme() {
		return punoIme;
	}

	public void setPunoIme(String punoIme) {
		this.punoIme = punoIme;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getZemlja() {
		return zemlja;
	}

	public void setZemlja(String zemlja) {
		this.zemlja = zemlja;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	@Override
	public String toString() {
		return "Atleticar [punoIme=" + punoIme + ", datumRodjenja=" + datumRodjenja + ", pol=" + pol + ", zemlja="
				+ zemlja + ", disciplina=" + disciplina + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(datumRodjenja, disciplina, pol, punoIme, zemlja);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atleticar other = (Atleticar) obj;
		return Objects.equals(datumRodjenja, other.datumRodjenja) && Objects.equals(disciplina, other.disciplina)
				&& Objects.equals(pol, other.pol) && Objects.equals(punoIme, other.punoIme)
				&& Objects.equals(zemlja, other.zemlja);
	}
	
	public static Atleticar fromString(String line) {
		Pattern pattern = Pattern.compile("^(?<ime>.*?),(?:.*?),(?<pol>.*?),"
				+ "(?<datumrodj>.*?),(?:.*?),(?:.*?),(?<zemlja>.*?),(?:.*?),"
				+ "(?<disciplina>.*?),(?:.*?),(?:.*?),(?:.*?),(?:.*?),(?:.*)$");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			LocalDate datumrodj;
			try {
				datumrodj = LocalDate.parse(matcher.group("datumrodj"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			} catch (DateTimeParseException de) {
				System.out.println("preskocena linija");
				return null;
			};
			String ime = matcher.group("ime");
			String pol = matcher.group("pol");
			String zemlja = matcher.group("zemlja");
			String disciplina = matcher.group("disciplina");
			
			if(ime.equals("") || pol.equals("") || zemlja.equals("") || disciplina.equals("")) {
				System.out.println("preskocena linija");
				return null;
			}
			
			return new Atleticar(ime, datumrodj, pol, zemlja, disciplina);
		}
		return null;
	}	
}
