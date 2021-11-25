package Utiles;

public class procedure_stored {
	private String select="select * from ";

	public String Select(String procedimiento,String como){
		return this.select+procedimiento+como;
	}
}
