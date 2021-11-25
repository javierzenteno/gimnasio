package Utiles;

public class Vectores {
	public String convertir_Int_a_String(int vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertir_float_a_String(float vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
			cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
	public String convertir_String_a_Vector(String vec[]){
		String cad="{";
		if(vec.length==0)return "{}";
		for (int i = 0; i < vec.length; i++) 
				cad+=vec[i]+",";
		return cad.substring(0, cad.length()-1)+"}";
	}
}
