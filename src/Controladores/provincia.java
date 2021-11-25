package Controladores;
import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

import Modelos.Provincia;
import Modelos.Persona;
import Servicios.ProvinciaS;
import Servicios.UsuarioS;

@Controller
@RequestMapping("/provincia/*")
public class provincia {
	
	@Autowired
	ProvinciaS provinciaS;
	@Autowired
	UsuarioS personaS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("personas", personaS.listar(-1, true, "", 0));
		return "provincia/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=provinciaS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,String nompro,String acropro,int codper, MultipartFile logpro)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Provincia p=new Provincia();
		p.setAcropro(acropro);
		p.setNompro(nompro);
		p.setCodper(codper);
		if (us != null){
			String nombre;
			if(logpro!=null && logpro.getSize()>0){
				nombre="codpro-"+acropro+logpro.getOriginalFilename().substring(logpro.getOriginalFilename().lastIndexOf('.'));
				File archivo=new File(request.getSession().getServletContext().getRealPath("/img")+"/"+nombre);
				try {
					logpro.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				p.setLogpro(nombre);
			}else{
				p.setLogpro("logo_blank.png");
			}
			Data.put("status", provinciaS.adicionar(p));
		}else
			Data.put("status", false);
		return Data;		
	}
	@SuppressWarnings("deprecation")
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,String nompro,String acropro,int codper,int codpro,MultipartFile logpro)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		Provincia p=new Provincia();
		p.setAcropro(acropro);
		p.setNompro(nompro);
		p.setCodper(codper);
		p.setCodpro(codpro);
		Map<String, Object> pro=provinciaS.obtener(codpro);
		if (us != null){
			String nombre;
			if(logpro!=null && logpro.getSize()>0){
				nombre="codpro-"+acropro+logpro.getOriginalFilename().substring(logpro.getOriginalFilename().lastIndexOf('.'));
				File archivo=new File(request.getSession().getServletContext().getRealPath("/img")+"/"+nombre);
				if(pro.get("logpro").toString().equals(nombre))new File(request.getRealPath("/img"+"/"+pro.get("logpro").toString())).delete();
				try {
					logpro.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				p.setLogpro(nombre);
			}else{
				p.setLogpro(pro.get("logpro").toString());
			}
			Data.put("status", provinciaS.modificar(p));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codpro)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", provinciaS.darEstado(codpro,false));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codpro)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", provinciaS.darEstado(codpro,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nompro){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", provinciaS.validarNom(nompro));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codpro){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", provinciaS.obtener(codpro));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
}