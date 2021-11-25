package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Provincia;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class ProvinciaS extends Db_Coneccion {
	
	String sql;
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from provincia where provincia.estpro=true;");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from provincia_lista(?,?,?,?)"+as_object_add(as_provincia,"RN BIGINT,Tot INT"),start,length,search,estado);
	}
	public Map<String, Object> obtener(int codpro){
		try {
			return db.queryForMap("select * from provincia_obtener(?)",codpro);
		} catch (Exception e) {
			System.out.println("error obtener provincia="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Provincia p){
		try {
			Letra l=new Letra();
			return db.queryForObject("select provincia_adicionar(?,?,?,?);",Boolean.class,l.Primera_Mayuscula(p.getNompro()),p.getAcropro().toUpperCase(),p.getLogpro(),p.getCodper());
		} catch (Exception e) {
			System.out.println("error al adicionar provincia="+e.toString());
			return false;
		}
	}
	public boolean modificar(Provincia p){
		try {
			Letra l=new Letra();
			return db.queryForObject("select provincia_modificar(?,?,?,?,?);",Boolean.class,l.Primera_Mayuscula(p.getNompro()),p.getAcropro().toUpperCase(),p.getLogpro(),p.getCodper(),p.getCodpro());
		} catch (Exception e) {
			System.out.println("error al modificar provincia="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codpro,boolean estpro){
		try {
			return db.queryForObject("select provincia_darestado(?,?);",Boolean.class,estpro,codpro);
		} catch (Exception e) {
			System.out.println("error al darestado provincia="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
