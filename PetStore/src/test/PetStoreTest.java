package test;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

import schema.Customer;
import schema.Customer_sale;
import schema.Pet_care_log;
import schema.PolyglotDatabase;
import schema.Product;
import schema.Sales_item;

public class PetStoreTest {
	
	protected PolyglotDatabase db;
	protected Product package1;
	protected Product product1;
	protected Product product2;
	protected Customer customer1;
	protected Customer_sale sale1;
	protected Customer_sale sale2;
	protected Pet_care_log log1;
	protected Pet_care_log log2;
	protected Sales_item item1;
	protected Sales_item item2;

	
	@Before
	public void setUp() throws Exception {
		db =  new PolyglotDatabase();
		package1 = db.createProduct();
		package1.setProduct_Name("1");
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate1 = dateFormat1.parse("2018-06-01");
		package1.setLast_update_Date(myDate1);
		Date myDate2 = dateFormat1.parse("2018-06-02");
		sale1 =db.createCustomer_sale();
		sale1.setSales_date(myDate2);
		
	}
	
	@After
	public void delete(){
		db.deleteProduct(package1);
		db.deleteCustomer_sale(sale1);
	}
	
	
	@Test
	public void gettest() throws ParseException {
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate1 = dateFormat1.parse("2018-06-01");
		assertEquals(myDate1, package1.getLast_update_Date());
		Date myDate2 = dateFormat1.parse("2018-06-02");
		assertEquals(myDate2, sale1.getSales_date());
		
	}

}
