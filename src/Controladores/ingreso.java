package Controladores;
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

import Modelos.Ingreso;
import Modelos.Persona;
import Servicios.IngresoS;
import Utiles.GeneradorReportes;

@Controller
@RequestMapping("/ingreso/*")
public class ingreso {
	@Autowired
	DataSource dataSource;
	@Autowired
	IngresoS ingresoS;
	@Autowired
	principal p;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		Persona us = (Persona) request.getSession().getAttribute("user");
		if(us != null){
		model.addAttribute("codper",us.getCodper());
		model.addAttribute("general",(int)request.getSession().getAttribute("gestion"));
		model.addAttribute("caja",ingresoS.obtenercaja((int)request.getSession().getAttribute("gestion")));
		return "ingreso/gestion";
		}else{
			return p.acceder(request, model);
		}
	}
	@RequestMapping("caja")
	public @ResponseBody Map<String, Object> caja(HttpServletRequest request){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", ingresoS.obtenercaja((int)request.getSession().getAttribute("gestion")));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtenercaja="+e.toString());
		}
		return Data;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length,String fecing)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=ingresoS.listar(start, estado, search,length,fecing);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			System.out.println("lista"+e.toString());
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Ingreso d)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null){
			Data.put("status", ingresoS.adicionar(d,(int)request.getSession().getAttribute("gestion")));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Ingreso d)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null){
			Data.put("status", ingresoS.modificar(d,(int)request.getSession().getAttribute("gestion")));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int coding)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", ingresoS.darEstado(coding,false,(int)request.getSession().getAttribute("gestion")));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int coding)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", ingresoS.darEstado(coding,true,(int)request.getSession().getAttribute("gestion")));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nomdoc){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", ingresoS.validarNom(nomdoc));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int coding){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", ingresoS.obtener(coding));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("ver")
	public @ResponseBody void ver (HttpServletRequest request,HttpServletResponse response,int coddoc){
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/documento_ver.jasper";
		parametros.put("coddoc",coddoc);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Documento", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
}