package Modelos;

public class Convocatoria {
	private int codcon;
	private long coddoc;
	private String fecpubcon,titcon;
	private boolean estdoc,tipcon;
	
	public long getCoddoc() {
		return coddoc;
	}
	public void setCoddoc(long coddoc) {
		this.coddoc = coddoc;
	}
	public int getCodcon() {
		return codcon;
	}
	public void setCodcon(int codcon) {
		this.codcon = codcon;
	}
	public String getFecpubcon() {
		return fecpubcon;
	}
	public void setFecpubcon(String fecpubcon) {
		this.fecpubcon = fecpubcon;
	}
	public String getTitcon() {
		return titcon;
	}
	public void setTitcon(String titcon) {
		this.titcon = titcon;
	}
	public boolean isTipcon() {
		return tipcon;
	}
	public void setTipcon(boolean tipcon) {
		this.tipcon = tipcon;
	}
	public boolean isEstdoc() {
		return estdoc;
	}
	public void setEstdoc(boolean estdoc) {
		this.estdoc = estdoc;
	}

}
