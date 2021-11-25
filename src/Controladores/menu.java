package Controladores;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Menu;
import Modelos.Persona;
import Servicios.MenuS;
import Servicios.ProcesoS;

@Controller
@RequestMapping("/menu/*")
public class menu {
	
	@Autowired
	MenuS menuS;
	@Autowired
	ProcesoS procesoS;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("procesos",procesoS.lista());
		return "menu/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=menuS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Menu m)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.adicionar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Menu m)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.modificar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codmen)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.darEstado(codmen,false));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codmen)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.darEstado(codmen,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nom_men){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.validarNom(nom_men));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codmen){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", menuS.obtener(codmen));
			Data.put("procesos",menuS.obtenerProcesos(codmen));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request,Model model,int codmen,Integer procesos[])throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.asignar(codmen,procesos));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("reasignar")
	public @ResponseBody Map<String, Object> reasignar(HttpServletRequest request,Model model,int codmen,Integer procesos2[])throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", menuS.asignar(codmen,procesos2));
		else
			Data.put("status", false);
		return Data;
	}
}