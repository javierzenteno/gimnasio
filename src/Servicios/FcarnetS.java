package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Documento;
import Utiles.Db_Coneccion;

@Service
public class FcarnetS extends Db_Coneccion {

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		try{
		if(search==null)search="";	
		return db.queryForList("select * from fcarnet_lista(?,?,?,?)"+as_object_add(as_fcarnet,"ban boolean,RN BIGINT,Tot INT"),start,length,search,estado);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public List<Map<String,Object>> personas(){
		return db.queryForList("select codper,concat(nomper,' ',priapper,' ',segapper)nombre from persona where estper=true and codper>0 order by codper");
	}
	public long genera(){
		return db.queryForObject("select coalesce(max(coddoc),0)+1 from documento", Long.class);
	}
	public Map<String, Object> obtener(long coddoc,int codper){
		try {
			return db.queryForMap("select * from fcarnet_obtener(?,?)"+as_fcarnet,coddoc,codper);
		} catch (Exception e) {
			System.out.println("error obtener fcarnet="+e.toString());
			return null;
		}
	}
	public boolean adicionar(String fcaduco,int codper,Documento d){
		try {
			return db.queryForObject("select fcarnet_adicionar(?,?,?,?,?,?);",Boolean.class,d.getCodgen(),d.getPdfdoc(),d.getCodpercre(),d.getUbidoc(),codper,fcaduco);
		} catch (Exception e) {
			System.out.println("error al adicionar fcarnet="+e.toString());
			return false;
		}
	}
	public boolean modificar(Documento d,int codper,String fcaduco){
		try {
			return db.queryForObject("select fcarnet_modificar(?,?,?,?,?,?);",Boolean.class,d.getPdfdoc(),d.getCodpermod(),d.getUbidoc(),fcaduco,d.getCoddoc(),codper);
		} catch (Exception e) {
			System.out.println("error al modificar nota="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codper,long coddoc){
		try {
			return db.queryForObject("select fcarnet_darestado(?,?);",Boolean.class,coddoc,codper);
		} catch (Exception e) {
			System.out.println("error al darestado nota="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
