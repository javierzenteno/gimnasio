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

import Modelos.General;
import Modelos.Persona;
import Servicios.GeneralS;
import Servicios.UsuarioS;

@Controller
@RequestMapping("/general/*")
public class general {
	
	@Autowired
	GeneralS generalS;
	@Autowired
	UsuarioS personaS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		Persona us = (Persona) request.getSession().getAttribute("user");
		if(us != null){
		model.addAttribute("dirnoadd", generalS.lista((int)request.getSession().getAttribute("gestion")));
		model.addAttribute("general",(int)request.getSession().getAttribute("gestion"));
		return "general/gestion";
		}else{
			return "principal/login";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=generalS.listar(start, estado, search,length);
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
	@RequestMapping("asignar")
	public @ResponseBody Map<String, Object> asignar(HttpServletRequest request,Model model,int codgen,Integer codper[],String cargo[])throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", generalS.asignar(codgen, codper, cargo));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,String nomgen,String telgen,String dirgen,String acrgen,float monto, MultipartFile loggen)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		General p=new General();
		p.setAcrgen(acrgen);
		p.setNomgen(nomgen);
		p.setTelgen(telgen);
		p.setDirgen(dirgen);
		if (us != null){
			String nombre;
			if(loggen!=null && loggen.getSize()>0){
				nombre="codgen-"+acrgen+loggen.getOriginalFilename().substring(loggen.getOriginalFilename().lastIndexOf('.'));
				File archivo=new File(request.getSession().getServletContext().getRealPath("/img")+"/"+nombre);
				try {
					loggen.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				p.setLoggen(nombre);
			}else{
				p.setLoggen("logo_blank.png");
			}
			Data.put("status", generalS.adicionar(p,monto,us.getCodper()));
		}else
			Data.put("status", false);
		return Data;		
	}
	@SuppressWarnings("deprecation")
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,String nomgen,String telgen,String dirgen,String acrgen,int codgen,MultipartFile loggen)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		General p=new General();
		p.setAcrgen(acrgen);
		p.setNomgen(nomgen);
		p.setTelgen(telgen);
		p.setDirgen(dirgen);
		p.setCodgen(codgen);
		Map<String, Object> pro=generalS.obtener(codgen);
		if (us != null){
			String nombre;
			if(loggen!=null && loggen.getSize()>0){
				nombre="codgen-"+acrgen+loggen.getOriginalFilename().substring(loggen.getOriginalFilename().lastIndexOf('.'));
				File archivo=new File(request.getSession().getServletContext().getRealPath("/img")+"/"+nombre);
				if(pro.get("loggen").toString().equals(nombre))new File(request.getRealPath("/img"+"/"+pro.get("loggen").toString())).delete();
				try {
					loggen.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				p.setLoggen(nombre);
			}else{
				p.setLoggen(pro.get("loggen").toString());
			}
			Data.put("status", generalS.modificar(p));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codgen)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", generalS.darEstado(codgen,false));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codgen)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", generalS.darEstado(codgen,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nompro){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", generalS.validarNom(nompro));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codgen){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", generalS.obtener(codgen));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("obtenerd")
	public @ResponseBody Map<String, Object> obtenerd(HttpServletRequest request,int codgen){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", generalS.obtener(codgen));
			Data.put("directiva", generalS.obtenerd(codgen));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
}