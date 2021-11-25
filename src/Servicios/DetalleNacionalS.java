package Servicios;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.DetalleNacional;
import Utiles.Db_Coneccion;
@Service
public class DetalleNacionalS extends Db_Coneccion {
	


	String sql;
	public List<Map<String, Object>> listar(int start,String search,int length,int codnac){
		if(search==null)search="";
		try{
		return db.queryForList("select * from detallenacional_lista(?,?,?,?)"+as_object_add(as_detallenacional, "nomper varchar,priapper varchar,segapper varchar,nomcat varchar,RN BIGINT,Tot INT"),start,length,search,codnac);
		}catch(Exception e){
			System.out.println("error listar detallenacional="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(int codnac,int codjug){
		try {
			return db.queryForMap("select * from detallenacional_obtener(?,?)",codnac,codjug);
		} catch (Exception e) {
			System.out.println("error obtenerdetallenacional="+e.toString());
			return null;
		}
	}
	
	public boolean adicionar(DetalleNacional m){
		try {
			return db.queryForObject("select detallenacional_adicionar(?,?,?,?);",Boolean.class,m.getCodnac(),m.getCodjug(),m.getCodcat(),m.getPosicion());
		} catch (Exception e) {
			System.out.println("error al adicionar detallenacional="+e.toString());
			return false;
		}
	}

	public boolean modificar(DetalleNacional m){
		try {
			return db.queryForObject("select detallenacional_modificar(?,?,?,?);",Boolean.class,m.getCodcat(),m.getPosicion(),m.getCodnac(),m.getCodjug());
		} catch (Exception e) {
			System.out.println("error al modificar detallenacional="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codnac,int codjug){
		try {
			return db.queryForObject("select detallenacional_eliminar(?,?);",Boolean.class,codnac,codjug);
		} catch (Exception e) {
			System.out.println("error al eliminar detallenacional="+e.toString());
			return false;
		}
	}

}