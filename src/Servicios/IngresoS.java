package Servicios;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Ingreso;
import Utiles.Db_Coneccion;
import Utiles.fechas;

@Service
public class IngresoS extends Db_Coneccion {

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,String fecing){
		try{
		if(search==null)search="";
		fechas f=new fechas();
		Date fechac=null;
		if(fecing!=null && fecing!="")fechac=f.convertirStringDate(fecing, "dd/MM/yyyy");			
		return db.queryForList("select * from ingreso_lista(?,?,?,?,?)"+as_object_add(as_ingreso,"gen integer,RN BIGINT,Tot INT"),start,length,search,estado,fechac);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(int coding){
		try {
			return db.queryForMap("select * from ingreso_obtener(?)"+as_ingreso,coding);
		} catch (Exception e) {
			System.out.println("error obtener ingreso="+e.toString());
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
	public boolean adicionar(Ingreso d,int ges){
		try {
			int a=db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges);
			return db.queryForObject("select ingreso_adicionar(?,?,?,?,?,?);",Boolean.class,a,d.getMonto(),d.getCodper(),d.getDesing(),d.getRecibio(),d.getDequien());
		} catch (Exception e) {
			System.out.println("error al adicionar ingreso="+e.toString());
			return false;
		}
	}
	public boolean modificar(Ingreso d,int ges){
		try {
			int a=db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges);
			return db.queryForObject("select ingreso_modificar(?,?,?,?,?,?,?);",Boolean.class,a,d.getMonto(),d.getCodper(),d.getDesing(),d.getRecibio(),d.getDequien(),d.getCoding());
		} catch (Exception e) {
			System.out.println("error al modificar ingreso="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int coding,boolean esting,int ges){
		try {
			int a=db.queryForObject("select codcaja from caja where codgen=?",Integer.class,ges);
			return db.queryForObject("select ingreso_darestado(?,?,?);",Boolean.class,esting,coding,a);
		} catch (Exception e) {
			System.out.println("error al darestado ingreso="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
