	//Function que retorna el mes literal tomando en cuenta
	function ObtenerMesLiteral(mes){
		switch (mes) {
		case 1 :				
			return "Enero";
		case 2 :			
			return "Febrero";	
		case 3 :			
			return "Marzo";			
		case 4 :		
			return "Abril";			
		case 5 :		
			return "Mayo";		
		case 6 :			
			return "Junio";			
		case 7 :			
			return "Julio";			
		case 8 :		
			return "Agosto";			
		case 9 :			
			return "Septiembre";		
		case 10 :		
			return "Octubre";		
		case 11 :		
			return "Noviembre";			
		case 12 :			
			return "Diciembre";		
		default:			
			return null;	
		}
	}
	//Convierte el tiempo en formato local 
	function convertirFechaFormatoLocal(fecha){
		var dato=fecha.split('/');
		ms=Date.parse(dato[2]+'-'+dato[1]+'-'+dato[0]);
		var fec=new Date(ms);
		var options = {weekday: "long", year: "numeric", month: "long", day: "numeric"};
		return fec.toLocaleDateString("es-ES",options);
	}
	//convierte string a date formato dd/MM/yyyy
	function convertirDate(fecha){
		var dato=fecha.split('/');
		ms=Date.parse(dato[2]+'-'+dato[1]+'-'+dato[0]);
		var fec=new Date(ms);
		return fec;
	}
	//funcion para restar horas y devuelve formato hora y minuto
		function restarHorasTiempo(inicio,fin) {
		
		if(inicio.split(':')[1].length==1)inicio='0'+inicio;
		if(fin.split(':')[1].length==1)fin='0'+fin;
		  inicioMinutos = parseInt(inicio.substr(inicio.indexOf(':')+1,2));
		  inicioHoras = parseInt(inicio.substr(0,inicio.indexOf(':')));
		  finMinutos = parseInt(fin.substr(fin.indexOf(':')+1,2));
		  finHoras = parseInt(fin.substr(0,fin.indexOf(':')));

		  transcurridoMinutos = finMinutos - inicioMinutos;
		  transcurridoHoras = finHoras - inicioHoras;
		  if (transcurridoMinutos < 0) {
		    transcurridoHoras--;
		    transcurridoMinutos = 60 + transcurridoMinutos;
		  }
		  horas = transcurridoHoras.toString();
		  minutos = transcurridoMinutos.toString();
		  console.log('min='+minutos);
		  if (horas.length < 2) {
		    horas = "0"+horas;
		  }
		  
		  if (minutos.length < 2) {
		    minutos = "0"+minutos;
		  }
		  
		  return (horas+":"+minutos);

		}
	//funcion para restar y devuelve en horas
	function restarHoras(inicio,fin) {
		if(inicio.indexOf(':')==1)inicio='0'+inicio;
		if(fin.indexOf(':')==1)fin='0'+fin;
		  inicioMinutos = parseInt(inicio.substr(inicio.indexOf(':')+1,2));
		  inicioHoras = parseInt(inicio.substr(0,inicio.indexOf(':')));
		  
		  finMinutos = parseInt(fin.substr(fin.indexOf(':')+1,2));
		  finHoras = parseInt(fin.substr(0,fin.indexOf(':')));
		  transcurridoMinutos = finMinutos - inicioMinutos;
		  transcurridoHoras = finHoras - inicioHoras;
		  if (transcurridoMinutos < 0) {
		    transcurridoHoras--;
		    transcurridoMinutos = 60 + transcurridoMinutos;
		  }
		  
		  horas = transcurridoHoras.toString();
		  minutos = transcurridoMinutos.toString();
		  
		  if (horas.length < 2) {
		    horas = "0"+horas;
		  }
		  
		  if (minutos.length < 2) {
		    minutos = "0"+minutos;
		  }
		  var totalh=parseFloat(horas)+(parseFloat(minutos)/60);
		  return totalh;
		}
	//funcion para sumar horas
	function sumarHorasTiempo(t1, t2){
		var dot1 = t1.indexOf(":");
		var dot2 = t2.indexOf(":");
		var m1 = t1.substr(0, dot1);
		var m2 = t2.substr(0, dot2);
		var s1 = t1.substr(dot1 + 1);
		var s2 = t2.substr(dot2 + 1);
		var sRes = (Number(s1) + Number(s2));
		var mRes;
		var addMinute = false;
		if (sRes >= 60){
		addMinute = true;
		sRes -= 60;
		}
		mRes = (Number(m1) + Number(m2) + (addMinute? 1: 0));
		var cad1=String(mRes);
		var cad2=String(sRes);
		if(cad1.length<2)cad1='0'+cad1;
		if(cad2.length<2)cad2='0'+cad2;
		return  cad1+ ":" + cad2;
		}
	//funcion para calcular rangos de f inicio y f final
	function validarRango(i,f){
		if(i==''||f=='')return true;
		try{
			fi = new Date(i.substring(6,10),parseInt(i.substring(3,5))-1,i.substring(0,2));
			ff = new Date(f.substring(6,10),parseInt(f.substring(3,5))-1,f.substring(0,2));
			return ff>=fi;
		}
		catch(e){return false;}
	}
	// Función para calcular los días transcurridos entre dos fechas
	 function restaFechas(f1,f2)
	 {
	 var aFecha1 = f1.split('/'); 
	 var aFecha2 = f2.split('/'); 
	 var fFecha1 = Date.UTC(aFecha1[2],aFecha1[1]-1,aFecha1[0]); 
	 var fFecha2 = Date.UTC(aFecha2[2],aFecha2[1]-1,aFecha2[0]); 
	 var dif = fFecha2 - fFecha1;
	 var dias = Math.floor(dif / (1000 * 60 * 60 * 24)); 
	 return dias;
	 }
	 //funcion que suma dias a una fecha
	 sumaFecha = function(d, fecha)
	 {
		  var Fecha = new Date();
		  var sFecha = fecha || (Fecha.getDate() + "/" + (Fecha.getMonth() +1) + "/" + Fecha.getFullYear());
		  var sep = sFecha.indexOf('/') != -1 ? '/' : '-'; 
		  var aFecha = sFecha.split(sep);
		  var fecha = aFecha[2]+'/'+aFecha[1]+'/'+aFecha[0];
		  fecha= new Date(fecha);
		  fecha.setDate(fecha.getDate()+parseInt(d));
		  var anno=fecha.getFullYear();
		  var mes= fecha.getMonth()+1;
		  var dia= fecha.getDate();
		  mes = (mes < 10) ? ("0" + mes) : mes;
		  dia = (dia < 10) ? ("0" + dia) : dia;
		  var fechaFinal = dia+sep+mes+sep+anno;
		  return (fechaFinal);
	  }