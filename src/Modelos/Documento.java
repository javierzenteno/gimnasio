package Modelos;

public class Documento {
	private int codpercre,codpermod,codgen;
	private long coddoc;
	private String feccredoc,fecmoddoc,pdfdoc,ubidoc;
	private boolean estdoc;
	
	public int getCodgen() {
		return codgen;
	}
	public void setCodgen(int codgen) {
		this.codgen = codgen;
	}
	public int getCodpercre() {
		return codpercre;
	}
	public void setCodpercre(int codpercre) {
		this.codpercre = codpercre;
	}
	public int getCodpermod() {
		return codpermod;
	}
	public void setCodpermod(int codpermod) {
		this.codpermod = codpermod;
	}
	public long getCoddoc() {
		return coddoc;
	}
	public void setCoddoc(long coddoc) {
		this.coddoc = coddoc;
	}
	public String getFeccredoc() {
		return feccredoc;
	}
	public void setFeccredoc(String feccredoc) {
		this.feccredoc = feccredoc;
	}
	public String getFecmoddoc() {
		return fecmoddoc;
	}
	public void setFecmoddoc(String fecmoddoc) {
		this.fecmoddoc = fecmoddoc;
	}
	public String getPdfdoc() {
		return pdfdoc;
	}
	public void setPdfdoc(String pdfdoc) {
		this.pdfdoc = pdfdoc;
	}
	public String getUbidoc() {
		return ubidoc;
	}
	public void setUbidoc(String ubidoc) {
		this.ubidoc = ubidoc;
	}
	public boolean isEstdoc() {
		return estdoc;
	}
	public void setEstdoc(boolean estdoc) {
		this.estdoc = estdoc;
	}

	
}
