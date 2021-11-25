package Servicios;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Fecha;
import Modelos.Liga;
import Utiles.Db_Coneccion;
import Utiles.Letra;
import Utiles.Vectores;
import Utiles.fechas;

@Service
public class LigaS extends Db_Coneccion {
	

	String sql;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,String feci,String fecf){
		 
		fechas f=new fechas();
		int gestion =f.obteneraño();
		Date fecin=new Date();
		Date fecfi=new Date();
		if(feci==null || feci==""){feci="01/01/"+(gestion-4);fecin=f.convertirStringDate(feci, "dd/MM/yyyy");}else{fecin=f.convertirStringDate(feci, "dd/MM/yyyy");}
		if(fecf==null || fecf==""){fecf="31/12/"+(gestion+2);fecfi=f.convertirStringDate(fecf, "dd/MM/yyyy");}else{fecfi=f.convertirStringDate(fecf, "dd/MM/yyyy");}
		if(search==null)search="";
		return db.queryForList("select * from liga_lista(?,?,?,?,?,?)"+as_object_add(as_liga, start<0?"":"feci text,fecf text,jugadores bigint,RN BIGINT,Tot INT"),start,length,search,estado,fecin,fecfi);
	}
	public Map<String, Object> obtener(int codlig){
		try {
			return db.queryForMap("select * from liga_obtener(?)"+as_object_add(as_liga, "feci text,fecf text"),codlig);
		} catch (Exception e) {
			System.out.println("error obtenerliga="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerJugadores(int codlig){
		try {
			return db.queryForList("select * from detalleliga_obtener(?)"+as_object_add(as_jugador,"jugador varchar,sel text"),codlig);
		} catch (Exception e) {
			System.out.println("error obtenerJugadores="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Liga li){
		try {
			Letra l=new Letra();
			fechas f=new fechas();
			return db.queryForObject("select liga_adicionar(?,?,?);",Boolean.class,l.Primera_Mayuscula(li.getNomlig()),f.convertirStringDate(li.getFecini(), "dd/MM/yyyy"),f.convertirStringDate(li.getFecfin(),"dd/MM/yyyy"));
		} catch (Exception e) {
			System.out.println("error al adicionar liga="+e.toString());
			return false;
		}
	}
	public boolean modificar(Liga li){
		try {
			Letra l=new Letra();
			fechas f=new fechas();
			return db.queryForObject("select liga_modificar(?,?,?,?);",Boolean.class,l.Primera_Mayuscula(li.getNomlig()),f.convertirStringDate(li.getFecini(), "dd/MM/yyyy"),f.convertirStringDate(li.getFecfin(), "dd/MM/yyyy"),li.getCodlig());
		} catch (Exception e) {
			System.out.println("error al modificar liga="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codlig){
		try {
			return db.queryForObject("select liga_eliminar(?);",Boolean.class,codlig);
		} catch (Exception e) {
			System.out.println("error al eliminar liga="+e.toString());
			return false;
		}
	}
	public boolean asignar(int codlig,int jugadores[]){
		try {
			Vectores v=new Vectores();
			String codjug=v.convertir_Int_a_String(jugadores);
			System.out.println(codjug);
			return db.queryForObject("select detalleliga_adicionar(?,\'"+codjug+"\');",Boolean.class,codlig);
		} catch (Exception e) {
			System.out.println("error al asignar jugadores-liga="+e.toString());
			return false;
		}
	}
	public List<Map<String, Object>> listar_f(int start,int length,int lig){
		return db.queryForList("select * from fecha_lista(?,?,?)"+as_object_add(as_fecha, start<0?"":"fec text,jugadores bigint,RN BIGINT,Tot INT"),start,length,lig);
	}
	public boolean adicionar_f(Fecha d){
		try {
			System.out.println(d.getFecha()+" "+d.getInsfec()+" "+d.getCodlig());
			return db.queryForObject("select fecha_adicionar(?,?,?)", Boolean.class, d.getFecha(),d.getInsfec(),d.getCodlig());
		} catch (Exception e) {
			System.out.println("error adicionarfecha"+e.toString());
			return false;
		}
	}
	public boolean modificar_f(Fecha d){
		try {
			return db.queryForObject("select fecha_modificar(?,?,?,?)", Boolean.class, d.getFecha(),d.getInsfec(),d.getCodlig(),d.getCodfec());
		} catch (Exception e) {
			System.out.println("error adicionarfecha"+e.toString());
			return false;
		}
	}
	public boolean darEstado_f(int codfec,int codlig){
		try {
			return db.queryForObject("select fecha_eliminar(?,?);",Boolean.class,codfec,codlig);
		} catch (Exception e) {
			System.out.println("error al eliminar fecha="+e.toString());
			return false;
		}
	}
	public boolean asignar_f(int codfec,int jugadores[]){
		try {
			Vectores v=new Vectores();
			String codjug=v.convertir_Int_a_String(jugadores);
			System.out.println(codjug);
			return db.queryForObject("select detallefecha_adicionar(?,\'"+codjug+"\');",Boolean.class,codfec);
		} catch (Exception e) {
			System.out.println("error al asignar jugadores-fecha="+e.toString());
			return false;
		}
	}
	public Map<String, Object> obtener_f(int codfec,int codlig){
		try {
			return db.queryForMap("select * from fecha_obtener(?,?)"+as_object_add(as_fecha, "fec text"),codfec,codlig);
		} catch (Exception e) {
			System.out.println("error obtenerliga="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> fecha_reporte(int lig){
		try {
			return db.queryForList("select * from fechas where codlig=?",lig);
		} catch (Exception e) {
			System.out.println("error listarfecha_reporte="+e.toString());
			return null;
		}
	}
}