package Utiles;

public class paginacion {
	public int num_pag(int tam){
		if(tam%10==0)return tam/10;
		return (tam/10)+1;
	}
}
