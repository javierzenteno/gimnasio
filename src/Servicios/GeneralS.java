package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.General;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class GeneralS extends Db_Coneccion {
	
	String sql;
	public List<Map<String, Object>> lista(int codgen){
		return db.queryForList("SELECT codper,concat(nomper,' ',priapper,' ',segapper)persona from persona where persona.codper not in(select directiva.codper from directiva join persona on persona.codper=directiva.codper where directiva.codgen=?) and (cast(left(cast(now() as text),4) as integer))-(cast(left(cast(persona.fecnacper as text),4) as integer))>18 and codper>0 ORDER BY codper",codgen);
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from general_lista(?,?,?,?)"+as_object_add(as_general,start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado);
	}
	public Map<String, Object> obtener(int codgen){
		try {
			return db.queryForMap("select * from general_obtener(?)",codgen);
		} catch (Exception e) {
			System.out.println("error obtener general="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerd(int codgen){
		try {
			return db.queryForList("select * from directiva_obtener(?) as(codgen integer,codper integer,cargo varchar,dir text)",codgen);
		} catch (Exception e) {
			System.out.println("error obtener directiva= "+e.toString());
			return null;
		}
	}
	public boolean asignar(int codgen,Integer codper[],String cargo[]){
		boolean aux=false;
		try {
			db.update("delete from directiva where codgen=?",codgen);
			if(codper !=null){
				for (int i = 0; i < cargo.length; i++) {
					aux=db.queryForObject("select directiva_adicionar(?,?,?);",Boolean.class,cargo[i],codper[i],codgen);
				}
			}
			return aux;
		} catch (Exception e) {
			System.out.println("error al asignar directivo a general= "+e.toString());
			return aux;
		}
	}
	public boolean adicionar(General p,float mon,int cod){
		try {
			Letra l=new Letra();
			return db.queryForObject("select general_adicionar(?,?,?,?,?,?,?);",Boolean.class,l.Primera_Mayuscula(p.getNomgen()),p.getTelgen(),p.getDirgen(),p.getLoggen(),p.getAcrgen().toUpperCase(),mon,cod);
		} catch (Exception e) {
			System.out.println("error al adicionar general="+e.toString());
			return false;
		}
	}
	public boolean modificar(General p){
		try {
			Letra l=new Letra();
			return db.queryForObject("select general_modificar(?,?,?,?,?,?);",Boolean.class,l.Primera_Mayuscula(p.getNomgen()),p.getTelgen(),p.getDirgen(),p.getLoggen(),p.getAcrgen().toUpperCase(),p.getCodgen());
		} catch (Exception e) {
			System.out.println("error al modificar general="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codgen,boolean estgen){
		try {
			return db.queryForObject("select general_darestado(?,?);",Boolean.class,codgen,estgen);
		} catch (Exception e) {
			System.out.println("error al darestado general="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
