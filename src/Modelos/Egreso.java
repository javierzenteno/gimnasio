package Modelos;

public class Egreso {
	private Long codegr;
	private int codper,codcaja;
	private String desegr,pago,aquien;
	private float monto;
	private boolean estegr;
	
	public String getPago() {
		return pago;
	}
	public void setPago(String pago) {
		this.pago = pago;
	}
	public String getAquien() {
		return aquien;
	}
	public void setAquien(String aquien) {
		this.aquien = aquien;
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

	public float getMonto() {
		return monto;
	}
	public void setMonto(float monto) {
		this.monto = monto;
	}
	public Long getCodegr() {
		return codegr;
	}
	public void setCodegr(Long codegr) {
		this.codegr = codegr;
	}
	public String getDesegr() {
		return desegr;
	}
	public void setDesegr(String desegr) {
		this.desegr = desegr;
	}
	public boolean isEstegr() {
		return estegr;
	}
	public void setEstegr(boolean estegr) {
		this.estegr = estegr;
	}


}
