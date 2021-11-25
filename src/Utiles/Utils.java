package Utiles;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public abstract class Utils {

	public static String SubirArchivo(MultipartFile archivo,String nombre,String repositorio){
		if(archivo!=null && archivo.getSize()>0){
			try {
  	              archivo.transferTo(new File(repositorio+nombre));
  	    	   	  return nombre;
			} catch (IllegalStateException|IOException e) {
  	    	   		e.printStackTrace();	
  	    	}
		}
		return null;
	}
	
	public static void deleteFile(HttpServletRequest r,String folder,String filename) {			
		new File(r.getSession().getServletContext().getRealPath("images") + "/"+folder+"/" + filename).delete();	
	}	
	
	public static String generarAlias(String nombres,String PriApe,String SegApe){
		String[]noms=nombres.split(" ");
		String alias="";
		if(noms.length==1)alias+=nombres.substring(0,2);
		else for(int i=0;i<noms.length;i++){
					if(noms[i].length()>2)alias+=noms[i].substring(0,1);	
				}
		if(SegApe!=null&&SegApe.length()>1)alias+=PriApe.substring(0,2)+SegApe.substring(0,2);
		else try{alias+=PriApe.substring(0,4);}catch(Exception e){alias+=PriApe;}
			
		return alias.toUpperCase();
	}
}

