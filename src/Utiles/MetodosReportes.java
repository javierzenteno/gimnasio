package Utiles;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class MetodosReportes extends JRDefaultScriptlet{
	public String obtenerLiteral(Double num)throws JRScriptletException{
		return "numero";
	}
}
