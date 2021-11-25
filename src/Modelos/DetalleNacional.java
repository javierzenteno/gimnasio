package Modelos;

public class DetalleNacional {
	private int codjug,codnac,codcat;
	private short posicion;
	public int getCodjug() {
		return codjug;
	}
	public void setCodjug(int codjug) {
		this.codjug = codjug;
	}
	public int getCodnac() {
		return codnac;
	}
	public void setCodnac(int codnac) {
		this.codnac = codnac;
	}
	public short getPosicion() {
		return posicion;
	}
	public void setPosicion(short posicion) {
		this.posicion = posicion;
	}
	public int getCodcat() {
		return codcat;
	}
	public void setCodcat(int codcat) {
		this.codcat = codcat;
	}
	@Override
	public String toString() {
		return "DetalleNacional [codjug=" + codjug + ", codnac=" + codnac
				+ ", posicion=" + posicion + ", categoria=" + codcat + "]";
	}
	
}
