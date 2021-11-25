package Modelos;

public class Nota {
	private int codnot;
	private long coddoc;
	private String emitente,remitente,titnot,obsnot;
	private boolean estnot,tipnot;
	
	public long getCoddoc() {
		return coddoc;
	}
	public void setCoddoc(long coddoc) {
		this.coddoc = coddoc;
	}
	public int getCodnot() {
		return codnot;
	}
	public void setCodnot(int codnot) {
		this.codnot = codnot;
	}
	public String getEmitente() {
		return emitente;
	}
	public void setEmitente(String emitente) {
		this.emitente = emitente;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getTitnot() {
		return titnot;
	}
	public void setTitnot(String titnot) {
		this.titnot = titnot;
	}
	public String getObsnot() {
		return obsnot;
	}
	public void setObsnot(String obsnot) {
		this.obsnot = obsnot;
	}
	public boolean isEstnot() {
		return estnot;
	}
	public void setEstnot(boolean estnot) {
		this.estnot = estnot;
	}
	public boolean isTipnot() {
		return tipnot;
	}
	public void setTipnot(boolean tipnot) {
		this.tipnot = tipnot;
	}
	
}
