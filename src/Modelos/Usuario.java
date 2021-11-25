package Modelos;

public class Usuario {
	private int codper;
	private String login,password;
	private boolean estusu;
	public int getCodper() {
		return codper;
	}
	public void setCodper(int codper) {
		this.codper = codper;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEstusu() {
		return estusu;
	}
	public void setEstusu(boolean estusu) {
		this.estusu = estusu;
	}
	@Override
	public String toString() {
		return "Usuario [codper=" + codper + ", login=" + login + ", password="
				+ password + ", estusu=" + estusu + "]";
	}
	
}
