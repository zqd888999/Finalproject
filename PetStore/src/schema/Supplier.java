package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Supplier{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Supplier(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public City getSupplier_city(){
		return db.getSupplier_city(this);
	}
	
	public void setSupplier_city(City element){
		if(this.getSupplier_city()==null) {
			db.set(this, "Supplier_city", element.getDatabaseID());
			element.setSupplier_city(this);
		}
		else{
			if(this.getSupplier_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getSupplier_city(), "Supplier", null);
				db.set(this, "Supplier_city", element.getDatabaseID());
				element.setSupplier_city(this);
			}
		}
	}
	
	public void addAnimalorderSupplier(AnimalOrder element){
		boolean exist = false;
		for(AnimalOrder member: getAnimalorderSupplier())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setAnimalorderSupplier(this);
	}
	
	public ArrayList<AnimalOrder> getAnimalorderSupplier(){
		return db.getAnimalorderSupplier(this);
	}
	
	public void deleteAnimalorderSupplier(AnimalOrder element){
		db.set(element, "AnimalorderSupplier", null);
	}
	
	public AnimalOrder FindAnimalorderSupplier(int ponumber) {
		for(AnimalOrder element: getAnimalorderSupplier()) {
	
			if(element.getPoNumber()==ponumber)
				return element;
		}
		return null;
	}

	public void addMerchandiseorderSupplier(MerchandiseOrder element){
		boolean exist = false;
		for(MerchandiseOrder member: getMerchandiseorderSupplier())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setMerchandiseorderSupplier(this);
	}
	
	public ArrayList<MerchandiseOrder> getMerchandiseorderSupplier(){
		return db.getMerchandiseorderSupplier(this);
	}
	
	public void deleteMerchandiseorderSupplier(MerchandiseOrder element){
		db.set(element, "MerchandiseorderSupplier", null);
	}
	
	public MerchandiseOrder FindMerchandiseorderSupplier(String orderid) {
		for(MerchandiseOrder element: getMerchandiseorderSupplier()) {
			if(element.getOrderID().equals(orderid))
				return element;
		}
		return null;
	}

	public String getSupplierID() {
		return db.get(this,"SupplierID");
	}
	
	public void setSupplierID(String SupplierID) {
		db.set(this,"SupplierID",SupplierID);
	}
	
	public String getName() {
		return db.get(this,"Name");
	}
	
	public void setName(String Name) {
		db.set(this,"Name",Name);
	}
	
	public String getContactName() {
		return db.get(this,"ContactName");
	}
	
	public void setContactName(String ContactName) {
		db.set(this,"ContactName",ContactName);
	}
	
	public String getPhone() {
		return db.get(this,"Phone");
	}
	
	public void setPhone(String Phone) {
		db.set(this,"Phone",Phone);
	}
	
}	

	
