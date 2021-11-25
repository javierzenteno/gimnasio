package Servicios;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Arbitro;
import Utiles.Db_Coneccion;
@Service 
public class ArbitroS extends Db_Coneccion{

	public List<Map<String, Object>> listapersonas(){
		return db.queryForList("SELECT * from persona where persona.codper not in(select arbitro.codper from arbitro join persona on persona.codper=arbitro.codper)");
	}
	public List<Map<String, Object>> listaen(){
		return db.queryForList("SELECT persona.*,codarb from persona join arbitro on arbitro.codper=persona.codper");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		try{
		if(search==null)search="";
		return db.queryForList("select * from arbitro_lista(?,?,?,?)"+as_object_add(as_arbitro,start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado);
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
			return db.queryForMap("select * from arbitro_obtener(?)"+as_arbitro,codent);
		} catch (Exception e) {
			System.out.println("error obtener arbitro="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Arbitro j){
		try{
			return db.queryForObject("select arbitro_adicionar(?,?,?)",Boolean.class,j.getCodper(),j.getRanarb(),j.getFotarb());
		}catch(Exception e){
			return false;
		}
	}
	public boolean modificar(Arbitro j){
		try{
			return db.queryForObject("select arbitro_modificar(?,?,?)",Boolean.class,j.getRanarb(),j.getFotarb(),j.getCodarb());
		}catch(Exception e){
			return false;
		}
	}
	public boolean darestado(boolean estent,int codent){
		try{
			return db.queryForObject("select arbitro_darestado(?,?)",Boolean.class,codent,estent);
		}catch(Exception e){
			return false;
		}
	}
}
