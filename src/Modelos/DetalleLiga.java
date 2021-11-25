package Modelos;

public class DetalleLiga {
	private int codjug,codlig,coddetlig;
	private float total;
	private String categoria;
	public int getCodjug() {
		return codjug;
	}
	public void setCodjug(int codjug) {
		this.codjug = codjug;
	}
	public int getCodlig() {
		return codlig;
	}
	public void setCodlig(int codnac) {
		this.codlig = codnac;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getCoddetlig() {
		return coddetlig;
	}
	public void setCoddetlig(int coddetlig) {
		this.coddetlig = coddetlig;
	}
	@Override
	public String toString() {
		return "DetalleLiga [codjug=" + codjug + ", codlig=" + codlig + ", coddetlig=" + coddetlig + ", total=" + total
				+ ", categoria=" + categoria + "]";
	}
	
	
}
