package Servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;

import Modelos.Rol;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class RolS extends Db_Coneccion {
	public class a_objeto implements ParameterizedRowMapper<Rol>{
		@Override
		public Rol mapRow(ResultSet set, int index) throws SQLException {
			Rol r=new Rol();
			r.setCodrol(set.getInt("codrol"));
			r.setNomrol(set.getString("nomrol"));
			r.setEstrol(set.getBoolean("estrol"));
			return r;
		}
	}
	String sql;
	public List<Rol> obtenerRolesXcodusu(int codusu){
		return db.query("select * from rol_obtenerxcodusu(?)", new a_objeto(),codusu);
	}
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from rol where rol.estrol=true;");
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from rol_lista(?,?,?,?)"+as_object_add(as_rol,"RN BIGINT,Tot INT,menus BIGINT"),start,length,search,estado);
	}
	public Map<String, Object> obtener(int codrol){
		try {
			return db.queryForMap("select * from rol_obtener(?)",codrol);
		} catch (Exception e) {
			System.out.println("error obtenerRol="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerMenus(int codrol){
		try {
			return db.queryForList("select menu.*,case when (select count(*) from rolmen where codrol=? and rolmen.codmen=menu.codmen)>0 THEN 'selected' else '' end as sel from menu where menu.estmen=true;",codrol);
		} catch (Exception e) {
			System.out.println("error obtenerMenus="+e.toString());
			return null;
		}
	}
	public boolean adicionar(Rol r){
		try {
			Letra l=new Letra();
			return db.queryForObject("select rol_adicionar(?);",Boolean.class,l.Primera_Mayuscula(r.getNomrol()));
		} catch (Exception e) {
			System.out.println("error al adicionar rol="+e.toString());
			return false;
		}
	}
	public boolean modificar(Rol r){
		try {
			Letra l=new Letra();
			return db.queryForObject("select rol_modificar(?,?);",Boolean.class,l.Primera_Mayuscula(r.getNomrol()),r.getCodrol());
		} catch (Exception e) {
			System.out.println("error al modificar rol="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codrol,boolean estmen){
		try {
			return db.queryForObject("select rol_darestado(?,?);",Boolean.class,codrol,estmen);
		} catch (Exception e) {
			System.out.println("error al eliminar rol="+e.toString());
			return false;
		}
	}
	public boolean asignar(int codrol,Integer menus[]){
		try {
			db.update("delete from rolmen where codrol=?",codrol);
			if(menus!=null)
				for (int i = 0; i < menus.length; i++)
					db.update("insert into rolmen(codrol,codmen) values(?,?)",codrol,menus[i]);
			return true;
		} catch (Exception e) {
			System.out.println("error al asignar rol-menu="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
