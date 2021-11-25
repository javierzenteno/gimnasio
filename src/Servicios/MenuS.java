package Servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;

import Modelos.Menu;
import Utiles.Db_Coneccion;
import Utiles.Letra;

@Service
public class MenuS extends Db_Coneccion {
	
	@Autowired
	ProcesoS procesoS;

	public class a_objeto implements ParameterizedRowMapper<Menu>{
		@Override
		public Menu mapRow(ResultSet set, int fila) throws SQLException {
			Menu m=new Menu();
			m.setCodmen(set.getInt("codmen"));
			m.setNommen(set.getString("nommen"));
			m.setIcomen(set.getString("icomen"));
			m.setEstmen(set.getBoolean("estmen"));
			try {
				m.setProcesos(procesoS.obtenerprocesos(set.getInt("codmen")));
			} catch (Exception e) {
				m.setProcesos(null);
			}
			return m;
		}
	}
	String sql;
	public List<Map<String, Object>> lista(){
		return db.queryForList("select * from menu where menu.estmen=true;");
	}
	public List<Map<String, Object>> obtenerProcesos(int codmen){
		try {
			return db.queryForList("select proceso.*,case when (select count(*) from menpro where codmen=? and menpro.codpro=proceso.codpro)>0 THEN 'selected' else '' end as sel from proceso where proceso.estpro=true;",codmen);
		} catch (Exception e) {
			System.out.println("error obtenerProcesos="+e.toString());
			return null;
		}
	}
	public List<Menu> obtenerXusuario(String login,int codrol){
		try{
		if(codrol>0)
			return db.query("select * from menus_obtenerxlogincodrol(?,?)"+as_menu, new a_objeto(),login,codrol);
		else
			return db.query("select * from menu_obtenerxlogin(?)"+as_menu, new a_objeto(),login);
		}catch(Exception e){
			System.out.println(e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		return db.queryForList("select * from menu_lista(?,?,?,?)"+as_object_add(as_menu, "RN BIGINT,Tot INT,procesos BIGINT"),start,length,search,estado);
	}
	
	public Map<String, Object> obtener(int codmen){
		try {
			return db.queryForMap("select * from menu_obtener(?)",codmen);
		} catch (Exception e) {
			System.out.println("error obtenerMenu="+e.toString());
			return null;
		}
	}
	
	public boolean adicionar(Menu m){
		try {
			Letra l=new Letra();
			return db.queryForObject("select menu_adicionar(?,?);",Boolean.class,l.Primera_Mayuscula(m.getNommen()),m.getIcomen());
		} catch (Exception e) {
			System.out.println("error al adicionar menu="+e.toString());
			return false;
		}
	}
	public boolean modificar(Menu m){
		try {
			Letra l=new Letra();
			return db.queryForObject("select menu_modificar(?,?,?);",Boolean.class,l.Primera_Mayuscula(m.getNommen()),m.getIcomen(),m.getCodmen());
		} catch (Exception e) {
			System.out.println("error al modificar menu="+e.toString());
			return false;
		}
	}
	public boolean darEstado(int codmen,boolean estmen){
		try {
			return db.queryForObject("select menu_darestado(?,?);",Boolean.class,codmen,estmen);
		} catch (Exception e) {
			System.out.println("error al eliminar menu="+e.toString());
			return false;
		}
	}
	public boolean asignar(int codmen,Integer procesos[]){
		try {
			db.update("delete from menpro where codmen=?",codmen);
			if(procesos!=null)
				for (int i = 0; i < procesos.length; i++)
					db.update("insert into menpro(codmen,codpro) values(?,?)",codmen,procesos[i]);
			return true;
		} catch (Exception e) {
			System.out.println("error al asignar menu-procesos="+e.toString());
			return false;
		}
	}
	public boolean validarNom(String nom){
		return db.queryForObject("select menu_validar(?)", Boolean.class,nom);
	}
}