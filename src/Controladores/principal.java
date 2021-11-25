package Controladores;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
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

import Modelos.Persona;
import Servicios.ConvocatoriaS;
import Servicios.FechaS;
import Servicios.GeneralS;
import Servicios.MenuS;
import Servicios.UsuarioS;
import Utiles.GeneradorReportes;
import Utiles.fechas;

@Controller
@RequestMapping("/principal/*")
public class principal {
	@Autowired
	UsuarioS usuarioS;
	@Autowired
	MenuS menuS;
	@Autowired
	GeneralS generalS;
	@Autowired
	ConvocatoriaS convo;
	@Autowired
	DataSource dataSource;
	@Autowired
	FechaS fechaS;
	@RequestMapping("login")
	public String login(Model model){
		model.addAttribute("msg","Sistema a su Servicio");
		return "principal/login";
	}
	@RequestMapping("convocatoria")
	public String convocatoria(Model model){
		model.addAttribute("convocatoria",convo.listara());
		return "principal/convocatoria";
	}
	@RequestMapping("ranking")
	public void ranking(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_ranking.jasper";
		int codtor=fechaS.ultimaf();
		parametros.put("codtor",codtor);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Ranking Departamental", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("jugador")
	public String jugador(Model model){
		model.addAttribute("jugador",convo.jugador());
		return "principal/jugador";
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validar(HttpServletRequest request,Model model,String username,String password){
		Persona person=usuarioS.iniciar_sesion(username, password);
		Map<String, Object> Data=new HashMap<>();
		if(person==null){
			Data.put("login_status", "invalid");
			Data.put("status", false);
		}else{
			HttpSession sesion=request.getSession();
			fechas f =new fechas();
			sesion.setAttribute("user", person);
			sesion.setAttribute("gestion", f.obteneraño());
			Data.put("login_status", "success");
			Data.put("redirect_url", "../principal/acceder");
			Data.put("status", true);
		}
		return Data;
	}
	@RequestMapping("acceder")
	public String acceder(HttpServletRequest request,Model model){
		HttpSession session=request.getSession();
		Persona person=(Persona)session.getAttribute("user");
		if(session==null || session.isNew()){
			model.addAttribute("msg", "Su sesion ah expirado!");
			return "principal/login";
		}
		
			if(person==null){
				model.addAttribute("msg", "Usuario no Autorizado!");
				return "principal/login";
			}
		int rol=-1;
		Enumeration<String> nombres=request.getParameterNames();
		String temp;
		Object rolo=null;
		while(nombres.hasMoreElements()){
			temp=nombres.nextElement();
			if(temp.trim().equalsIgnoreCase("xrol"))
				rolo=request.getParameter("xrol");
		}
		if(rolo!=null && !((String)rolo).equals("")){
			rol=Integer.parseInt((String)rolo);
		}
			model.addAttribute("user",person);
			model.addAttribute("menus",menuS.obtenerXusuario(person.getUsuario().getLogin(),rol));
			model.addAttribute("xrol",rol);
			model.addAttribute("gestion",generalS.obtener((int)session.getAttribute("gestion")));
			model.addAttribute("rol",usuarioS.obtenerRol(person.getUsuario().getLogin()));
			model.addAttribute("msg","Bienvenido, "+person.getNomper()+" "+person.getPriapper()+" al Sistema");
			return "principal/principal";
	}
	@RequestMapping("salir")
	public String salir(HttpServletRequest request,Model model){
		HttpSession sesion=request.getSession();
		Persona person=(Persona)sesion.getAttribute("user");
		model.addAttribute("username",person.getUsuario().getLogin());
		model.addAttribute("dato_user",person.getNomper()+" "+person.getPriapper()+" "+person.getSegapper());
		model.addAttribute("gestion",generalS.obtener((int)sesion.getAttribute("gestion")));
		sesion.invalidate();
		model.addAttribute("msg","Gracias por utilizar el Sistema");
		return "principal/usuario_sesion";
	}
}