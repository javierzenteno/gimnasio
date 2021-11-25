package Modelos;

import Modelos.Usuario;

public class Persona {
	private int codper;
	private String ciper,nomper,priapper,segapper,telper,dirper,fecnacper;
	private boolean estper,genper;
	private Usuario usuario;
	
	public int getCodper() {
		return codper;
	}

	public String getFecnacper() {
		return fecnacper;
	}

	public void setFecnacper(String fecnacper) {
		this.fecnacper = fecnacper;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setCodper(int codper) {
		this.codper = codper;
	}

	public String getCiper() {
		return ciper;
	}

	public void setCiper(String ciper) {
		this.ciper = ciper;
	}

	public String getNomper() {
		return nomper;
	}

	public void setNomper(String nomper) {
		this.nomper = nomper;
	}

	public String getPriapper() {
		return priapper;
	}

	public void setPriapper(String priapper) {
		this.priapper = priapper;
	}

	public String getSegapper() {
		return segapper;
	}

	public void setSegapper(String segapper) {
		this.segapper = segapper;
	}

	public String getTelper() {
		return telper;
	}

	public void setTelper(String telper) {
		this.telper = telper;
	}

	public String getDirper() {
		return dirper;
	}

	public void setDirper(String dirper) {
		this.dirper = dirper;
	}

	public boolean isEstper() {
		return estper;
	}

	public void setEstper(boolean estper) {
		this.estper = estper;
	}
	
	public boolean isGenper() {
		return genper;
	}

	public void setGenper(boolean genper) {
		this.genper = genper;
	}

	@Override
	public String toString() {
		return "Usuario [codper=" + codper + ", ciper=" + ciper + ", nomper="
				+ nomper + ", priapper=" + priapper + ", segapper=" + segapper
				+ ", telper=" + telper + ", dirper=" + dirper + ", estper="
				+ estper +", fecnacper="+fecnacper+ "]";
	}

}
