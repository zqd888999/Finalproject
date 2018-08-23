package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class City{
	
	private int id;
	protected PolyglotDatabase db;
	
	public City(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Supplier getSupplier_city(){
		return db.getSupplier_city(this);
	}
	
	public void setSupplier_city(Supplier element){
		if(this.getSupplier_city()==null) {
			db.set(this, "Supplier_city", element.getDatabaseID());
			element.setSupplier_city(this);
		}
		else{
			if(this.getSupplier_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getSupplier_city(), "City", null);
				db.set(this, "Supplier_city", element.getDatabaseID());
				element.setSupplier_city(this);
			}
		}
	}
	
	public Employee getEmployee_city(){
		return db.getEmployee_city(this);
	}
	
	public void setEmployee_city(Employee element){
		if(this.getEmployee_city()==null) {
			db.set(this, "Employee_city", element.getDatabaseID());
			element.setEmployee_city(this);
		}
		else{
			if(this.getEmployee_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getEmployee_city(), "City", null);
				db.set(this, "Employee_city", element.getDatabaseID());
				element.setEmployee_city(this);
			}
		}
	}
	
	public Customer getCustomer_city(){
		return db.getCustomer_city(this);
	}
	
	public void setCustomer_city(Customer element){
		if(this.getCustomer_city()==null) {
			db.set(this, "Customer_city", element.getDatabaseID());
			element.setCustomer_city(this);
		}
		else{
			if(this.getCustomer_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getCustomer_city(), "City", null);
				db.set(this, "Customer_city", element.getDatabaseID());
				element.setCustomer_city(this);
			}
		}
	}
	
	public String getCityID() {
		return db.get(this,"CityID");
	}
	
	public void setCityID(String CityID) {
		db.set(this,"CityID",CityID);
	}
	
	public String getZipCode() {
		return db.get(this,"ZipCode");
	}
	
	public void setZipCode(String ZipCode) {
		db.set(this,"ZipCode",ZipCode);
	}
	
	public String getCityName() {
		return db.get(this,"CityName");
	}
	
	public void setCityName(String CityName) {
		db.set(this,"CityName",CityName);
	}
	
}	

	
