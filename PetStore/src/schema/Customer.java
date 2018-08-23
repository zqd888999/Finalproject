package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Customer{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Customer(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public City getCustomer_city(){
		return db.getCustomer_city(this);
	}
	
	public void setCustomer_city(City element){
		if(this.getCustomer_city()==null) {
			db.set(this, "Customer_city", element.getDatabaseID());
			element.setCustomer_city(this);
		}
		else{
			if(this.getCustomer_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getCustomer_city(), "Customer", null);
				db.set(this, "Customer_city", element.getDatabaseID());
				element.setCustomer_city(this);
			}
		}
	}
	
	public void addCustomerSale(Sale element){
		boolean exist = false;
		for(Sale member: getCustomerSale())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setCustomerSale(this);
	}
	
	public ArrayList<Sale> getCustomerSale(){
		return db.getCustomerSale(this);
	}
	
	public void deleteCustomerSale(Sale element){
		db.set(element, "CustomerSale", null);
	}
	
	public Sale FindCustomerSale(String saleid) {
		for(Sale element: getCustomerSale()) {
			if(element.getSaleID().equals(saleid))
				return element;
		}
		return null;
	}

	public String getCustomerID() {
		return db.get(this,"CustomerID");
	}
	
	public void setCustomerID(String CustomerID) {
		db.set(this,"CustomerID",CustomerID);
	}
	
	public String getName() {
		return db.get(this,"Name");
	}
	
	public void setName(String Name) {
		db.set(this,"Name",Name);
	}
	
	public String getPhone() {
		return db.get(this,"Phone");
	}
	
	public void setPhone(String Phone) {
		db.set(this,"Phone",Phone);
	}
	
}	

	
