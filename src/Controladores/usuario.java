package Controladores;
//import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Persona;
import Servicios.RolS;
import Servicios.UsuarioS;
import Utiles.GeneradorReportes;

@Controller
@RequestMapping("/usuario/*")
public class usuario {
	
	@Autowired
	UsuarioS usuarioS;
	@Autowired
	RolS rolS;
	@Autowired
	DataSource dataSource;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("roles", rolS.lista());
		return "usuario/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = (request.getParameter("search[value]")==null?"":request.getParameter("search[value]"));
		List<?> lista=usuarioS.listar(start, estado, search,length);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if(!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Persona user)throws IOException{
		Persona us = (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null){
			user.setCodper(usuarioS.adicionar(user));
			Data.put("persona", user);
			Data.put("status", user.getCodper()>0);
	}else
			Data.put("status",false);
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Persona user)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us=(Persona)request.getSession().getAttribute("user");
		if(us != null){
			Data.put("status", usuarioS.modificar(user));
		}else{
			Data.put("status", false);
		}
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codper)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null)
			Data.put("status", usuarioS.darestado(codper,false));
		else{
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codper)throws IOException{
		Persona us= (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if ( us != null)
			Data.put("status", usuarioS.darestado(codper,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String ciper){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", usuarioS.validarCi(ciper));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codper){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> lista= usuarioS.obtenerRoles(usuarioS.obtenerloginxcodper(codper));
			Data.put("data", usuarioS.obtener(codper));
			Data.put("roles",lista);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request,Model model,int codper,String login,String password)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if( us != null){	
			if(usuarioS.asignar(codper,login,password))
				Data.put("status", true);
			else
				Data.put("status", false);
		}else{
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("reasignar")
	public @ResponseBody Map<String, Object> reasignar(HttpServletRequest request,Model model,int codper,String login,String password1)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if( us != null){
			if(usuarioS.reasignar(codper,login,password1))
				Data.put("status", true);
			else
				Data.put("status", false);
		}else{
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("asignarr")
	public @ResponseBody Map<String, Object> asignarr(HttpServletRequest request,Model model,int codper,Integer roles[])throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if( us != null)
			Data.put("status", usuarioS.asignarr(usuarioS.obtenerloginxcodper(codper),roles));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("reasignarr")
	public @ResponseBody Map<String, Object> reasignarr(HttpServletRequest request,Model model,int codper,Integer roles2[])throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if( us != null)
			Data.put("status", usuarioS.asignarr(usuarioS.obtenerloginxcodper(codper),roles2));
		else
			Data.put("status", false);	
		return Data;
	}
	@RequestMapping("ver")
	public @ResponseBody void ver (HttpServletRequest request,HttpServletResponse response,int codper){
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/persona_ver.jasper";
		parametros.put("codper",codper);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Persona", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
}