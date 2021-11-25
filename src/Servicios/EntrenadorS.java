package Servicios;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Entrenador;
import Utiles.Db_Coneccion;
@Service 
public class EntrenadorS extends Db_Coneccion{

	public List<Map<String, Object>> listapersonas(){
		return db.queryForList("SELECT * from persona where persona.codper not in(select entrenador.codper from entrenador join persona on persona.codper=entrenador.codper)");
	}
	public List<Map<String, Object>> listaen(){
		return db.queryForList("SELECT persona.*,codent from persona join entrenador on entrenador.codper=persona.codper");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		try{
		if(search==null)search="";
		return db.queryForList("select * from entrenador_lista(?,?,?,?)"+as_object_add(as_entrenador,start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public int edad(int codper){
		try{
		return db.queryForObject("select año_obtener(?)",Integer.class,codper);}catch(Exception e){System.out.println("error edad");return 0;}
	}
	public Map<String, Object> obtener(int codent){
		try {
			return db.queryForMap("select * from entrenador_obtener(?)"+as_entrenador,codent);
		} catch (Exception e) {
			System.out.println("error obtener entrenador="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Entrenador j){
		try{
			return db.queryForObject("select entrenador_adicionar(?,?,?)",Boolean.class,j.getCodper(),j.getRanent(),j.getFotent());
		}catch(Exception e){
			return false;
		}
	}
	public boolean modificar(Entrenador j){
		try{
			return db.queryForObject("select entrenador_modificar(?,?,?)",Boolean.class,j.getRanent(),j.getFotent(),j.getCodent());
		}catch(Exception e){
			return false;
		}
	}
	public boolean darestado(boolean estent,int codent){
		try{
			return db.queryForObject("select entrenador_darestado(?,?)",Boolean.class,codent,estent);
		}catch(Exception e){
			return false;
		}
	}
}
