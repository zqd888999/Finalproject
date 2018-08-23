package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AnimalOrder{
	
	private int id;
	protected PolyglotDatabase db;
	
	public AnimalOrder(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Supplier getAnimalorderSupplier(){
		return db.getAnimalorderSupplier(this);
	}
	
	public void setAnimalorderSupplier(Supplier element){
		db.set(this, "AnimalorderSupplier", element.getDatabaseID());
	}
	
	public Employee getAnimalOrderEmployee(){
		return db.getAnimalOrderEmployee(this);
	}
	
	public void setAnimalOrderEmployee(Employee element){
		db.set(this, "AnimalOrderEmployee", element.getDatabaseID());
	}
	
	public void addAnimalOrderItem(Animal element){
		boolean exist = false;
		for(Animal member: getAnimalOrderItem())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setAnimalOrderItem(this , element);
			db.setAnimals(element, this);
		}		
			
	}
	
	public ArrayList<Animal> getAnimalOrderItem(){
		return db.getAnimalOrderItem(this);
	}
	
	public void deleteAnimalOrderItem(Animal element){
		db.deleteAnimalOrderItem(this , element);
	}
	
	public Animal FindAnimalOrderItem(String animalid) {
		for(Animal element: getAnimalOrderItem()) {
			if(element.getAnimalID().equals(animalid))
	
				return element;
		}
		return null;
	}	

	public double getAnimalCost(Animal animal) {
		return Double.valueOf(db.getAnimalOrderItem(this,animal.getDatabaseID(),"cost"));
	}
	
	public void setAnimalCost(Animal animal, double cost) {
		db.setAnimalOrderItem(this,animal.getDatabaseID(),"cost",cost);
		db.setAnimals(animal,id,"cost",cost);
	}
	public int getPoNumber() {
		return Integer.parseInt(db.get(this,"PoNumber"));
	}
	
	public void setPoNumber(int PoNumber) {
		db.set(this,"PoNumber",PoNumber);
	}
	
	public Date getOrderDate() {
		Date date = null;    
		String str = db.get(this,"OrderDate");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setOrderDate(Date OrderDate) {
		db.set(this,"OrderDate",OrderDate);
	}
	
	public Date getReceiveDate() {
		Date date = null;    
		String str = db.get(this,"ReceiveDate");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setReceiveDate(Date ReceiveDate) {
		db.set(this,"ReceiveDate",ReceiveDate);
	}
	
	public double getCost() {
		return Double.valueOf(db.get(this,"Cost"));
	}
	
	public void setCost(double Cost) {
		db.set(this,"Cost",Cost);
	}
	
}	

	
