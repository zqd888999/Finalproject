package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Pet_care_log{
	
	protected int id;
	protected PolyglotDatabase db;
	
	public Pet_care_log(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setID(int id) {
		this.id=id;
		
	}
	
	public int getID() {
		return id;
	}
	
	public Product getProduct(){
		return db.getproduct(this);
	}
	
	public void setProduct(Product element){
		db.set(this, "product", element.getID());
	}
	
	public String getCreated_by_user() {
		return db.get(this,"created_by_user");
	}
	
	public void setCreated_by_user(String created_by_user) {
		db.set(this,"created_by_user",created_by_user);
	}
	
	public String getLog_test() {
		return db.get(this,"Log_test");
	}
	
	public void setLog_test(String Log_test) {
		db.set(this,"Log_test",Log_test);
	}
	
	public Date getLast_update_DateTime() {
		Date date = null;    
		String str = db.get(this,"Last_update_DateTime");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setLast_update_DateTime(Date Last_update_DateTime) {
		db.set(this,"Last_update_DateTime",Last_update_DateTime);
	}
	
}	

