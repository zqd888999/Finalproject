package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MerchandiseOrder{
	
	private int id;
	protected PolyglotDatabase db;
	
	public MerchandiseOrder(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Supplier getMerchandiseorderSupplier(){
		return db.getMerchandiseorderSupplier(this);
	}
	
	public void setMerchandiseorderSupplier(Supplier element){
		db.set(this, "MerchandiseorderSupplier", element.getDatabaseID());
	}
	
	public Employee getMenchandiseOrderEmployee(){
		return db.getMenchandiseOrderEmployee(this);
	}
	
	public void setMenchandiseOrderEmployee(Employee element){
		db.set(this, "MenchandiseOrderEmployee", element.getDatabaseID());
	}
	
	public void addOrderItem(Merchandise element){
		boolean exist = false;
		for(Merchandise member: getOrderItem())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setOrderItem(this , element);
			db.setMerchandiseOrder(element, this);
		}		
			
	}
	
	public ArrayList<Merchandise> getOrderItem(){
		return db.getOrderItem(this);
	}
	
	public void deleteOrderItem(Merchandise element){
		db.deleteOrderItem(this , element);
	}
	
	public Merchandise FindOrderItem(String itemid) {
		for(Merchandise element: getOrderItem()) {
			if(element.getItemID().equals(itemid))
	
				return element;
		}
		return null;
	}	

	public String getMerchandiseQuantity(Merchandise merchandise) {
		return db.getOrderItem(this,merchandise.getDatabaseID(),"Quantity");
	}
	
	public void setMerchandiseQuantity(Merchandise merchandise, String Quantity) {
		db.setOrderItem(this,merchandise.getDatabaseID(),"Quantity",Quantity);
		db.setMerchandiseOrder(merchandise,id,"Quantity",Quantity);
	}
	public double getMerchandisePrice(Merchandise merchandise) {
		return Double.valueOf(db.getOrderItem(this,merchandise.getDatabaseID(),"Price"));
	}
	
	public void setMerchandisePrice(Merchandise merchandise, double Price) {
		db.setOrderItem(this,merchandise.getDatabaseID(),"Price",Price);
		db.setMerchandiseOrder(merchandise,id,"Price",Price);
	}
	public String getOrderID() {
		return db.get(this,"OrderID");
	}
	
	public void setOrderID(String OrderID) {
		db.set(this,"OrderID",OrderID);
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

	
