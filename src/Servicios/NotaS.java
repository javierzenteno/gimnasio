package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Documento;
import Modelos.Nota;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class NotaS extends Db_Coneccion {

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,boolean tipdoc){
		try{
		if(search==null)search="";	
		return db.queryForList("select * from nota_lista(?,?,?,?,?)"+as_object_add(as_nota,"RN BIGINT,Tot INT"),start,length,search,estado,tipdoc);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public int genera(){
		return db.queryForObject("select coalesce(max(codnot),0)+1 from nota", Integer.class);
	}
	public Map<String, Object> obtener(int codcon){
		try {
			return db.queryForMap("select * from nota_obtener(?)"+as_nota,codcon);
		} catch (Exception e) {
			System.out.println("error obtener nota="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Nota n,Documento d){
		try {
			Letra l=new Letra();
			return db.queryForObject("select nota_adicionar(?,?,?,?,?,?,?,?,?);",Boolean.class,d.getCodgen(),d.getPdfdoc(),d.getCodpercre(),d.getUbidoc(),l.Primera_Mayuscula(n.getTitnot()),l.Primera_Mayuscula(n.getEmitente()),l.Primera_Mayuscula(n.getRemitente()),n.isTipnot(),n.getObsnot());
		} catch (Exception e) {
			System.out.println("error al adicionar nota="+e.toString());
			return false;
		}
	}
	public boolean modificar(Documento d,Nota c){
		try {
			Letra l=new Letra();
			return db.queryForObject("select nota_modificar(?,?,?,?,?,?,?,?,?,?);",Boolean.class,d.getPdfdoc(),d.getCodpermod(),d.getUbidoc(),d.getCoddoc(),l.Primera_Mayuscula(c.getTitnot()),l.Primera_Mayuscula(c.getEmitente()),l.Primera_Mayuscula(c.getRemitente()),c.isTipnot(),c.getObsnot(),c.getCodnot());
		} catch (Exception e) {
			System.out.println("error al modificar nota="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codnot,boolean estnot){
		try {
			return db.queryForObject("select nota_darestado(?,?);",Boolean.class,estnot,codnot);
		} catch (Exception e) {
			System.out.println("error al darestado nota="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
