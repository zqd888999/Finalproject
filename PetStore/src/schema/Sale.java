package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Sale{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Sale(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addSaleAnimal(Animal element){
		boolean exist = false;
		for(Animal member: getSaleAnimal())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setSaleAnimal(this , element);
			db.setAnimalsale(element, this);
		}		
			
	}
	
	public ArrayList<Animal> getSaleAnimal(){
		return db.getSaleAnimal(this);
	}
	
	public void deleteSaleAnimal(Animal element){
		db.deleteSaleAnimal(this , element);
	}
	
	public Animal FindSaleAnimal(String animalid) {
		for(Animal element: getSaleAnimal()) {
			if(element.getAnimalID().equals(animalid))
	
				return element;
		}
		return null;
	}	

	public double getAnimalPrice(Animal animal) {
		return Double.valueOf(db.getSaleAnimal(this,animal.getDatabaseID(),"Price"));
	}
	
	public void setAnimalPrice(Animal animal, double Price) {
		db.setSaleAnimal(this,animal.getDatabaseID(),"Price",Price);
		db.setAnimalsale(animal,id,"Price",Price);
	}
	public Customer getCustomerSale(){
		return db.getCustomerSale(this);
	}
	
	public void setCustomerSale(Customer element){
		db.set(this, "CustomerSale", element.getDatabaseID());
	}
	
	public Employee getEmployeeSale(){
		return db.getEmployeeSale(this);
	}
	
	public void setEmployeeSale(Employee element){
		db.set(this, "EmployeeSale", element.getDatabaseID());
	}
	
	public void addSaleItem(Merchandise element){
		boolean exist = false;
		for(Merchandise member: getSaleItem())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setSaleItem(this , element);
			db.setItems(element, this);
		}		
			
	}
	
	public ArrayList<Merchandise> getSaleItem(){
		return db.getSaleItem(this);
	}
	
	public void deleteSaleItem(Merchandise element){
		db.deleteSaleItem(this , element);
	}
	
	public Merchandise FindSaleItem(String itemid) {
		for(Merchandise element: getSaleItem()) {
			if(element.getItemID().equals(itemid))
	
				return element;
		}
		return null;
	}	

	public String getMerchandiseQuantity(Merchandise merchandise) {
		return db.getSaleItem(this,merchandise.getDatabaseID(),"Quantity");
	}
	
	public void setMerchandiseQuantity(Merchandise merchandise, String Quantity) {
		db.setSaleItem(this,merchandise.getDatabaseID(),"Quantity",Quantity);
		db.setItems(merchandise,id,"Quantity",Quantity);
	}
	public double getMerchandisePrice(Merchandise merchandise) {
		return Double.valueOf(db.getSaleItem(this,merchandise.getDatabaseID(),"Price"));
	}
	
	public void setMerchandisePrice(Merchandise merchandise, double Price) {
		db.setSaleItem(this,merchandise.getDatabaseID(),"Price",Price);
		db.setItems(merchandise,id,"Price",Price);
	}
	public String getSaleID() {
		return db.get(this,"SaleID");
	}
	
	public void setSaleID(String SaleID) {
		db.set(this,"SaleID",SaleID);
	}
	
	public Date getSaleDate() {
		Date date = null;    
		String str = db.get(this,"SaleDate");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setSaleDate(Date SaleDate) {
		db.set(this,"SaleDate",SaleDate);
	}
	
}	

	
