package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Animal{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Animal(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addAnimals(AnimalOrder element){
		boolean exist = false;
		for(AnimalOrder member: getAnimals())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setAnimals(this , element);
			db.setAnimalOrderItem(element, this);
		}		
			
	}
	
	public ArrayList<AnimalOrder> getAnimals(){
		return db.getAnimals(this);
	}
	
	public void deleteAnimals(AnimalOrder element){
		db.deleteAnimals(this , element);
	}
	
	public AnimalOrder FindAnimals(int ponumber) {
		for(AnimalOrder element: getAnimals()) {
	
			if(element.getPoNumber()==ponumber)
	
				return element;
		}
		return null;
	}	

	public double getAnimalOrderCost(AnimalOrder animalorder) {
		return Double.valueOf(db.getAnimals(this,animalorder.getDatabaseID(),"cost"));
	}
	
	public void setAnimalOrderCost(AnimalOrder animalorder, double cost) {
		db.setAnimals(this,animalorder.getDatabaseID(),"cost",cost);
		db.setAnimalOrderItem(animalorder,id,"cost",cost);
	}
	public void addAnimalsale(Sale element){
		boolean exist = false;
		for(Sale member: getAnimalsale())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setAnimalsale(this , element);
			db.setSaleAnimal(element, this);
		}		
			
	}
	
	public ArrayList<Sale> getAnimalsale(){
		return db.getAnimalsale(this);
	}
	
	public void deleteAnimalsale(Sale element){
		db.deleteAnimalsale(this , element);
	}
	
	public Sale FindAnimalsale(String saleid) {
		for(Sale element: getAnimalsale()) {
			if(element.getSaleID().equals(saleid))
	
				return element;
		}
		return null;
	}	

	public double getSalePrice(Sale sale) {
		return Double.valueOf(db.getAnimalsale(this,sale.getDatabaseID(),"Price"));
	}
	
	public void setSalePrice(Sale sale, double Price) {
		db.setAnimalsale(this,sale.getDatabaseID(),"Price",Price);
		db.setSaleAnimal(sale,id,"Price",Price);
	}
	public String getAnimalID() {
		return db.get(this,"AnimalID");
	}
	
	public void setAnimalID(String AnimalID) {
		db.set(this,"AnimalID",AnimalID);
	}
	
	public String getName() {
		return db.get(this,"Name");
	}
	
	public void setName(String Name) {
		db.set(this,"Name",Name);
	}
	
	public String getCategory() {
		return db.get(this,"Category");
	}
	
	public void setCategory(String Category) {
		db.set(this,"Category",Category);
	}
	
}	

	
