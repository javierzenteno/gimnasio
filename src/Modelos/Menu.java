package Modelos;

import java.util.List;
import java.util.Map;

public class Menu {
	private int codmen;
	private String nommen,icomen;
	private boolean estmen;
	private List<Map<String,Object>> procesos;
	
	public List<Map<String, Object>> getProcesos() {
		return procesos;
	}
	public void setProcesos(List<Map<String, Object>> procesos) {
		this.procesos = procesos;
	}
	public int getCodmen() {
		return codmen;
	}
	public void setCodmen(int codmen) {
		this.codmen = codmen;
	}
	public String getIcomen() {
		return icomen;
	}
	public void setIcomen(String icomen) {
		this.icomen = icomen;
	}
	public String getNommen() {
		return nommen;
	}
	public void setNommen(String nommen) {
		this.nommen = nommen;
	}
	public boolean isEstmen() {
		return estmen;
	}
	public void setEstmen(boolean estmen) {
		this.estmen = estmen;
	}
	}
