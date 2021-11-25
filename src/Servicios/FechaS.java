package Servicios;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Modelos.Grupo;
import Modelos.Partido;
import Modelos.Torneo;
import Modelos.Fase;
import Utiles.Db_Coneccion;
@Service 
public class FechaS extends Db_Coneccion{


	public List<Map<String, Object>> listar(int start,short estado,String search,int length,int codmod){
		try{
		if(search==null)search="";
		return db.queryForList("select * from torneo_lista(?,?,?,?,?)"+as_object_add(as_torneo,start<0?"":"RN BIGINT,Tot INT"),start,length,search,estado,codmod);
		} catch(Exception e){
			System.out.println("error listar torneo="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listarfase(int start,long codtor,int length,String search,short estado){
		try{
		return db.queryForList("select * from fase_lista(?,?,?,?,?)"+as_object_add(as_fase,start<0?"":"RN BIGINT,Tot INT"),start,length,codtor,search,estado);
		} catch(Exception e){
			System.out.println("error listar fase="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listargrupo(int start,long codfas,long codtor,int length,String search){
		try{
		return db.queryForList("select * from grupo_lista(?,?,?,?,?)"+as_object_add(as_grupo,start<0?"":"RN BIGINT,Tot INT"),start,length,codfas,codtor,search);
		} catch(Exception e){
			System.out.println("error listar grupo="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listarset(long codtor,long codfas,long codgru){
		try{
		return db.queryForList("select * from partido_lista(?,?,?)"+as_object_add(as_set,"Tot INT"),codtor,codfas,codgru);
		} catch(Exception e){
			System.out.println("error listar partido="+e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> listarpartidos(long codtor,long codfas,long codgru){
		try{
		return db.queryForList("select * from partido_listar(?,?,?)"+as_object_add(as_partido,"cant bigint,Tot INT"),codtor,codfas,codgru);
		} catch(Exception e){
			System.out.println("error listar partido="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(long codtor){
		try {
			return db.queryForMap("select * from torneo_obtener(?)"+as_torneo,codtor);
		} catch (Exception e) {
			System.out.println("error obtener fecha de liga="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtenerfase(long codfas,long codtor){
		try {
			return db.queryForMap("select * from fase_obtener(?,?)",codfas,codtor);
		} catch (Exception e) {
			System.out.println("error obtener fase de liga="+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtenergrupo(long codgru,long codfas, long codtor){
		try {
			return db.queryForMap("select * from grupo_obtener(?,?,?)",codgru,codfas,codtor);
		} catch (Exception e) {
			System.out.println("error obtener grupo de liga="+e.toString());
			return null;
		}
	}
	public boolean modificar(Torneo t){
		try{
			return db.queryForObject("select torneo_modificar(?,?,?,?,?,?)",Boolean.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen(),t.getCodtor());
		}catch(Exception e){
			return false;
		}
	}
	public boolean eliminar(long codtor){
		try{
			return db.queryForObject("select torneo_eliminar(?)",Boolean.class,codtor);
		}catch(Exception e){
			return false;
		}
	}
	public boolean darestado(long codtor,short esttor){
		try{
			return db.queryForObject("select torneo_darestado(?,?)",Boolean.class,esttor,codtor);
		}catch(Exception e){
			return false;
		}
	}
	public int ultimaf(){
		try{
			return db.queryForObject("select max(codtor) from torneo where codmod=1",Integer.class);
		}catch(Exception e){
			return -1;
		}
	}
	public boolean darestadof(long codfas,short estfas){
		try{
			return db.queryForObject("select fase_darestado(?,?)",Boolean.class,estfas,codfas);
		}catch(Exception e){
			return false;
		}
	}
	public boolean darestadog(long codgru,short estgru){
		try{
			return db.queryForObject("select grupo_darestado(?,?)",Boolean.class,estgru,codgru);
		}catch(Exception e){
			return false;
		}
	}
	public boolean set(long tor,long fas,long gru, long par,int can){
		try {
			return db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,tor,fas,gru,par,can+1);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	public boolean setmod(long tor,long fas,long gru, long par,int pts,boolean aux,int set){
		try {
			return db.queryForObject("select set_modificar(?,?,?,?,?,?,?)", Boolean.class,tor,fas,gru,par,set,pts,aux);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	public boolean adicionar(Torneo t,int[]codjug,short[] pos, int b){
		try{
			Fase f=new Fase();
			Grupo g=new Grupo();
			Partido p=new Partido();
			int tam=codjug.length;int diferencia,ultimo=0;
			float cantg=(float)tam/b;
			int c=(int)tam/b;int aux=0;
			boolean torjug=false,fasjug=false,grujug=false,set=false;
			if(c==cantg){
				ultimo=6;
			}else{ 
				diferencia=tam-(c*b);
				if (diferencia>3) {
					ultimo=diferencia;
					c++;
				}else{
					ultimo=diferencia+b;
				}
			}
			t.setCodtor(db.queryForObject("select torneo_adicionar(?,?,?,?,?)", Long.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen()));
			if(t.getCodtor()>0){
				f.setCodfas(db.queryForObject("select fase_adicionar(?,?,?)", Long.class,t.getCodtor(),1,1));
				for (int i = 0; i < pos.length; i++) {
					torjug=(db.queryForObject("select torjug_adicionar(?,?,?)", Boolean.class,t.getCodtor(),codjug[i],pos[i]));
				}
				if(f.getCodfas()>0 && torjug){
					for (int i = 0; i < pos.length; i++) {
						fasjug=(db.queryForObject("select fasjug_adicionar(?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),codjug[i],pos[i]));
					}
					for(int i=0;i<c;i++){
						g.setCodgru(db.queryForObject("select grupo_adicionar(?,?,?)", Long.class,t.getCodtor(),f.getCodfas(),i+1));
						System.out.println("paso grupo");
						if(i!=(c-1) && g.getCodgru()>0 && fasjug){
							for (int j = 0; j < b; j++) {
								grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
								aux++;
							}
							if (grujug) {
								//ronda 1
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-1],1));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],1));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],1));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								//ronda 2
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-2],2));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],2));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],2));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								//ronda 3
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-3],3));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],3));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-5],3));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								//ronda 4
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-4],4));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-5],4));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],4));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								//ronda 5
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-5],5));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],5));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
								p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],5));
								if (p.getCodpar()>0) {
									for (int j = 0; j < 3; j++) {
										set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
									}
								}
							}
						}else{
							if (fasjug && g.getCodgru()>0) {
								switch (ultimo) {
								case 4:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-2],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								case 5:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-1],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-3],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 4
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-3],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 5
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								case 6:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-1],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-5],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 4
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-4],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-5],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 5
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-5],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								case 7:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-1],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-1],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-3],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-2],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-5],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 4
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-3],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-5],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 5
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-4],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-5],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-6],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 6
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-5],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-6],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 7
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-6],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								case 8:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-1],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-3],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-2],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-4],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-5],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-5],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-6],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 4
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-4],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-5],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-6],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-7],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 5
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-5],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-6],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-7],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 6
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-6],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-7],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 7
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-7],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-1],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								case 9:
									for (int j = 0; j < ultimo; j++) {
										grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
										aux++;
									}
									if (grujug) {
										//ronda 1
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-1],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-2],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-3],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],1));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 2
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-1],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-3],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-4],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-5],2));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 3
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-2],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-5],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-7],codjug[aux-6],3));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 4
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-3],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-5],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-8],codjug[aux-7],4));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 5
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-4],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-5],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-6],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-7],5));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 6
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-5],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-6],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-7],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-8],6));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 7
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-6],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-7],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-8],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],7));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 8
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-7],8));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-8],8));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],8));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],8));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										//ronda 9
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-9],codjug[aux-8],9));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-6],codjug[aux-1],9));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],9));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
										p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],9));
										if (p.getCodpar()>0) {
											for (int j = 0; j < 3; j++) {
												set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
											}
										}
									}
									break;
								}
							}
							
						}
					}
				}
			}
			return set;
		}catch(Exception e){
			return false;
		}
	}
	public boolean adicionart(Torneo t,int[]codjug,short[] pos, int b){
		try{
			Fase f=new Fase();
			Grupo g=new Grupo();
			Partido p=new Partido();
			int tam=codjug.length;int diferencia=0,ultimo=0;
			float cantg=(float)tam/b;
			int c=(int)tam/b;int aux=0;
			boolean torjug=false,fasjug=false,grujug=false,set=false;
			if(pos.length>5){
				if(c==cantg){
					ultimo=b;diferencia=1;
				}else{ 
					diferencia=tam-(c*b);
					if (diferencia==2 || diferencia==1) {
						ultimo=b+diferencia;
						//c++;
					}/*else{
						ultimo=diferencia+b;
					}*/
				}boolean ban=false;
				t.setCodtor(db.queryForObject("select torneo_adicionar(?,?,?,?,?)", Long.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen()));
				if(t.getCodtor()>0){
					f.setCodfas(db.queryForObject("select fase_adicionar(?,?,?)", Long.class,t.getCodtor(),1,1));
					for (int i = 0; i < pos.length; i++) {
						torjug=(db.queryForObject("select torjug_adicionar(?,?,?)", Boolean.class,t.getCodtor(),codjug[i],pos[i]));
					}
					if(f.getCodfas()>0 && torjug){
						for (int i = 0; i < pos.length; i++) {
							fasjug=(db.queryForObject("select fasjug_adicionar(?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),codjug[i],pos[i]));
						}
						for(int i=0;i<c;i++){
							g.setCodgru(db.queryForObject("select grupo_adicionar(?,?,?)", Long.class,t.getCodtor(),f.getCodfas(),i+1));
							if(i!=(c-diferencia) && g.getCodgru()>0 && fasjug && ban==false){
								for (int j = 0; j < b; j++) {
									grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
									aux++;
								}
								if (grujug) {
									//ronda 1
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 2
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 3
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
								}
							}else{
								if (fasjug && g.getCodgru()>0) {
									switch (ultimo) {
									case 3:
										for (int j = 0; j < ultimo; j++) {
											grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
											aux++;
										}
										if (grujug) {
											//ronda 1
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],1));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 2
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 3
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],3));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
										}
										break;
									case 4:
										for (int j = 0; j < ultimo; j++) {
											grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
											aux++;
										}
										if (grujug) {
											//ronda 1
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 2
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-2],2));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 3
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],3));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],3));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
										}
										break;
									case 5:
										for (int j = 0; j < 4; j++) {
											grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
											aux++;
										}
										if (grujug) {
											//ronda 1
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 2
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-2],2));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											//ronda 3
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],3));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}
											p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],3));
											if (p.getCodpar()>0) {
												for (int j = 0; j < 3; j++) {
													set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
												}
											}ban=true;
										}
										break;
									}
								}
								
							}
						}
					}
				}
				return set;
			}else{
				switch (pos.length) {
				case 3:
					t.setCodtor(db.queryForObject("select torneo_adicionar(?,?,?,?,?)", Long.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen()));
					if(t.getCodtor()>0){
						f.setCodfas(db.queryForObject("select fase_adicionar(?,?,?)", Long.class,t.getCodtor(),1,1));
						for (int i = 0; i < pos.length; i++) {
							torjug=(db.queryForObject("select torjug_adicionar(?,?,?)", Boolean.class,t.getCodtor(),codjug[i],pos[i]));
						}
						if(f.getCodfas()>0 && torjug){
							for (int i = 0; i < pos.length; i++) {
								fasjug=(db.queryForObject("select fasjug_adicionar(?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),codjug[i],pos[i]));
							}
							g.setCodgru(db.queryForObject("select grupo_adicionar(?,?,?)", Long.class,t.getCodtor(),f.getCodfas(),1));
							if(g.getCodgru()>0 && fasjug){
								for (int j = 0; j < pos.length; j++) {
									grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
									aux++;
								}
								if (grujug) {
									//ronda 1
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 2
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-1],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 3
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
								}
							}
						}
					}
					break;
				case 4:
					t.setCodtor(db.queryForObject("select torneo_adicionar(?,?,?,?,?)", Long.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen()));
					if(t.getCodtor()>0){
						f.setCodfas(db.queryForObject("select fase_adicionar(?,?,?)", Long.class,t.getCodtor(),1,1));
						for (int i = 0; i < pos.length; i++) {
							torjug=(db.queryForObject("select torjug_adicionar(?,?,?)", Boolean.class,t.getCodtor(),codjug[i],pos[i]));
						}
						if(f.getCodfas()>0 && torjug){
							for (int i = 0; i < pos.length; i++) {
								fasjug=(db.queryForObject("select fasjug_adicionar(?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),codjug[i],pos[i]));
							}
							g.setCodgru(db.queryForObject("select grupo_adicionar(?,?,?)", Long.class,t.getCodtor(),f.getCodfas(),1));
							if(g.getCodgru()>0 && fasjug){
								for (int j = 0; j < pos.length; j++) {
									grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
									aux++;
								}
								if (grujug) {
									//ronda 1
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 2
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-2],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 3
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
								}
							}
						}
					}
					break;
				case 5:
					t.setCodtor(db.queryForObject("select torneo_adicionar(?,?,?,?,?)", Long.class,t.getNomtor(),t.getFector(),t.getDestor(),t.getCodmod(),t.getCodgen()));
					if(t.getCodtor()>0){
						f.setCodfas(db.queryForObject("select fase_adicionar(?,?,?)", Long.class,t.getCodtor(),1,1));
						for (int i = 0; i < pos.length; i++) {
							torjug=(db.queryForObject("select torjug_adicionar(?,?,?)", Boolean.class,t.getCodtor(),codjug[i],pos[i]));
						}
						if(f.getCodfas()>0 && torjug){
							for (int i = 0; i < pos.length; i++) {
								fasjug=(db.queryForObject("select fasjug_adicionar(?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),codjug[i],pos[i]));
							}
							g.setCodgru(db.queryForObject("select grupo_adicionar(?,?,?)", Long.class,t.getCodtor(),f.getCodfas(),1));
							if(g.getCodgru()>0 && fasjug){
								for (int j = 0; j < pos.length; j++) {
									grujug=(db.queryForObject("select grujug_adicionar(?,?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux],j+1,0));
									aux++;
								}
								if (grujug) {
									//ronda 1
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-1],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-3],codjug[aux-2],1));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 2
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-1],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-4],codjug[aux-3],2));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 3
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-2],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-1],codjug[aux-3],3));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 4
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-3],4));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-4],4));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									//ronda 5
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-5],codjug[aux-4],5));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
									p.setCodpar(db.queryForObject("select partido_adicionar(?,?,?,?,?,?)",Long.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),codjug[aux-2],codjug[aux-1],5));
									if (p.getCodpar()>0) {
										for (int j = 0; j < 3; j++) {
											set=(db.queryForObject("select set_adicionar(?,?,?,?,?)", Boolean.class,t.getCodtor(),f.getCodfas(),g.getCodgru(),p.getCodpar(),j+1));
										}
									}
								}
							}
						}
					}
					break;
				default:
					break;
				}
				return set;
			}
		}catch(Exception e){
			return false;
		}
	}	
}
