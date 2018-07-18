package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Customer{
	
	protected int id;
	protected PolyglotDatabase db;
	
	public Customer(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setID(int id) {
		this.id=id;
		
	}
	
	public int getID() {
		return id;
	}
	
	public void addSales(Customer_sale element){
		boolean exist = false;
		for(Customer_sale member: getSales())
			if(member.getID()==element.getID())
				exist =true;
		if(!exist)
			element.setCustomer(this);
	}
	
	public ArrayList<Customer_sale> getSales(){
		return db.getSales(this);
	}
	
	public Customer_sale Findsales(int cust_id) {
		for(Customer_sale element: getSales()) {
	
			if(element.getCust_ID()==cust_id)
				return element;
		}
		return null;
	}

	public String getFisrtname() {
		return db.get(this,"Fisrtname");
	}
	
	public void setFisrtname(String Fisrtname) {
		db.set(this,"Fisrtname",Fisrtname);
	}
	
	public String getLastname() {
		return db.get(this,"Lastname");
	}
	
	public void setLastname(String Lastname) {
		db.set(this,"Lastname",Lastname);
	}
	
	public String getAddress() {
		return db.get(this,"Address");
	}
	
	public void setAddress(String Address) {
		db.set(this,"Address",Address);
	}
	
	public String getCity() {
		return db.get(this,"City");
	}
	
	public void setCity(String City) {
		db.set(this,"City",City);
	}
	
	public String getState() {
		return db.get(this,"State");
	}
	
	public void setState(String State) {
		db.set(this,"State",State);
	}
	
	public String getZip() {
		return db.get(this,"Zip");
	}
	
	public void setZip(String Zip) {
		db.set(this,"Zip",Zip);
	}
	
}	

