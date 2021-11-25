package Controladores;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Modelos.Persona;
import Modelos.Torneo;
import Servicios.ConvocatoriaS;
import Servicios.FechaS;
import Utiles.GeneradorReportes;

@Controller
@RequestMapping("/liga/*")
public class liga {
	
	@Autowired
	FechaS fechaS;
	@Autowired
	ConvocatoriaS convocatoria;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("jugador",convocatoria.jugador());
		return "liga/gestion";
	}
	
	@RequestMapping("gestiont")
	public String gestiont(HttpServletRequest request,Model model){
		model.addAttribute("jugador",convocatoria.jugador());
		return "torneo/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, short estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=fechaS.listar(start, estado, search,length,1);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		
		return Data;

	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listat")
	public @ResponseBody Map<?, ?> listat(HttpServletRequest request, Integer draw, Integer start, short estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=fechaS.listar(start, estado, search,length,2);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		
		return Data;

	}
	@RequestMapping("fase")
	public String fase(HttpServletRequest request,Model model,long codtor,short estado){
		model.addAttribute("torneo", codtor);
		Map<String, Object> torneo=fechaS.obtener(codtor);
		short esttor=Short.parseShort(torneo.get("esttor").toString());short es=2;
		boolean aux=false;
		if(esttor!=es){
			aux=fechaS.darestado(codtor,es);
		}else{
			aux=true;
		}
		if(aux)
			return "liga/fase";
		else	
			return "liga/gestion";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listarf")
	public @ResponseBody Map<?, ?> listarf(HttpServletRequest request, Integer draw, Integer start, long codtor,int length,short estado)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = "";
		List<?> lista=fechaS.listarfase(start, codtor,length,search,estado);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		
		return Data;

	}
	@RequestMapping("grupo")
	public String grupo(HttpServletRequest request,Model model,long codfas,long codtor){
		model.addAttribute("fase", codfas);
		model.addAttribute("torneo", codtor);
		Map<String, Object> fase=fechaS.obtenerfase(codfas,codtor);
		short estfas=Short.parseShort(fase.get("estfas").toString());
		short estado=2;boolean aux=false;
		if(estfas!=estado){
			aux=fechaS.darestadof(codfas,estado);
		}else{
			aux=true;
		}
		if(aux)
			return "liga/grupo";
		else	
			return "liga/fase";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listarg")
	public @ResponseBody Map<?, ?> listarg(HttpServletRequest request, Integer draw, Integer start, long codfas,long codtor,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = "";
		List<?> lista=fechaS.listargrupo(start, codfas, codtor,length,search);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		return Data;

	}

	@RequestMapping("partido")
	public String partido(HttpServletRequest request,Model model,long codgru,long codfas,long codtor){
		model.addAttribute("grupo", codgru);
		model.addAttribute("fase", codfas);
		model.addAttribute("torneo", codtor);
		model.addAttribute("set", fechaS.listarset(codtor,codfas,codgru));
		model.addAttribute("partidos", fechaS.listarpartidos(codtor,codfas,codgru));
		Map<String, Object> grupo=fechaS.obtenergrupo(codgru,codfas,codtor);
		short estgru=Short.parseShort(grupo.get("estgru").toString());
		short estado=2;boolean aux=false;
		if(estgru!=estado){
			aux=fechaS.darestadog(codgru,estado);
		}else{
			aux=true;
		}
		if(aux)
			return "liga/partido";
		else	
			return "liga/grupo";
	}
	@RequestMapping("finall")
	public String finall(HttpServletRequest request,Model model, long codtor){
		short est=3;
		boolean aux=false;
		aux=fechaS.darestado(codtor,est);System.out.println(aux);
		if(aux)return "liga/gestion"; else return "liga/partido";
	}
//torneo
	@RequestMapping("faset")
	public String faset(HttpServletRequest request,Model model,long codtor,short estado){
		model.addAttribute("torneo", codtor);
		Map<String, Object> torneo=fechaS.obtener(codtor);
		short esttor=Short.parseShort(torneo.get("esttor").toString());
		short es=2;
		boolean aux=false;
		if(esttor!=es){
			aux=fechaS.darestado(codtor,es);
		}else{
			aux=true;
		}
		if(aux)
			return "torneo/fase";
		else	
			return "torneo/gestion";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listarft")
	public @ResponseBody Map<?, ?> listarft(HttpServletRequest request, Integer draw, Integer start, long codtor,int length,short estado)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = "";
		List<?> lista=fechaS.listarfase(start, codtor,length,search,estado);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		
		return Data;

	}
	@RequestMapping("grupot")
	public String grupot(HttpServletRequest request,Model model,long codfas,long codtor){
		model.addAttribute("fase", codfas);
		model.addAttribute("torneo", codtor);
		Map<String, Object> fase=fechaS.obtenerfase(codfas,codtor);
		short estfas=Short.parseShort(fase.get("estfas").toString());
		short estado=2;boolean aux=false;
		if(estfas!=estado){
			aux=fechaS.darestadof(codfas,estado);
		}else{
			aux=true;
		}
		if(aux)
			return "torneo/grupo";
		else	
			return "torneo/fase";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listargt")
	public @ResponseBody Map<?, ?> listargt(HttpServletRequest request, Integer draw, Integer start, long codfas,long codtor,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = "";
		List<?> lista=fechaS.listargrupo(start, codfas, codtor,length,search);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered",!search.equals("")?lista.size():total);
		return Data;

	}
	@RequestMapping("partidot")
	public String partidot(HttpServletRequest request,Model model,long codgru,long codfas,long codtor){
		model.addAttribute("grupo", codgru);
		model.addAttribute("fase", codfas);
		model.addAttribute("torneo", codtor);
		model.addAttribute("set", fechaS.listarset(codtor,codfas,codgru));
		model.addAttribute("partidos", fechaS.listarpartidos(codtor,codfas,codgru));
		Map<String, Object> grupo=fechaS.obtenergrupo(codgru,codfas,codtor);
		short estgru=Short.parseShort(grupo.get("estgru").toString());
		short estado=2;boolean aux=false;
		if(estgru!=estado){
			aux=fechaS.darestadog(codgru,estado);
		}else{
			aux=true;
		}
		if(aux)
			return "torneo/partido";
		else	
			return "torneo/grupo";
	}
	@RequestMapping("finalt")
	public String finalt(HttpServletRequest request,Model model, long codtor){
		short est=3;
		boolean aux=false;
		aux=fechaS.darestado(codtor,est);System.out.println(aux);
		if(aux)return "torneo/gestion"; else return "torneo/partido";
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Torneo p, int[] codjug,short[] postor)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		p.setCodgen((int)request.getSession().getAttribute("gestion"));p.setCodmod(1);
		Data.put("status",us!=null?fechaS.adicionar(p,codjug,postor,6):false);
		return Data;		
	}
	@RequestMapping("guardart")
	public @ResponseBody Map<String, Object> guardart(HttpServletRequest request,Model model,Torneo p, int[] codjug,short[] postor)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		p.setCodgen((int)request.getSession().getAttribute("gestion"));p.setCodmod(2);
		Data.put("status",us!=null?fechaS.adicionart(p,codjug,postor,3):false);
		return Data;		
	}
	@RequestMapping("set")
	public @ResponseBody Map<String, Object>set(HttpServletRequest request,Model model,long codpar,long codgru,long codfas,long codtor,int can)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		Data.put("status",us!=null?fechaS.set(codtor,codfas,codgru,codpar,can):false);
		return Data;
	}
	@RequestMapping("setmod")
	public @ResponseBody Map<String, Object>setmod(HttpServletRequest request,Model model,long codpar,long codgru,long codfas,long codtor,int ptsa,int codset)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		Data.put("status",us!=null?fechaS.setmod(codtor,codfas,codgru,codpar,ptsa,true,codset):false);
		return Data;
	}
	@RequestMapping("setmodi")
	public @ResponseBody Map<String, Object>setmodi(HttpServletRequest request,Model model,long codpar,long codgru,long codfas,long codtor,int ptsb,int codset)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		Data.put("status",us!=null?fechaS.setmod(codtor,codfas,codgru,codpar,ptsb,false,codset):false);
		return Data;
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Torneo p)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		Data.put("status", us!=null?fechaS.modificar(p):false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,long codtor)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Persona us = (Persona) request.getSession().getAttribute("user");
		Data.put("status", us!=null?fechaS.eliminar(codtor):false);
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codtor){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", fechaS.obtener(codtor));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@Autowired
	 DataSource dataSource;
	@RequestMapping("ver")
	public @ResponseBody void ver (HttpServletRequest request,HttpServletResponse response,int codtor){
		Map<String, Object> parametros=new HashMap<String, Object>();
		//HttpSession sesion=request.getSession();
		//Map<String, Object> gestion=generalS.obtener((int)sesion.getAttribute("gestion"));
		String reportUrl="/Reportes/lista_torneo.jasper";
		parametros.put("codtor",codtor);
		//parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos")+"\\");
		//parametros.put("path2",request.getSession().getServletContext().getRealPath("/img")+"\\");
		//parametros.put("logo", gestion.get("loggen").toString());
		//parametros.put("codgen",Integer.parseInt(gestion.get("codgen").toString()));
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Torneo", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
}