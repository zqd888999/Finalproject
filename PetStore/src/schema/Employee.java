package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Employee{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Employee(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public City getEmployee_city(){
		return db.getEmployee_city(this);
	}
	
	public void setEmployee_city(City element){
		if(this.getEmployee_city()==null) {
			db.set(this, "Employee_city", element.getDatabaseID());
			element.setEmployee_city(this);
		}
		else{
			if(this.getEmployee_city().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getEmployee_city(), "Employee", null);
				db.set(this, "Employee_city", element.getDatabaseID());
				element.setEmployee_city(this);
			}
		}
	}
	
	public void addMenchandiseOrderEmployee(MerchandiseOrder element){
		boolean exist = false;
		for(MerchandiseOrder member: getMenchandiseOrderEmployee())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setMenchandiseOrderEmployee(this);
	}
	
	public ArrayList<MerchandiseOrder> getMenchandiseOrderEmployee(){
		return db.getMenchandiseOrderEmployee(this);
	}
	
	public void deleteMenchandiseOrderEmployee(MerchandiseOrder element){
		db.set(element, "MenchandiseOrderEmployee", null);
	}
	
	public MerchandiseOrder FindMenchandiseOrderEmployee(String orderid) {
		for(MerchandiseOrder element: getMenchandiseOrderEmployee()) {
			if(element.getOrderID().equals(orderid))
				return element;
		}
		return null;
	}

	public void addAnimalOrderEmployee(AnimalOrder element){
		boolean exist = false;
		for(AnimalOrder member: getAnimalOrderEmployee())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setAnimalOrderEmployee(this);
	}
	
	public ArrayList<AnimalOrder> getAnimalOrderEmployee(){
		return db.getAnimalOrderEmployee(this);
	}
	
	public void deleteAnimalOrderEmployee(AnimalOrder element){
		db.set(element, "AnimalOrderEmployee", null);
	}
	
	public AnimalOrder FindAnimalOrderEmployee(int ponumber) {
		for(AnimalOrder element: getAnimalOrderEmployee()) {
	
			if(element.getPoNumber()==ponumber)
				return element;
		}
		return null;
	}

	public void addEmployeeSale(Sale element){
		boolean exist = false;
		for(Sale member: getEmployeeSale())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setEmployeeSale(this);
	}
	
	public ArrayList<Sale> getEmployeeSale(){
		return db.getEmployeeSale(this);
	}
	
	public void deleteEmployeeSale(Sale element){
		db.set(element, "EmployeeSale", null);
	}
	
	public Sale FindEmployeeSale(String saleid) {
		for(Sale element: getEmployeeSale()) {
			if(element.getSaleID().equals(saleid))
				return element;
		}
		return null;
	}

	public String getEmployeeID() {
		return db.get(this,"EmployeeID");
	}
	
	public void setEmployeeID(String EmployeeID) {
		db.set(this,"EmployeeID",EmployeeID);
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

	
