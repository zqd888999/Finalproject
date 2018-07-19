package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Product{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Product(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addProducts(Product element){
		boolean exist = false;
		for(Product member: getProducts())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setPackage(this);
	}
	
	public ArrayList<Product> getProducts(){
		return db.getProducts(this);
	}
	
	public Product Findproducts(String product_name) {
		for(Product element: getProducts()) {
			if(element.getProduct_Name().equals(product_name))
				return element;
		}
		return null;
	}

	public Product getPackage(){
		return db.getpackage(this);
	}
	
	public void setPackage(Product element){
		db.set(this, "package", element.getDatabaseID());
	}
	
	public void addItems(Sales_item element){
		boolean exist = false;
		for(Sales_item member: getItems())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setProduct(this);
	}
	
	public ArrayList<Sales_item> getItems(){
		return db.getItems(this);
	}
	
	public Sales_item Finditems(int sale_amount) {
		for(Sales_item element: getItems()) {
	
			if(element.getSale_amount()==sale_amount)
				return element;
		}
		return null;
	}

	public void addLogs(Pet_care_log element){
		boolean exist = false;
		for(Pet_care_log member: getLogs())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setProduct(this);
	}
	
	public ArrayList<Pet_care_log> getLogs(){
		return db.getLogs(this);
	}
	
	public Pet_care_log Findlogs(String created_by_user) {
		for(Pet_care_log element: getLogs()) {
			if(element.getCreated_by_user().equals(created_by_user))
				return element;
		}
		return null;
	}

	public String getProduct_Name() {
		return db.get(this,"Product_Name");
	}
	
	public void setProduct_Name(String Product_Name) {
		db.set(this,"Product_Name",Product_Name);
	}
	
	public int getCurrent_inventory_count() {
		return Integer.parseInt(db.get(this,"Current_inventory_count"));
	}
	
	public void setCurrent_inventory_count(int Current_inventory_count) {
		db.set(this,"Current_inventory_count",Current_inventory_count);
	}
	
	public double getStore_cost() {
		return Double.valueOf(db.get(this,"Store_cost"));
	}
	
	public void setStore_cost(double Store_cost) {
		db.set(this,"Store_cost",Store_cost);
	}
	
	public double getSale_cost() {
		return Double.valueOf(db.get(this,"Sale_cost"));
	}
	
	public void setSale_cost(double Sale_cost) {
		db.set(this,"Sale_cost",Sale_cost);
	}
	
	public Date getLast_update_Date() {
		Date date = null;    
		String str = db.get(this,"Last_update_Date");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setLast_update_Date(Date Last_update_Date) {
		db.set(this,"Last_update_Date",Last_update_Date);
	}
	
	public String getUpdated_by_User() {
		return db.get(this,"Updated_by_User");
	}
	
	public void setUpdated_by_User(String Updated_by_User) {
		db.set(this,"Updated_by_User",Updated_by_User);
	}
	
	public String getPet_flag() {
		return db.get(this,"Pet_flag");
	}
	
	public void setPet_flag(String Pet_flag) {
		db.set(this,"Pet_flag",Pet_flag);
	}
	
}	

