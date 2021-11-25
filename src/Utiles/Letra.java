package Utiles;


public class Letra {
	
	public String Primera_Mayuscula(String palabra){
		if(palabra==null)return "";
		String correct = "";
		
		if (!palabra.isEmpty() && !palabra.equals("")) {
		
			for (String string : palabra.toLowerCase().split(" ")) {
			
				correct += string.toUpperCase().charAt(0);
				
				for(int i = 0; i < string.length(); i++){
				
					if(i > 0) correct += string.charAt(i);
				
				}
			
				correct += " ";
			
			}
		
		}
		
		return correct.trim();
	
	}
}