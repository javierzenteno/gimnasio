package Utiles;

import java.sql.CallableStatement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class Db_Coneccion {
	
	protected JdbcTemplate db;
	protected Integer listlength = 10;
	@Resource
	protected DataSource ds;
	protected CallableStatement cstmt;
	@Autowired
	
	public void setJdbcTemplate(DataSource dataSource) {
	
		this.db = new JdbcTemplate(dataSource);
		
	}
 	public String as_jugador=" as (codjug integer,estjug bool,codpro INTEGER,codper INTEGER,fotjug varchar,madera varchar,gomad varchar,gomar varchar,nompro character varying(50),jugador text,edad integer);";
	public String as_informe=" as (codinf integer,titinf character varying(100),clainf character varying(20),feccreinf text,fecentinf text,tipinf character varying(20),obsinf character varying(500),desinf character varying(500),estinf boolean,codusu integer,ubiinf character varying(20));";
	public String as_categoria=" as (codcat integer,nomcat character varying(50),estcat boolean);";
	public String as_caja=" as (codcaja integer,monto real);";
	public String as_grupo=" as (codgru bigint,codfas bigint,numgru integer,estgru smallint,codtor bigint);";
	public String as_partido=" as (codpar bigint, codgru bigint,codjuga integer,codjugb integer,wopar boolean,byepar boolean,estpar smallint,ganpar integer,ronpar integer,codfas bigint,codtor bigint,fotjuga varchar,jugador_a text,fotjugb varchar,jugador_b text);";
	public String as_set=" as (codset integer,codpar bigint,ganset integer,ptsa smallint,ptsb smallint,falseta smallint,falsetb smallint,estset smallint,tieseta boolean,tiesetb boolean,codgru bigint,codfase bigint,codtor bigint);";
	public String as_fase=" as (codfas bigint,codtor bigint,nivel integer,estfas smallint);";
	public String as_torneo=" as (codtor bigint,nomtor varchar,fector TIMESTAMP,destor varchar,esttor smallint,codmod integer,codgen integer,fecha text);";
	public String as_fcarnet=" as (codper integer,coddoc bigint,fcaduco text,estcar boolean,udueño text, codgen integer,ucrea text,umodifica text,feccredoc text,fecmoddoc text,ubidoc varchar,pdfdoc varchar,estdoc boolean);";
	public String as_convocatoria=" as (codcon integer,coddoc bigint, fecpubcon text,tipcon boolean,titcon varchar,estcon boolean, codgen integer,ucrea text,umodifica text,feccredoc text,fecmoddoc text,ubidoc varchar,pdfdoc varchar,estdoc boolean);";
	public String as_nota=" as (codnot integer,coddoc bigint, titnot varchar,emitente varchar,remitente varchar,tipnot boolean,estnot boolean,obsnot varchar, codgen integer,ucrea text,umodifica text,feccredoc text,fecmoddoc text,ubidoc varchar,pdfdoc varchar,estdoc boolean);";
	public String as_general=" as (codgen integer,nomgen varchar,telgen varchar,dirgen varchar,loggen varchar,acrgen varchar,estgen boolean);";
	public String as_ingreso=" as (coding bigint,codper integer,desing varchar,monto real,codcaja integer,esting boolean,fecing timestamp,recibio varchar,dequien varchar,fecha text,usuario text);";
	public String as_egreso=" as (codegr bigint,codper integer,desegr varchar,monto real,codcaja integer,estegr boolean,fecegr timestamp,pago varchar,aquien varchar,fecha text,usuario text);";
	public String as_provincia=" as (codpro integer,nompro character varying(50),estpro boolean,acropro varchar,logpro varchar,codper integer);";
	public String as_entrenador=" as (codent integer,codper integer,ranent smallint,estent boolean,fotent varchar,entrenador text);";
	public String as_arbitro=" as (codarb integer,codper integer,ranarb smallint,estarb boolean,fotarb varchar,arbitro text);";
	public String as_usuario=" as (codper integer,estusu boolean,login character varying(20),password character varying(20));";
	public String as_menu=" as (codmen integer,nommen character varying(50),estmen boolean,icomen varchar);";
	public String as_nacional=" as (codnac integer,fecha date,nomnac character varying(150),estnac boolean,codgen integer);";
	public String as_persona=" as (codper integer,ciper character varying(9),nomper character varying(50),priapper character varying(50),segapper character varying(50),dirper character varying(200),telper character varying(50),estper boolean,fecnacper text,genper boolean);"; 
	public String as_proceso=" as (codpro integer,nompro character varying(50),estpro boolean,urlpro character varying(50));";
	public String as_rol=" as (codrol integer,nomrol character varying(50),estrol boolean);";
	public String as_detallenacional=" as (codnac integer,codjug integer,codcat integer,posicion smallint);";
	public String as_liga=" as (codlig integer,fecini date,fecfin date,nomlig varchar,estlig boolean);";
	public String as_detalleliga=" as (coddetlig integer,codlig integer,codjug integer,categoria character varying(1),total real);";
	public String as_fecha=" as (codfec integer,fecha date,estfec boolean,codlig integer,insfec real,numero smallint);";
	public String as_detallefecha=" as (coddetfec integer,codfec bigint,codjug integer,posicion integer);";
	public String as_object_add(String tabla,String add){
		if(add.equals(""))
			return tabla;
		else
			return tabla.substring(0,tabla.length()-2)+","+add+");";
	}
}
