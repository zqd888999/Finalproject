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

import schema.*;

public class PetStoreTest {
	
	public Animal a;
	public AnimalOrder ao;
	public City c1;
	public City c2;
	public City c3;
	public Customer cu;
	public Employee e;
	public Merchandise m;
	public MerchandiseOrder mo;
	public PolyglotDatabase db;
	public Sale sale;
	public Supplier s;
	
	@Before
	public void setUp() throws Exception {
		db = new PolyglotDatabase();
		a = db.createAnimal();
		a.setAnimalID("111");
		a.setName("la");
		a.setCategory("cat");
		ao =db.createAnimalOrder();
		ao.setPoNumber(111);
		ao.setCost(1.1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate1 = dateFormat.parse("2018-08-23");
		Date myDate2 = dateFormat.parse("2018-08-24");
		ao.setOrderDate(myDate1);
		ao.setReceiveDate(myDate2);
		a.addAnimals(ao);
		a.setAnimalOrderCost(ao, 1.2);
		m = db.createMerchandise();
		m.setItemID("adasfasf");
		m.setDescription("this is hat");
		sale = db.createSale();
		sale.setSaleID("asdaf");
		sale.setSaleDate(myDate2);
		sale.addSaleItem(m);
		sale.setMerchandisePrice(m, 1.2);
		m.setSaleQuantity(sale, "good");
	}
	
	@After
	public void delete(){
		db.deleteAnimal(a);
		db.deleteAnimalOrder(ao);
		db.deleteMerchandise(m);
		db.deleteSale(sale);
	}
	
	
	@Test
	public void gettest() throws ParseException {
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate1 = dateFormat1.parse("2018-08-23");
		assertEquals(myDate1, ao.getOrderDate());
		Date myDate2 = dateFormat1.parse("2018-08-24");
		assertEquals(myDate2, ao.getReceiveDate());
		assertEquals(1.2,ao.getAnimalCost(a),1e-5);
		assertEquals(1.2,m.getSalePrice(sale),1e-5);
		assertEquals("good", sale.getMerchandiseQuantity(m));
		assertEquals(1.1,db.findAnimalOrderByPoNumber(111).getCost(),1e-5);

	}

}
