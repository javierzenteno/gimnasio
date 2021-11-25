package Controladores;

import java.text.SimpleDateFormat;
import java.util.Date;

public class prueba {

	public static void main(String[] args) {
		SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		System.out.println(fecha.format(new Date()));
	}

}
