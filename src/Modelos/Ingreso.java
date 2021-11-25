package Modelos;

public class Ingreso {
	private Long coding;
	private int codper,codcaja;
	private String desing,recibio,dequien;
	private float monto;
	private boolean esting;
	public String getRecibio() {
		return recibio;
	}
	public void setRecibio(String recibio) {
		this.recibio = recibio;
	}
	public String getDequien() {
		return dequien;
	}
	public void setDequien(String dequien) {
		this.dequien = dequien;
	}
	public Long getCoding() {
		return coding;
	}
	public void setCoding(Long coding) {
		this.coding = coding;
	}
	public int getCodper() {
		return codper;
	}
	public void setCodper(int codper) {
		this.codper = codper;
	}
	public int getCodcaja() {
		return codcaja;
	}
	public void setCodcaja(int codcaja) {
		this.codcaja = codcaja;
	}
	public String getDesing() {
		return desing;
	}
	public void setDesing(String desing) {
		this.desing = desing;
	}
	public float getMonto() {
		return monto;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
	public boolean isEsting() {
		return esting;
	}
	public void setEsting(boolean esting) {
		this.esting = esting;
	}

}
