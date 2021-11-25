package Servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;

import Modelos.Proceso;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class ProcesoS extends Db_Coneccion {

	public class a_objeto implements ParameterizedRowMapper<Proceso>{
		@Override
		public Proceso mapRow(ResultSet set, int fila) throws SQLException {
			Proceso p=new Proceso();
			p.setCodpro(set.getInt("codpro"));
			p.setNompro(set.getString("nompro"));
			p.setEstpro(set.getBoolean("estpro"));
			p.setUrlpro(set.getString("urlpro"));
			return p;
		}
	}
	String sql;
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from proceso where proceso.estpro=true;");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from proceso_lista(?,?,?,?)"+as_object_add(as_proceso, start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado);
	}
	public List<Map<String, Object>> obtenerprocesos(int cod_men){
		try {
			return db.queryForList("select * from procesos_obtenerxmenu(?)"+as_proceso,cod_men);
		} catch (Exception e) {
			System.out.println("error obtenerProcesos="+e.toString());
			return null;
		}
	}
	public List<Proceso> obtenerXmenu(int codm){
		sql="select DISTINCT p.* from proceso p join menpro mp on mp.codpro=p.codpro and mp.codmen=?";
		return db.query(sql, new a_objeto(),codm);
	}
	public List<Map<String, Object>> obtenerXcodm(int codmen){
		try {
			return db.queryForList("select * from proceso_obtenerxcodm(?)",codmen);
		} catch (Exception e) {
			System.out.println("error obtenerProcesos="+e.toString());
			return null;
		}
	}
	
	public Map<String, Object> obtener(int codpro){
		try {
			return db.queryForMap("select * from proceso_obtener(?)",codpro);
		} catch (Exception e) {
			System.out.println("error obtenerProceso="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Proceso p){
		try {
			Letra l=new Letra();
			return db.queryForObject("select proceso_adicionar(?,?)",Boolean.class,l.Primera_Mayuscula(p.getNompro()),p.getUrlpro());
		} catch (Exception e) {
			System.out.println("error al adicionar proceso="+e.toString());
			return false;
		}
	}
	public boolean modificar(Proceso p){
		try {
			Letra l=new Letra();
			return db.queryForObject("select proceso_modificar(?,?,?);",Boolean.class,p.getCodpro(),l.Primera_Mayuscula(p.getNompro()),p.getUrlpro());
		} catch (Exception e) {
			System.out.println("error al modificar proceso="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codpro,boolean estado){
		try {
			return db.queryForObject("select proceso_darestado(?,?)",Boolean.class,codpro,estado);
		} catch (Exception e) {
			System.out.println("error al activar proceso="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select proceso_validar(?)", Boolean.class,nom);
	}
}