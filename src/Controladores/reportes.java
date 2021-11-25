package Controladores;

import java.io.IOException;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;

import Servicios.FechaS;
import Servicios.GeneralS;
//import Servicios.LigaS;
import Servicios.NacionalS;
import Servicios.ProvinciaS;
import Utiles.GeneradorReportes;

@Controller
@RequestMapping("/reportes/*")
public class reportes {
	@Autowired
	DataSource dataSource;
	@Autowired
	ProvinciaS provinciaS;
	@Autowired
	FechaS ligaS;
	@Autowired
	NacionalS nacionalS;
	@Autowired
	GeneralS generalS;
	@RequestMapping("gestion")
	public String reporte1(Model model){
		model.addAttribute("provincias", provinciaS.listar(-1, true, "", 0));
		model.addAttribute("nacionales", nacionalS.listar(-1, true,"", 0));
		short est=3;
		model.addAttribute("fechas", ligaS.listar(-1,est, "", 0, 1));
		model.addAttribute("torneo", ligaS.listar(-1,est, "", 0, 2));
		model.addAttribute("gestiones", generalS.listar(-1, true, "", 0));
		return "reporte/reporte1";
	}
	@RequestMapping("reporte1")
	public void reporte_jugadores(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_jugadores.jasper";
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de jugadores", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte2")
	public void reporte_jugadoresxcat(HttpServletRequest request,HttpServletResponse response,int cat)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_categoria.jasper";
		if (cat==11) {
			parametros.put("edad", cat);
			parametros.put("edad2", (cat-1));
		}
		if (cat==13) {
			parametros.put("edad", cat);
			parametros.put("edad2", (cat-1));
		}
		if (cat==15) {
			parametros.put("edad", cat);
			parametros.put("edad2", (cat-1));
		}
		if (cat==18) {
			parametros.put("edad", cat);
			parametros.put("edad2", (cat-2));
		}
		if (cat==21) {
			parametros.put("edad", cat);
			parametros.put("edad2", (cat-2));
		}
		if (cat==40) {
			parametros.put("edad", cat);
			parametros.put("edad2", (22));
		}
		if (cat==80) {
			parametros.put("edad", cat);
			parametros.put("edad2", (41));
		}
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Jugadores por Categoria", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte3")
	public void reporte_arbitros(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_arbitros.jasper";
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Árbitros", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte4")
	public void reporte_entrenadores(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_entrenadores.jasper";
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Entrenadores", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("error+"+e.toString());
		}
	}
	@RequestMapping("reporte5")
	public void reporte_jugadorxpro(HttpServletRequest request,HttpServletResponse response,int pro)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_provincia.jasper";
		parametros.put("codpro", pro);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de cobros", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte6")
	public void reporte_carnet(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/jugador_carnet.jasper";
		String Surl=getClass().getResource("/Reportes/jugador_ver.jasper").toString();
		parametros.put("subrep",Surl.substring(0, Surl.lastIndexOf("/"))+"/");
		parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos")+"\\");
		parametros.put("path2",request.getSession().getServletContext().getRealPath("/img")+"\\");
		HttpSession sesion=request.getSession();
		Map<String, Object> gestion=generalS.obtener((int)sesion.getAttribute("gestion"));
		parametros.put("logo", gestion.get("loggen").toString());
		GeneradorReportes rep=new GeneradorReportes();	
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Carnet de Jugadores", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
/*	@RequestMapping("list_fecha")
	public @ResponseBody Map<?,?> fecha(HttpServletRequest request,int lig1){
		Map<String, Object> Data = new HashMap<String,Object>();
		List<?> Dato=ligaS.fecha_reporte(lig1);
		Data.put("data", Dato);
		return Data;
	}*/
	@RequestMapping("reporte7")
	public void reporte_torneo(HttpServletRequest request,HttpServletResponse response,int codtor)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_torneo.jasper";
		parametros.put("codtor",codtor);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Torneo", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte8")
	public void reporte_liga(HttpServletRequest request,HttpServletResponse response,int codtor)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_torneo.jasper";
		parametros.put("codtor",codtor);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Fecha de la Liga", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte9")
	public void reporte_nacionales(HttpServletRequest request,HttpServletResponse response,int nac)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_nacional.jasper";
		parametros.put("codnac",nac);
		String Surl=getClass().getResource("/Reportes/lista_nacional_subreport1.jasper").toString();
		parametros.put("subrep",Surl.substring(0, Surl.lastIndexOf("/"))+"/");
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Nacionales", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("error+"+e.toString());
		}
	}
	@RequestMapping("reporte10")
	public void reporte_notas(HttpServletRequest request,HttpServletResponse response,String fnot)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String[] fe=fnot.split("-");
		String fini=fe[0];
		String ffin=fe[1];
		String reportUrl="/Reportes/documento_ver.jasper";
		parametros.put("fini",fini);
		parametros.put("ffin",ffin);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Entrenadores", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("error+"+e.toString());
		}
	}
	@RequestMapping("reporte11")
	public void reporte_convocatorias(HttpServletRequest request,HttpServletResponse response,String fcon)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String[] fe=fcon.split("-");
		String fini=fe[0];
		String ffin=fe[1];
		String reportUrl="/Reportes/lista_convocatorias.jasper";
		parametros.put("fini",fini);
		parametros.put("ffin",ffin);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Convocatorias", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("error+"+e.toString());
		}
	}
	@RequestMapping("reporte12")
	public void reporte_ingreso(HttpServletRequest request,HttpServletResponse response,int codgen)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_ingreso.jasper";
		parametros.put("codgen",codgen);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Ingresos", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte14")
	public void reporte_egreso(HttpServletRequest request,HttpServletResponse response,int codgen)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_egreso.jasper";
		parametros.put("codgen",codgen);
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Egresos", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("ewrror+"+e.toString());
		}
	}
	@RequestMapping("reporte13")
	public void reporte_municipal(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, Object> parametros=new HashMap<String, Object>();
		String reportUrl="/Reportes/lista_municipales.jasper";
		parametros.put("path",request.getSession().getServletContext().getRealPath("/img")+"\\");
		GeneradorReportes rep=new GeneradorReportes();		
		try{		
			rep.generarReporte(response, getClass().getResource(reportUrl), "pdf", parametros, dataSource.getConnection(), "Lista de Asoc. Municipales", "inline");	
		} catch (Exception e) {
			e.printStackTrace();System.out.println("error+"+e.toString());
		}
	}
}