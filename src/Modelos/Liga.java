package Modelos;

public class Liga {
	private int codlig;
	private String nomlig,fecini,fecfin;
	private boolean estlig;
	public int getCodlig() {
		return codlig;
	}
	public void setCodlig(int codlig) {
		this.codlig = codlig;
	}
	public String getNomlig() {
		return nomlig;
	}
	public void setNomlig(String nomlig) {
		this.nomlig = nomlig;
	}
	public String getFecini() {
		return fecini;
	}
	public void setFecini(String fecini) {
		this.fecini = fecini;
	}
	public String getFecfin() {
		return fecfin;
	}
	public void setFecfin(String fecfin) {
		this.fecfin = fecfin;
	}
	public boolean isEstlig() {
		return estlig;
	}
	public void setEstlig(boolean estlig) {
		this.estlig = estlig;
	}
}
