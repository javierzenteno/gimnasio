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

import Modelos.Proceso;
import Servicios.ProcesoS;

@Controller
@RequestMapping("/proceso/*")
public class proceso {
	
	@Autowired
	ProcesoS procesoS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "proceso/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=procesoS.listar(start, estado, search,length);
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
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request,Model model,Proceso p)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", procesoS.adicionar(p));
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request,Model model,Proceso p)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", procesoS.modificar(p));
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,int codpro)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", procesoS.darEstado(codpro,false));
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,int codpro)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", procesoS.darEstado(codpro,true));
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nompro){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", procesoS.validarNom(nompro));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,int codpro){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", procesoS.obtener(codpro));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
}