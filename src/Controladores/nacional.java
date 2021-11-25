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

import Modelos.Nacional;
import Modelos.DetalleNacional;
import Modelos.Persona;
import Servicios.JugadorS;
import Servicios.NacionalS;
import Servicios.DetalleNacionalS;

@Controller
@RequestMapping("/nacional/*")
public class nacional {
	
	@Autowired
	JugadorS jugadorS;
	@Autowired
	NacionalS nacionalS;
	@Autowired
	DetalleNacionalS detallenacionalS;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("jugador",jugadorS.listaju());
		model.addAttribute("categorias", nacionalS.categorias());
		return "nacional/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=nacionalS.listar(start, estado, search,length);
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
	@SuppressWarnings("unchecked")
	@RequestMapping("lista_n")
	public @ResponseBody Map<?, ?> lista_n(HttpServletRequest request, Integer draw, Integer start,int length,String nac)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		int nacional=-1;
		if(nac!=null)nacional=Integer.parseInt(nac);
		List<?> lista=detallenacionalS.listar(start, search,length,nacional);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Nacional m)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		m.setCodgen((int)request.getSession().getAttribute("gestion"));
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", nacionalS.adicionar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Nacional m)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", nacionalS.modificar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codnac)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", nacionalS.darEstado(codnac,false));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codnac)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", nacionalS.darEstado(codnac,true));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nom_nac){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", nacionalS.validarNom(nom_nac));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codnac){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", nacionalS.obtener(codnac));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("guardar_d")
	public @ResponseBody Map<String, Object> guardar_d(HttpServletRequest request,Model model,DetalleNacional m)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", detallenacionalS.adicionar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("actualizar_d")
	public @ResponseBody Map<String, Object> actualizar_d(HttpServletRequest request,Model model,DetalleNacional m,int codcat1)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		m.setCodcat(codcat1);
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", detallenacionalS.modificar(m));
		else
			Data.put("status", false);
		return Data;		
	}
	@RequestMapping("eliminar_d")
	public @ResponseBody Map<String, Object> eliminar_d(HttpServletRequest request,Model model,int codnac,int codjug)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null)
			Data.put("status", detallenacionalS.darEstado(codnac,codjug));
		else
			Data.put("status", false);
		return Data;
	}
	@RequestMapping("obtener_d")
	public @ResponseBody Map<String, Object> obtener_d(HttpServletRequest request,int codnac,int codjug){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", detallenacionalS.obtener(codnac,codjug));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("guardar_c")
	public @ResponseBody Map<String, Object> guardar_c(HttpServletRequest request,Model model,String nomcat)throws IOException{
		Persona us=(Persona)request.getSession().getAttribute("user");
		Map<String, Object> Data = new HashMap<String, Object>();
		if (us != null){
			int cod=nacionalS.adicionarcategoria(nomcat);
			Data.put("codcat", cod);
			Data.put("nomcat", nomcat);
			Data.put("status", cod>0);
		}else
			Data.put("status", false);
		return Data;		
	}
}