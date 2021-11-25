package Modelos;

public class Torneo {
	
	private long codtor;
	private int codmod,codgen;
	private String nomtor,destor,fector;
	private short esttor;
	
	public long getCodtor() {
		return codtor;
	}
	public void setCodtor(long codtor) {
		this.codtor = codtor;
	}
	public int getCodmod() {
		return codmod;
	}
	public void setCodmod(int codmod) {
		this.codmod = codmod;
	}
	public int getCodgen() {
		return codgen;
	}
	public void setCodgen(int codgen) {
		this.codgen = codgen;
	}
	public String getNomtor() {
		return nomtor;
	}
	public void setNomtor(String nomtor) {
		this.nomtor = nomtor;
	}
	public String getDestor() {
		return destor;
	}
	public void setDestor(String destor) {
		this.destor = destor;
	}
	public String getFector() {
		return fector;
	}
	public void setFector(String fector) {
		this.fector = fector;
	}
	public short getEsttor() {
		return esttor;
	}
	public void setEsttor(short esttor) {
		this.esttor = esttor;
	}
}
