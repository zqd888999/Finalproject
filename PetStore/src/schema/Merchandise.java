package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Merchandise{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Merchandise(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addItems(Sale element){
		boolean exist = false;
		for(Sale member: getItems())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setItems(this , element);
			db.setSaleItem(element, this);
		}		
			
	}
	
	public ArrayList<Sale> getItems(){
		return db.getItems(this);
	}
	
	public void deleteItems(Sale element){
		db.deleteItems(this , element);
	}
	
	public Sale FindItems(String saleid) {
		for(Sale element: getItems()) {
			if(element.getSaleID().equals(saleid))
	
				return element;
		}
		return null;
	}	

	public String getSaleQuantity(Sale sale) {
		return db.getItems(this,sale.getDatabaseID(),"Quantity");
	}
	
	public void setSaleQuantity(Sale sale, String Quantity) {
		db.setItems(this,sale.getDatabaseID(),"Quantity",Quantity);
		db.setSaleItem(sale,id,"Quantity",Quantity);
	}
	public double getSalePrice(Sale sale) {
		return Double.valueOf(db.getItems(this,sale.getDatabaseID(),"Price"));
	}
	
	public void setSalePrice(Sale sale, double Price) {
		db.setItems(this,sale.getDatabaseID(),"Price",Price);
		db.setSaleItem(sale,id,"Price",Price);
	}
	public void addMerchandiseOrder(MerchandiseOrder element){
		boolean exist = false;
		for(MerchandiseOrder member: getMerchandiseOrder())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setMerchandiseOrder(this , element);
			db.setOrderItem(element, this);
		}		
			
	}
	
	public ArrayList<MerchandiseOrder> getMerchandiseOrder(){
		return db.getMerchandiseOrder(this);
	}
	
	public void deleteMerchandiseOrder(MerchandiseOrder element){
		db.deleteMerchandiseOrder(this , element);
	}
	
	public MerchandiseOrder FindMerchandiseOrder(String orderid) {
		for(MerchandiseOrder element: getMerchandiseOrder()) {
			if(element.getOrderID().equals(orderid))
	
				return element;
		}
		return null;
	}	

	public String getMerchandiseOrderQuantity(MerchandiseOrder merchandiseorder) {
		return db.getMerchandiseOrder(this,merchandiseorder.getDatabaseID(),"Quantity");
	}
	
	public void setMerchandiseOrderQuantity(MerchandiseOrder merchandiseorder, String Quantity) {
		db.setMerchandiseOrder(this,merchandiseorder.getDatabaseID(),"Quantity",Quantity);
		db.setOrderItem(merchandiseorder,id,"Quantity",Quantity);
	}
	public double getMerchandiseOrderPrice(MerchandiseOrder merchandiseorder) {
		return Double.valueOf(db.getMerchandiseOrder(this,merchandiseorder.getDatabaseID(),"Price"));
	}
	
	public void setMerchandiseOrderPrice(MerchandiseOrder merchandiseorder, double Price) {
		db.setMerchandiseOrder(this,merchandiseorder.getDatabaseID(),"Price",Price);
		db.setOrderItem(merchandiseorder,id,"Price",Price);
	}
	public String getItemID() {
		return db.get(this,"ItemID");
	}
	
	public void setItemID(String ItemID) {
		db.set(this,"ItemID",ItemID);
	}
	
	public String getDescription() {
		return db.get(this,"Description");
	}
	
	public void setDescription(String Description) {
		db.set(this,"Description",Description);
	}
	
}	

	
