package Servicios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;

import Modelos.Usuario;
import Modelos.Persona;
import Utiles.Db_Coneccion;
import Utiles.Letra;
import Utiles.fechas;
import Utiles.procedure_stored;
//import Utiles.encriptar;

@Service
public class UsuarioS extends Db_Coneccion{
	
	procedure_stored c;
	
	@Autowired
	RolS rolS;
	
	String sql;
	
	public class a_objeto implements ParameterizedRowMapper<Persona>{
		@Override
		public Persona mapRow(ResultSet set, int index) throws SQLException {
			// TODO Auto-generated method stub
			Persona u=new Persona();
			u.setCodper(set.getInt("codper"));
			u.setCiper(set.getString("ciper"));
			u.setNomper(set.getString("nomper"));
			u.setPriapper(set.getString("priapper"));
			u.setSegapper(set.getString("segapper"));
			u.setTelper(set.getString("telper"));
			u.setDirper(set.getString("dirper"));
			u.setEstper(set.getBoolean("estper"));
			u.setFecnacper(set.getString("fecnacper"));
			try{
				u.setUsuario(obtenerxcodper(set.getInt("codper")));
			}catch(Exception e){
				u.setUsuario(null);
			}
			return u;
		}
	}
	public class a_objeto_u implements ParameterizedRowMapper<Usuario>{
		@Override
		public Usuario mapRow(ResultSet set, int index) throws SQLException {
			// TODO Auto-generated method stub
			Usuario u=new Usuario();
			u.setCodper(set.getInt("codper"));
			u.setLogin(set.getString("login"));
			u.setPassword(set.getString("password"));
			u.setEstusu(set.getBoolean("estusu"));
			
			return u;
		}
	}
	public String obtenerloginxcodper (int codper){
		try{
			return db.queryForObject("select usuario.login from usuario where usuario.codper=?;", String.class,codper);
		}catch(Exception e){
			System.out.println("Error al obtener login x codper :"+e.toString());
			return null;
		}
	}
	public Usuario obtenerxcodper (int codper){
		try{
			return db.queryForObject("select * from usuario_obtenerxcodper(?)"+as_usuario,new a_objeto_u(),codper);
		}catch(Exception e){
			System.out.println("error al obtener usuario x codper :"+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerRol(String login){
		try{
			return db.queryForList("select * from usuario_obtenerrol(?)"+as_rol,login);
		}catch(Exception e){
			System.out.println("error al obtener roles x login :"+e.toString());
			return null;
	}
	}
	public Persona iniciar_sesion(String login,String password){
		try {
			return db.queryForObject("select * from usuario_iniciar_sesion(?,?)"+as_persona,new a_objeto(),login,password);
		} catch (Exception e) {
			System.out.println("error al iniciar sesion : "+e.toString());
			return null;
		}
	}
//	public boolean guardarFoto(String foto,int codper){
//		try {
//			db.queryForObject("select persona_guardarfoto(?,?) as resp",Integer.class,foto,codper);
//			return true;
//		} catch (Exception e) {
//			System.out.println(""+e.toString());
//			return false;
//		}
//	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		try{	
			return db.queryForList("select * from usuario_lista(?,?,?,?)"+as_object_add(as_persona,"RN BIGINT,Tot INT,clave BIGINT,roles BIGINT"),start,length,search,estado);
		} catch (Exception e) {
			System.out.println("error listar usuarios="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(int codper){
		try {
			return db.queryForMap("select * from usuario_obtener(?)"+as_object_add(as_persona,"login varchar,password varchar"),codper);
		} catch (Exception e) {
			System.out.println("error obtenerUsuario="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerRoles(String login){
		try {
			return db.queryForList("select rol.*,case when (select count(*) from usurol where login=? and usurol.codrol=rol.codrol)>0 THEN 'selected' else '' end as sel from rol where rol.estrol=true;",login);
		} catch (Exception e) {
			System.out.println("error obtenerProcesos="+e.toString());
			return null;
		}
	}
	public int adicionar(Persona us){
		try {
			Letra l=new Letra();
			fechas f=new fechas();
			Date fechac=null;
			fechac=f.convertirStringDate(us.getFecnacper(), "dd/MM/yyyy");
			return db.queryForObject("select persona_adicionar(?,?,?,?,?,?,?,?);",Integer.class,us.getCiper(),l.Primera_Mayuscula(us.getNomper()),l.Primera_Mayuscula(us.getPriapper()),l.Primera_Mayuscula(us.getSegapper()),us.getDirper(),us.getTelper(),fechac,us.isGenper());
		} catch (Exception e) {
			System.out.println("error al adicionar persona="+e.toString());
			return -1;
		}
	}
	public boolean modificar(Persona us){
		try {
			Letra l=new Letra();
			fechas f=new fechas();
			Date fechac=null;
			fechac=f.convertirStringDate(us.getFecnacper(), "dd/MM/yyyy");
			return db.queryForObject("select persona_modificar(?,?,?,?,?,?,?,?,?);",Boolean.class,us.getCiper(),l.Primera_Mayuscula(us.getNomper()),l.Primera_Mayuscula(us.getPriapper()),l.Primera_Mayuscula(us.getSegapper()),us.getDirper(),us.getTelper(),fechac,us.isGenper(),us.getCodper());
		} catch (Exception e) {
			System.out.println("error al modificar persona="+e.toString());
			return false;
		}
	}
	public boolean darestado(int codper,boolean estado){
		try {
			return db.queryForObject("select persona_darestado(?,?)",Boolean.class,codper,estado);
		} catch (Exception e) {
			System.out.println("error al eliminar persona="+e.toString());
			return false;
		}
	}
	public boolean asignar(int codper,String login,String password){
		try {
			db.queryForObject("select usuario_asignar(?,?,?)",Boolean.class,codper,login,password);
			return true;
		} catch (Exception e) {
			System.out.println("error al asignar persona"+e.toString());
			return false;
		}
	}
	
	public boolean reasignar(int codper,String login,String password){
		try {
			db.queryForObject("select usuario_reasignar(?,?,?)",Boolean.class,login,password,codper);
			return true;
		} catch (Exception e) {
			System.out.println("error al reasignar persona"+e.toString());
			return false;
		}
	}
	public boolean asignarr(String login,Integer roles[]){
		try {
			db.update("delete from usurol where login=?",login);
			if(roles!=null)
				for (int i = 0; i < roles.length; i++)
					db.update("insert into usurol(login,codrol) values(?,?)",login,roles[i]);
			return true;
		} catch (Exception e) {
			System.out.println("error al asignar rol-menu="+e.toString());
			return false;
		}
	}
	public boolean validarCi(String ci){
		return db.queryForObject("select persona_validarci(?)", Boolean.class,ci);
	}
}