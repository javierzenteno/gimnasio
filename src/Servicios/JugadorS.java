package Servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;

import Modelos.Jugador;
import Utiles.Db_Coneccion;
@Service 
public class JugadorS extends Db_Coneccion{
	public class a_objeto implements ParameterizedRowMapper<Jugador>{
		@Override
	public Jugador mapRow(ResultSet set, int index) throws SQLException{
		Jugador j=new Jugador();
		j.setCodjug(set.getInt("codjug"));
		j.setCodper(set.getInt("codper"));
		j.setEstjug(set.getBoolean("estjug"));
		j.setCodpro(set.getInt("codpro"));
		j.setFotjug(set.getString("fotjug"));
		j.setMadera(set.getString("madera"));
		j.setGomad(set.getString("gomad"));
		j.setGomar(set.getString("gomar"));
		return j;
	}
	}
	public List<Map<String, Object>> listapersonas(){
		return db.queryForList("SELECT * from persona where persona.codper not in(select jugador.codper from jugador join persona on persona.codper=jugador.codper)");
	}
	public List<Map<String, Object>> listaju(){
		return db.queryForList("SELECT persona.*,codjug from persona join jugador on jugador.codper=persona.codper");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length,String provincia){
		try{
		if(search==null)search="";
		int p=Integer.parseInt(provincia);
		return db.queryForList("select * from jugador_lista(?,?,?,?,?)"+as_object_add(as_jugador,start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado,p);
		} catch(Exception e){
			System.out.println("error listar="+e.toString());
			return null;
		}
	}
	public int edad(int codper){
		try{
		return db.queryForObject("select año_obtener(?)",Integer.class,codper);}catch(Exception e){System.out.println("error edad");return 0;}
	}
	public Map<String, Object> obtener(int codjug){
		try {
			return db.queryForMap("select * from jugador_obtener(?)"+as_jugador,codjug);
		} catch (Exception e) {
			System.out.println("error obtener jugador="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerxliga(int lig){
		try {
			return db.queryForList("select * from jugador_obtenerxliga(?)"+" as (codjug integer,jugador text,sel text)",lig);
		} catch (Exception e) {
			System.out.println("error obtener_jugador_liga"+e.toString());
			return null;
		}
	}
	public boolean adicionar(Jugador j){
		try{
			return db.queryForObject("select jugador_adicionar(?,?,?,?,?,?)",Boolean.class,j.getCodper(),j.getCodpro(),j.getFotjug(),j.getMadera(),j.getGomad(),j.getGomar());
		}catch(Exception e){
			return false;
		}
	}
	public boolean modificar(Jugador j){
		try{
			return db.queryForObject("select jugador_modificar(?,?,?,?,?,?)",Boolean.class,j.getCodjug(),j.getCodpro(),j.getFotjug(),j.getMadera(),j.getGomad(),j.getGomar());
		}catch(Exception e){
			return false;
		}
	}
	public boolean darestado(boolean estjug,int codjug){
		try{
			return db.queryForObject("select jugador_darestado(?,?)",Boolean.class,codjug,estjug);
		}catch(Exception e){
			return false;
		}
	}
}
