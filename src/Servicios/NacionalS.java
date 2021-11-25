package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Nacional;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class NacionalS extends Db_Coneccion {
	
	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from nacional_lista(?,?,?,?)"+as_object_add(as_nacional, start<0?"":"fech text,RN BIGINT,Tot INT"),start,length,search,estado);
	}
	public List<Map<String, Object>> categorias(){
		return db.queryForList("select * from categoria");
	}
	public Map<String, Object> obtener(int codnac){
		try {
			return db.queryForMap("select * from nacional_obtener(?)"+as_object_add(as_nacional, "fech text"),codnac);
		} catch (Exception e) {
			System.out.println("error obtenernacional="+e.toString());
			return null;
		}
	}
	
	public boolean adicionar(Nacional m){
		try {
			Letra l=new Letra();
			return db.queryForObject("select nacional_adicionar(?,?,?);",Boolean.class,l.Primera_Mayuscula(m.getNomnac()),m.getFecha(),m.getCodgen());
		} catch (Exception e) {
			System.out.println("error al adicionar nacional="+e.toString());
			return false;
		}
	}
	public int adicionarcategoria(String nomcat){
		try {
			Letra l=new Letra();
			return db.queryForObject("select categoria_adicionar(?);",Integer.class,l.Primera_Mayuscula(nomcat));
		} catch (Exception e) {
			System.out.println("error al adicionar categoria="+e.toString());
			return -1;
		}
	}
	public boolean modificar(Nacional m){
		try {
			Letra l=new Letra();
			return db.queryForObject("select nacional_modificar(?,?,?);",Boolean.class,l.Primera_Mayuscula(m.getNomnac()),m.getFecha(),m.getCodnac());
		} catch (Exception e) {
			System.out.println("error al modificar nacional="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codnac,boolean estnac){
		try {
			return db.queryForObject("select nacional_darestado(?,?);",Boolean.class,codnac,estnac);
		} catch (Exception e) {
			System.out.println("error al eliminar nacional="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select nacional_validar(?)", Boolean.class,nom);
	}
}