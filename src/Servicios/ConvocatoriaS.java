package Servicios;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Convocatoria;
import Modelos.Documento;
import Utiles.Db_Coneccion;
import Utiles.Letra;
import Utiles.fechas;

@Service
public class ConvocatoriaS extends Db_Coneccion {

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,String fecdoc,boolean tipdoc){
		try{
		if(search==null)search="";
		fechas f=new fechas();
		Date fechac=null;
		if(fecdoc!=null && fecdoc!="")fechac=f.convertirStringDate(fecdoc, "dd/MM/yyyy");		
		return db.queryForList("select * from convocatoria_lista(?,?,?,?,?,?)"+as_object_add(as_convocatoria,"ban boolean,RN BIGINT,Tot INT"),start,length,search,estado,fechac,tipdoc);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listara(){
		try{
		return db.queryForList("select tipcon,titcon,pdfdoc,fecpubcon from convocatoria join documento on documento.coddoc=convocatoria.coddoc where fecpubcon<=now() ORDER BY fecpubcon desc");
		} catch(Exception e){
			System.out.println("error listara="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> jugador(){
		try{
		return db.queryForList("select jugador.*,provincia.nompro,concat(persona.nomper,' ',persona.priapper,' ',persona.segapper) as jugador,año_obtener(codjug) as edad from jugador join provincia on provincia.codpro=jugador.codpro join persona on persona.codper=jugador.codper where estjug=true order by edad;");
		} catch(Exception e){
			System.out.println("error jugador="+e.toString());
			return null;
		}
	}
	public int genera(){
		return db.queryForObject("select coalesce(max(codcon),0)+1 from convocatoria", Integer.class);
	}
	public Map<String, Object> obtener(int codcon){
		try {
			return db.queryForMap("select * from convocatoria_obtener(?)"+as_convocatoria,codcon);
		} catch (Exception e) {
			System.out.println("error obtener convocatoria="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Convocatoria c,Documento d){
		try {
			Letra l=new Letra();
			return db.queryForObject("select convocatoria_adicionar(?,?,?,?,?,?,?);",Boolean.class,d.getCodgen(),d.getPdfdoc(),d.getCodpercre(),d.getUbidoc(),c.getFecpubcon(),c.isTipcon(),l.Primera_Mayuscula(c.getTitcon()));
		} catch (Exception e) {
			System.out.println("error al adicionar convocatoria="+e.toString());
			return false;
		}
	}
	public boolean modificar(Documento d,Convocatoria c){
		try {
			Letra l=new Letra();
			return db.queryForObject("select convocatoria_modificar(?,?,?,?,?,?,?,?);",Boolean.class,d.getPdfdoc(),d.getCodpermod(),d.getUbidoc(),d.getCoddoc(),c.getFecpubcon(),c.isTipcon(),l.Primera_Mayuscula(c.getTitcon()),c.getCodcon());
		} catch (Exception e) {
			System.out.println("error al modificar convocatoria="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codcon,boolean estcon){
		try {
			return db.queryForObject("select convocatoria_darestado(?,?);",Boolean.class,estcon,codcon);
		} catch (Exception e) {
			System.out.println("error al darestado convocatoria="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
