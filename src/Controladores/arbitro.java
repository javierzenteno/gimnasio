package Controladores;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

import Modelos.Arbitro;
import Modelos.Persona;
import Servicios.ArbitroS;
import Utiles.GeneradorReportes;

 @Controller
 @RequestMapping("/arbitro/*")
public class arbitro {
	 @Autowired
	 DataSource dataSource;
	 @Autowired
	 ArbitroS arbitroS;
	 @RequestMapping("gestion")
	 public String gestion(HttpServletRequest request,Model model){
		 model.addAttribute("personas",arbitroS.listapersonas());
		 model.addAttribute("persona", arbitroS.listaen());
		 return "arbitro/gestion";
	 }
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=arbitroS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String,Object> guardar(HttpServletRequest request,Model model,String codper,String ranarb,MultipartFile fotarb) throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Arbitro j=new Arbitro();
		j.setCodper(Integer.parseInt(codper));
		j.setRanarb(Short.parseShort(ranarb));
		if (us != null){
			String nombre;
			if(fotarb!=null && fotarb.getSize()>0){
				nombre="codper-"+j.getCodper()+fotarb.getOriginalFilename().substring(fotarb.getOriginalFilename().lastIndexOf('.'));;
				File archivo=new File(request.getSession().getServletContext().getRealPath("/fotos")+"/"+nombre);
				try {
					fotarb.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j.setFotarb(nombre);
			}else{
				j.setFotarb("user.png");
			}
			Data.put("status", arbitroS.adicionar(j));
		}else
			Data.put("status", false);
		return Data;
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,String codarb,String codper,String ranarb,MultipartFile fotarb)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Arbitro j=new Arbitro();
		j.setCodarb(Integer.parseInt(codarb));
		j.setCodper(Integer.parseInt(codper));
		j.setRanarb(Short.parseShort(ranarb));
		Map<String, Object> ju=arbitroS.obtener(j.getCodarb());
		if (us != null){
			String nombre;
		if(fotarb!=null && fotarb.getSize()>0){
			nombre="codper-"+j.getCodper()+fotarb.getOriginalFilename().substring(fotarb.getOriginalFilename().lastIndexOf('.'));;
			File archivo=new File(request.getSession().getServletContext().getRealPath("/fotos")+"/"+nombre);
			if(ju.get("fotarb").toString().equals(nombre))new File(request.getRealPath("/fotos"+"/"+ju.get("fotarb").toString())).delete();
			try {
				fotarb.transferTo(archivo);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j.setFotarb(nombre);
		}else{
			j.setFotarb(ju.get("fotarb").toString());
		}
			Data.put("status", arbitroS.modificar(j));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar (HttpServletRequest request,Model model,int codarb){
		Persona us = (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null){
			Data.put("status", arbitroS.darestado(false, codarb));
		}else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar (HttpServletRequest request,Model model,int codarb){
		Persona us = (Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if(us != null){
			Data.put("status", arbitroS.darestado(true, codarb));
		}else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener (HttpServletRequest request,int codarb){
		Map<String, Object> Data = new HashMap<String, Object>();
		try{	
			Data.put("data", arbitroS.obtener(codarb));
			Data.put("status", true);
		}catch(Exception e){
			Data.put("status", false);
		}
		return Data;
	}
	@RequestMapping("edad")
	public @ResponseBody String edad (HttpServletRequest request,int codarb){
		int e=arbitroS.edad(codarb);
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
	public @ResponseBody void ver (HttpServletRequest request,HttpServletResponse response,int codarb){
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/arbitro_ver.jasper";
		parametros.put("codarb",codarb);
		parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos")+"\\");
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "arbitro", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
}
