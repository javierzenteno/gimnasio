package Modelos;

public class Partido {
	private long codpar,codgru;
	private int ganpar,codjuga,codjugb;
	private boolean wopar,byepar;
	private short estpar;
	public long getCodpar() {
		return codpar;
	}
	public void setCodpar(long codpar) {
		this.codpar = codpar;
	}
	public long getCodgru() {
		return codgru;
	}
	public void setCodgru(long codgru) {
		this.codgru = codgru;
	}
	public int getGanpar() {
		return ganpar;
	}
	public void setGanpar(int ganpar) {
		this.ganpar = ganpar;
	}
	public int getCodjuga() {
		return codjuga;
	}
	public void setCodjuga(int codjuga) {
		this.codjuga = codjuga;
	}
	public int getCodjugb() {
		return codjugb;
	}
	public void setCodjugb(int codjugb) {
		this.codjugb = codjugb;
	}
	public boolean isWopar() {
		return wopar;
	}
	public void setWopar(boolean wopar) {
		this.wopar = wopar;
	}
	public boolean isByepar() {
		return byepar;
	}
	public void setByepar(boolean byepar) {
		this.byepar = byepar;
	}
	public short getEstpar() {
		return estpar;
	}
	public void setEstpar(short estpar) {
		this.estpar = estpar;
	}
		
}
