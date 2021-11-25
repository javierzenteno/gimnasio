package Controladores;
import java.io.File;
import java.io.IOException;
import java.util.Date;
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

import com.ibm.icu.text.SimpleDateFormat;


import Modelos.Documento;
import Modelos.Nota;
import Modelos.Persona;
import Servicios.NotaS;
import Utiles.GeneradorReportes;

@Controller
@RequestMapping("/nota/*")
public class nota {
	@Autowired
	DataSource dataSource;
	@Autowired
	NotaS documentoS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		Persona us = (Persona) request.getSession().getAttribute("user");
		if(us != null){
		model.addAttribute("gestion",(int) request.getSession().getAttribute("gestion"));
		model.addAttribute("fecha",new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		return "nota/gestion";
		}else{
			return "principal/login";
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length,boolean tipnot)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=documentoS.listar(start, estado, search,length,tipnot);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,int codgen,String titnot,boolean tipnot,String ubidoc,String emitente,String remitente,String obsnot, MultipartFile pdfdoc)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Nota c=new Nota();
		Documento d=new Documento();
		c.setEmitente(emitente);
		c.setTipnot(tipnot);
		c.setTitnot(titnot);
		c.setObsnot(obsnot);
		c.setRemitente(remitente);
		d.setCodgen(codgen);
		d.setUbidoc(ubidoc);
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null){
			d.setCodpercre(us.getCodper());
			String nombre;
			nombre="codnot-"+documentoS.genera()+pdfdoc.getOriginalFilename().substring(pdfdoc.getOriginalFilename().lastIndexOf('.'));
			File archivo=new File(request.getSession().getServletContext().getRealPath("/pdf")+"/"+nombre);
			try {
				pdfdoc.transferTo(archivo);
			} catch (IllegalStateException e) {
				// TODO: handle exception
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			d.setPdfdoc(nombre);
			Data.put("status", documentoS.adicionar(c,d));
		}else
			Data.put("status", false);
		return Data;		
	}
	@SuppressWarnings("deprecation")
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,int codnot,long coddoc,int codgen,String titnot,boolean tipnot1,String ubidoc,String emitente,String remitente,String obsnot, MultipartFile pdfdoc1)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Nota c=new Nota();
		Documento d=new Documento();
		c.setEmitente(emitente);
		c.setRemitente(remitente);
		c.setObsnot(obsnot);
		c.setTipnot(tipnot1);
		c.setTitnot(titnot);
		c.setCodnot(codnot);
		d.setCoddoc(coddoc);
		d.setCodgen(codgen);
		d.setUbidoc(ubidoc);
		Map<String, Object> cn=documentoS.obtener(codnot);
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null){
			d.setCodpermod(us.getCodper());
			String nombre;
			if(pdfdoc1!=null && pdfdoc1.getSize()>0){
				nombre="codnot-"+codnot+pdfdoc1.getOriginalFilename().substring(pdfdoc1.getOriginalFilename().lastIndexOf('.'));
				File archivo=new File(request.getSession().getServletContext().getRealPath("/pdf")+"/"+nombre);
				if(cn.get("pdfdoc").toString().equals(nombre))new File(request.getRealPath("/pdf"+"/"+cn.get("pdfdoc").toString())).delete();
				try {
					pdfdoc1.transferTo(archivo);
				} catch (IllegalStateException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				d.setPdfdoc(nombre);
			}else{
				d.setPdfdoc(cn.get("pdfdoc").toString());
			}
			Data.put("status", documentoS.modificar(d,c));
		}else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codnot)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", documentoS.darEstado(codnot,false));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codnot)throws IOException{
		Persona us = (Persona) request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", documentoS.darEstado(codnot,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nomdoc){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", documentoS.validarNom(nomdoc));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codnot){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", documentoS.obtener(codnot));
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