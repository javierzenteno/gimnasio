package Controladores;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import Modelos.Jugador;
import Modelos.Persona;
import Servicios.JugadorS;
import Servicios.ProvinciaS;
import Servicios.GeneralS;
import Utiles.GeneradorReportes;

 @Controller
 @RequestMapping("/jugador/*")
public class jugador {
	 @Autowired
	 DataSource dataSource;
	 @Autowired
	 JugadorS jugadorS;
	 @Autowired
	 ProvinciaS provinciaS;
	 @Autowired
	 GeneralS generalS;
	 @RequestMapping("gestion")
	 public String gestion(HttpServletRequest request,Model model){
		 model.addAttribute("personas",jugadorS.listapersonas());
		 model.addAttribute("persona", jugadorS.listaju());
		 model.addAttribute("provincias",provinciaS.lista());
		 return "jugador/gestion";
	 }
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length,String provincia)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=jugadorS.listar(start, estado, search,length,provincia);
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
	public @ResponseBody Map<String,Object> guardar(HttpServletRequest request,Model model,String codper,String codpro,MultipartFile fotjug,String madera,String gomad,String gomar) throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Jugador j=new Jugador();
		j.setCodper(Integer.parseInt(codper));
		j.setCodpro(Integer.parseInt(codpro));
		j.setMadera(madera);j.setGomad(gomad);j.setGomar(gomar);
		if (us != null){
			String nombre;
			if(fotjug!=null && fotjug.getSize()>0){
				nombre="codper-"+j.getCodper()+fotjug.getOriginalFilename().substring(fotjug.getOriginalFilename().lastIndexOf('.'));;
				File archivo=new File(request.getSession().getServletContext().getRealPath("/fotos")+"/"+nombre);
				try {
					fotjug.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j.setFotjug(nombre);
			}else{
				j.setFotjug("user.png");
			}
			Data.put("status", jugadorS.adicionar(j));
		}else
			Data.put("status", false);
		return Data;
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,String codjug,String codper,String codpro,MultipartFile fotjug,String madera,String gomad,String gomar)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Jugador j=new Jugador();
		j.setCodpro(Integer.parseInt(codpro));
		j.setCodjug(Integer.parseInt(codjug));
		j.setCodper(Integer.parseInt(codper));
		j.setMadera(madera);j.setGomad(gomad);j.setGomar(gomar);
		Map<String, Object> ju=jugadorS.obtener(j.getCodjug());
		if (us != null){
			String nombre;
		if(fotjug!=null && fotjug.getSize()>0){
			nombre="codper-"+j.getCodper()+fotjug.getOriginalFilename().substring(fotjug.getOriginalFilename().lastIndexOf('.'));;
			File archivo=new File(request.getSession().getServletContext().getRealPath("/fotos")+"/"+nombre);
			if(ju.get("fotjug").toString().equals(nombre))new File(request.getRealPath("/fotos"+"/"+ju.get("fotjug").toString())).delete();
			try {
				fotjug.transferTo(archivo);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j.setFotjug(nombre);
		}else{
			j.setFotjug(ju.get("fotjug").toString());
		}
			Data.put("status", jugadorS.modificar(j));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar (HttpServletRequest request,Model model,int codjug){
		Persona us = (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null){
			Data.put("status", jugadorS.darestado(false, codjug));
		}else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar (HttpServletRequest request,Model model,int codjug){
		Persona us = (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null){
			Data.put("status", jugadorS.darestado(true, codjug));
		}else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener (HttpServletRequest request,int codjug){
		Map<String, Object> Data = new HashMap<String, Object>();
		try{	
			Data.put("data", jugadorS.obtener(codjug));
			Data.put("status", true);
		}catch(Exception e){
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("edad")
	public @ResponseBody String edad (HttpServletRequest request,int codjug){
		int e=jugadorS.edad(codjug);
		if(e<=11){
			return "Sub-11";
		}
		if(e<=13){
			return "Sub-13";
		}
		if(e<=15){
			return "Sub-15";
		}
		if(e<=18){
			return "Sub-18";
		}
		if(e>18 && e<40){
			return "Mayores";
		}else
		return "Seniors";
		
	}
	@RequestMapping("ver")
	public @ResponseBody void ver (HttpServletRequest request,HttpServletResponse response,int codjug){
		Map<String, Object> parametros=new HashMap<String, Object>();
		HttpSession sesion=request.getSession();
		Map<String, Object> gestion=generalS.obtener((int)sesion.getAttribute("gestion"));
		String reportUrl="/Reportes/jugador_ver.jasper";
		parametros.put("codjug",codjug);
		parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos")+"\\");
		parametros.put("path2",request.getSession().getServletContext().getRealPath("/img")+"\\");
		parametros.put("logo", gestion.get("loggen").toString());
		parametros.put("codgen",Integer.parseInt(gestion.get("codgen").toString()));
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Jugador", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
}
