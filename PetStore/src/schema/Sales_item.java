package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Sales_item{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Sales_item(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Product getProduct(){
		return db.getproduct(this);
	}
	
	public void setProduct(Product element){
		db.set(this, "product", element.getDatabaseID());
	}
	
	public Customer_sale getCustomer_sales(){
		return db.getcustomer_sales(this);
	}
	
	public void setCustomer_sales(Customer_sale element){
		db.set(this, "customer_sales", element.getDatabaseID());
	}
	
	public int getSale_amount() {
		return Integer.parseInt(db.get(this,"sale_amount"));
	}
	
	public void setSale_amount(int sale_amount) {
		db.set(this,"sale_amount",sale_amount);
	}
	
}	

