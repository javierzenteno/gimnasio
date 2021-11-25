package Servicios;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Egreso;
import Utiles.Db_Coneccion;
import Utiles.fechas;

@Service
public class EgresoS extends Db_Coneccion {

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,String fecegr){
		try{
		if(search==null)search="";
		fechas f=new fechas();
		Date fechac=null;
		if(fecegr!=null && fecegr!="")fechac=f.convertirStringDate(fecegr, "dd/MM/yyyy");			
		return db.queryForList("select * from egreso_lista(?,?,?,?,?)"+as_object_add(as_egreso,"gen integer,RN BIGINT,Tot INT"),start,length,search,estado,fechac);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(int codegr){
		try {
			return db.queryForMap("select * from egreso_obtener(?)"+as_egreso,codegr);
		} catch (Exception e) {
			System.out.println("error obtener egreso="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtenercaja(int cod){
		try {
			return db.queryForMap("select concat(round(cast(monto as decimal), 2),' Bs.')round from caja where codgen=?",cod);
		} catch (Exception e) {
			System.out.println("error obtener caja="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Egreso d,int ges){
		try {
			return db.queryForObject("select egreso_adicionar(?,?,?,?,?,?);",Boolean.class,db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges),d.getMonto(),d.getCodper(),d.getDesegr(),d.getPago(),d.getAquien());
		} catch (Exception e) {
			System.out.println("error al adicionar egreso="+e.toString());
			return false;
		}
	}
	public boolean modificar(Egreso d,int ges){
		try {
			return db.queryForObject("select egreso_modificar(?,?,?,?,?,?,?);",Boolean.class,db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges),d.getMonto(),d.getCodper(),d.getDesegr(),d.getPago(),d.getAquien(),d.getCodegr());
		} catch (Exception e) {
			System.out.println("error al modificar egreso="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codegr,boolean estegr,int ges){
		try {
			return db.queryForObject("select egreso_darestado(?,?,?);",Boolean.class,estegr,codegr,db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges));
		} catch (Exception e) {
			System.out.println("error al darestado egreso="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
