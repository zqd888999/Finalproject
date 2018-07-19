package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Customer_sale{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Customer_sale(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Customer getCustomer(){
		return db.getcustomer(this);
	}
	
	public void setCustomer(Customer element){
		db.set(this, "customer", element.getDatabaseID());
	}
	
	public void addItems(Sales_item element){
		boolean exist = false;
		for(Sales_item member: getItems())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setCustomer_sales(this);
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

	public int getCust_ID() {
		return Integer.parseInt(db.get(this,"Cust_ID"));
	}
	
	public void setCust_ID(int Cust_ID) {
		db.set(this,"Cust_ID",Cust_ID);
	}
	
	public int getTotal_item_amount() {
		return Integer.parseInt(db.get(this,"Total_item_amount"));
	}
	
	public void setTotal_item_amount(int Total_item_amount) {
		db.set(this,"Total_item_amount",Total_item_amount);
	}
	
	public int getTax_amount() {
		return Integer.parseInt(db.get(this,"Tax_amount"));
	}
	
	public void setTax_amount(int Tax_amount) {
		db.set(this,"Tax_amount",Tax_amount);
	}
	
	public Date getSales_date() {
		Date date = null;    
		String str = db.get(this,"sales_date");
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");      
		try {    
           date = format1.parse(str);   
		} catch (ParseException e) {    
           e.printStackTrace();    
		}   
		return date;
	}
	
	public void setSales_date(Date sales_date) {
		db.set(this,"sales_date",sales_date);
	}
	
	public double getShipping_Handling_fee() {
		return Double.valueOf(db.get(this,"Shipping_Handling_fee"));
	}
	
	public void setShipping_Handling_fee(double Shipping_Handling_fee) {
		db.set(this,"Shipping_Handling_fee",Shipping_Handling_fee);
	}
	
}	

