package Modelos;

public class Nacional {
	private int codnac,codgen;
	private String nomnac,fecha;
	private boolean estnac;
	
	public int getCodnac() {
		return codnac;
	}
	public void setCodnac(int codnac) {
		this.codnac = codnac;
	}
	public String getNomnac() {
		return nomnac;
	}
	public void setNomnac(String nomnac) {
		this.nomnac = nomnac;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public boolean isEstnac() {
		return estnac;
	}
	public void setEstnac(boolean estnac) {
		this.estnac = estnac;
	}
	public int getCodgen() {
		return codgen;
	}
	public void setCodgen(int codgen) {
		this.codgen = codgen;
	}
	@Override
	public String toString() {
		return "Nacional [codnac=" + codnac + ", nomnac=" + nomnac + ", fecha="
				+ fecha + ", estnac=" + estnac + "]";
	}
	
}
