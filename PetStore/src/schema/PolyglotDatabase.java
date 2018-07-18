package schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class PolyglotDatabase {

	public class Mysql{
		protected  Connection conn;
		
		public Mysql(String url, String username, String password) {
			 try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			 try {
					conn = DriverManager.getConnection(url,username,password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		public boolean runSQL(String sql) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				int result;
				result = stmt.executeUpdate(sql);
				if (result != -1) 
					return true;
				else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return false;
		}
		
		public boolean TableIsExit(String table) {
			Statement stmt;
			try {
				String sql = "show tables like \""+table+"\"";
				stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(sql);
				if (resultSet.next()) 
					return true;
				else
					return false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return false;
		}
        
		public ResultSet  findTable(String name) {
			String sql = "SELECT * FROM "+name;
			Statement stmt;
			try {
				stmt = conn.createStatement();
				if(TableIsExit(name))
					return stmt.executeQuery(sql);
				else
					return null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	protected Mysql mysql_test;
	protected MongoDatabase mongo_newtest;

	public PolyglotDatabase(){
		String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT";
		mysql_test = new Mysql(url,"root","");

		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		mongo_newtest= mongoClient.getDatabase("newtest");
	
		mongo_newtest.getCollection("Product");
		
		if(!mysql_test.TableIsExit("Customer")) 
			mysql_test.runSQL("CREATE TABLE Customer(id int,Fisrtname varchar(255),Lastname varchar(255),Address varchar(255),City varchar(255),State varchar(255),Zip varchar(255))");
		
		if(!mysql_test.TableIsExit("Customer_sale")) 
			mysql_test.runSQL("CREATE TABLE Customer_sale(id int,Cust_ID int,Total_item_amount int,Tax_amount int,sales_date Date,Shipping_Handling_fee double,customer int)");
		
		if(!mysql_test.TableIsExit("Sales_item")) 
			mysql_test.runSQL("CREATE TABLE Sales_item(id int,sale_amount int,product int,customer_sales int)");
		
		mongo_newtest.getCollection("Pet_care_log");
		
	}
	
	public Product createProduct() {
		MongoCollection<Document> products = mongo_newtest.getCollection("Product");
		FindIterable<Document> documents = products.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("id");
		id++;
		Product product = new Product(this);
		product.setID(id);
		Document doc = new Document();
	    doc.put("id", id);
	    products.insertOne(doc);
		return product;
	}
	
	public void set(Product product, String name, Object value) {
		MongoCollection<Document> products = mongo_newtest.getCollection("Product");
		products.updateOne(Filters.eq("id", product.getID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Product product, String name) {
		MongoCollection<Document> products = mongo_newtest.getCollection("Product");
		FindIterable<Document> documents = products.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==product.getID()){
		    	if(document.get(name).getClass().getSimpleName().equals("Date")){
		    		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
					return format1.format(document.get(name));   
		    	}
		    	else
		    		return document.get(name).toString();
		    }
		    	
		}
		return null;
	}
	
	public Customer createCustomer() {
		ResultSet rs = mysql_test.findTable("Customer");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("id");
			id++;
			Customer customer = new Customer(this);
			customer.setID(id);
			mysql_test.runSQL("INSERT INTO Customer(id) VALUES("+id+")");
			return customer;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Customer customer, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Customer SET "+name+" = null WHERE id = "+customer.getID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Customer SET "+name+" = '"+value+"' WHERE id = "+customer.getID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Customer SET "+name+" = '"+ sqlDate +"' WHERE id = "+customer.getID());
			}
			else
				mysql_test.runSQL("UPDATE Customer SET "+name+" = "+value+" WHERE id = "+customer.getID());
			}
	}
	
	public String get(Customer customer, String name) {
		ResultSet rs =mysql_test.findTable("Customer");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==customer.getID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Customer_sale createCustomer_sale() {
		ResultSet rs = mysql_test.findTable("Customer_sale");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("id");
			id++;
			Customer_sale customer_sale = new Customer_sale(this);
			customer_sale.setID(id);
			mysql_test.runSQL("INSERT INTO Customer_sale(id) VALUES("+id+")");
			return customer_sale;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Customer_sale customer_sale, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Customer_sale SET "+name+" = null WHERE id = "+customer_sale.getID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Customer_sale SET "+name+" = '"+value+"' WHERE id = "+customer_sale.getID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Customer_sale SET "+name+" = '"+ sqlDate +"' WHERE id = "+customer_sale.getID());
			}
			else
				mysql_test.runSQL("UPDATE Customer_sale SET "+name+" = "+value+" WHERE id = "+customer_sale.getID());
			}
	}
	
	public String get(Customer_sale customer_sale, String name) {
		ResultSet rs =mysql_test.findTable("Customer_sale");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==customer_sale.getID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Sales_item createSales_item() {
		ResultSet rs = mysql_test.findTable("Sales_item");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("id");
			id++;
			Sales_item sales_item = new Sales_item(this);
			sales_item.setID(id);
			mysql_test.runSQL("INSERT INTO Sales_item(id) VALUES("+id+")");
			return sales_item;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Sales_item sales_item, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Sales_item SET "+name+" = null WHERE id = "+sales_item.getID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Sales_item SET "+name+" = '"+value+"' WHERE id = "+sales_item.getID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Sales_item SET "+name+" = '"+ sqlDate +"' WHERE id = "+sales_item.getID());
			}
			else
				mysql_test.runSQL("UPDATE Sales_item SET "+name+" = "+value+" WHERE id = "+sales_item.getID());
			}
	}
	
	public String get(Sales_item sales_item, String name) {
		ResultSet rs =mysql_test.findTable("Sales_item");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==sales_item.getID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Pet_care_log createPet_care_log() {
		MongoCollection<Document> pet_care_logs = mongo_newtest.getCollection("Pet_care_log");
		FindIterable<Document> documents = pet_care_logs.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("id");
		id++;
		Pet_care_log pet_care_log = new Pet_care_log(this);
		pet_care_log.setID(id);
		Document doc = new Document();
	    doc.put("id", id);
	    pet_care_logs.insertOne(doc);
		return pet_care_log;
	}
	
	public void set(Pet_care_log pet_care_log, String name, Object value) {
		MongoCollection<Document> pet_care_logs = mongo_newtest.getCollection("Pet_care_log");
		pet_care_logs.updateOne(Filters.eq("id", pet_care_log.getID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Pet_care_log pet_care_log, String name) {
		MongoCollection<Document> pet_care_logs = mongo_newtest.getCollection("Pet_care_log");
		FindIterable<Document> documents = pet_care_logs.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==pet_care_log.getID()){
		    	if(document.get(name).getClass().getSimpleName().equals("Date")){
		    		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
					return format1.format(document.get(name));   
		    	}
		    	else
		    		return document.get(name).toString();
		    }
		    	
		}
		return null;
	}
	
	public ArrayList<Product> getProducts(Product element) {
		ArrayList<Product> products = new ArrayList<Product>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Product");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("package")!=null) {
		    	if(document.getInteger("package")==element.getID()) {
		    	Product product = new Product(this);
		    	product.setID(document.getInteger("id"));
		    	products.add(product);
		    	}
		    }
		}
		return products;
	}
		
	public Product getpackage(Product element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Product");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==element.getID()) {
		    	if(document.getInteger("package")!=null) {
		    	Product  product = new Product(this);
		    	product.setID(document.getInteger("package"));
		    	return product;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<Sales_item> getItems(Product element) {
		ResultSet rs =mysql_test.findTable("Sales_item");
		ArrayList<Sales_item> sales_items = new ArrayList<Sales_item>();
		try {
			while(rs.next())
			{
				if(rs.getInt("product")==element.getID()) 
				{
					Sales_item sales_item =new Sales_item(this);
					sales_item.setID(rs.getInt("id"));
					sales_items.add(sales_item);
				}
			}
			return sales_items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Pet_care_log> getLogs(Product element) {
		ArrayList<Pet_care_log> pet_care_logs = new ArrayList<Pet_care_log>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Pet_care_log");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("product")!=null) {
		    	if(document.getInteger("product")==element.getID()) {
		    	Pet_care_log pet_care_log = new Pet_care_log(this);
		    	pet_care_log.setID(document.getInteger("id"));
		    	pet_care_logs.add(pet_care_log);
		    	}
		    }
		}
		return pet_care_logs;
	}
		
	public ArrayList<Customer_sale> getSales(Customer element) {
		ResultSet rs =mysql_test.findTable("Customer_sale");
		ArrayList<Customer_sale> customer_sales = new ArrayList<Customer_sale>();
		try {
			while(rs.next())
			{
				if(rs.getInt("customer")==element.getID()) 
				{
					Customer_sale customer_sale =new Customer_sale(this);
					customer_sale.setID(rs.getInt("id"));
					customer_sales.add(customer_sale);
				}
			}
			return customer_sales;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer getcustomer(Customer_sale element) {
		ResultSet rs =mysql_test.findTable("Customer_sale");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==element.getID())
				{
					Customer customer =new Customer(this);
					customer.setID(rs.getInt("customer"));
					return customer;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Sales_item> getItems(Customer_sale element) {
		ResultSet rs =mysql_test.findTable("Sales_item");
		ArrayList<Sales_item> sales_items = new ArrayList<Sales_item>();
		try {
			while(rs.next())
			{
				if(rs.getInt("customer_sales")==element.getID()) 
				{
					Sales_item sales_item =new Sales_item(this);
					sales_item.setID(rs.getInt("id"));
					sales_items.add(sales_item);
				}
			}
			return sales_items;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Product getproduct(Sales_item element) {
		ResultSet rs =mysql_test.findTable("Sales_item");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==element.getID())
				{
					Product product =new Product(this);
					product.setID(rs.getInt("product"));
					return product;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer_sale getcustomer_sales(Sales_item element) {
		ResultSet rs =mysql_test.findTable("Sales_item");
		try {
			while(rs.next())
			{
				if(rs.getInt("id")==element.getID())
				{
					Customer_sale customer_sale =new Customer_sale(this);
					customer_sale.setID(rs.getInt("customer_sales"));
					return customer_sale;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Product getproduct(Pet_care_log element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Pet_care_log");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==element.getID()) {
		    	if(document.getInteger("product")!=null) {
		    	Product  product = new Product(this);
		    	product.setID(document.getInteger("product"));
		    	return product;
		    	}
		    }
		}
		return null;
	}
	
	public Product findProductByProduct_Name(String product_name){
		MongoCollection<Document> table = mongo_newtest.getCollection("Product");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("Product_Name").equals(product_name)) {
		    	Product product =new Product(this);
		    	product.setID(document.getInteger("id"));
		    	return product;
		    }
		}
		System.out.println("Product Product_Name "+ product_name+" is not exist");
		return null;
	}
	
	public Customer findCustomerByFisrtname(String fisrtname){
		ResultSet rs =mysql_test.findTable("Customer");
		try {
			while(rs.next())
			{	
				if(rs.getString("Fisrtname").equals(fisrtname))
	
				{
					Customer customer =new Customer(this);
					customer.setID(rs.getInt("id"));
					return customer;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Customer Fisrtname "+ fisrtname+" is not exist");
		return null;
	}
	
	public Customer_sale findCustomer_saleByCust_ID(int cust_id){
		ResultSet rs =mysql_test.findTable("Customer_sale");
		try {
			while(rs.next())
			{	
				if(rs.getInt("Cust_ID")==cust_id)
	
				{
					Customer_sale customer_sale =new Customer_sale(this);
					customer_sale.setID(rs.getInt("id"));
					return customer_sale;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Customer_sale Cust_ID "+ cust_id+" is not exist");
		return null;
	}
	
	public Sales_item findSales_itemBySale_amount(int sale_amount){
		ResultSet rs =mysql_test.findTable("Sales_item");
		try {
			while(rs.next())
			{	
				if(rs.getInt("sale_amount")==sale_amount)
	
				{
					Sales_item sales_item =new Sales_item(this);
					sales_item.setID(rs.getInt("id"));
					return sales_item;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sales_item Sale_amount "+ sale_amount+" is not exist");
		return null;
	}
	
	public Pet_care_log findPet_care_logByCreated_by_user(String created_by_user){
		MongoCollection<Document> table = mongo_newtest.getCollection("Pet_care_log");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("created_by_user").equals(created_by_user)) {
		    	Pet_care_log pet_care_log =new Pet_care_log(this);
		    	pet_care_log.setID(document.getInteger("id"));
		    	return pet_care_log;
		    }
		}
		System.out.println("Pet_care_log Created_by_user "+ created_by_user+" is not exist");
		return null;
	}
	
	public void deleteProduct(Product product) {
		if(product!=null){
			for(Product element : product.getProducts()){
				set(element, "package", null);
			}
			for(Sales_item element : product.getItems()){
				set(element, "product", null);
			}
			for(Pet_care_log element : product.getLogs()){
				set(element, "product", null);
			}
			MongoCollection<Document> table = mongo_newtest.getCollection("Product");
			table.deleteOne(Filters.eq("id", product.getID()));
		}
	}
	
	public void deleteCustomer(Customer customer) {
		if(customer!=null){
			for(Customer_sale element : customer.getSales()){
				set(element, "customer", null);
			}
			mysql_test.runSQL("DELETE FROM Customer WHERE id ="+ customer.getID());
		}
	}
	
	public void deleteCustomer_sale(Customer_sale customer_sale) {
		if(customer_sale!=null){
			for(Sales_item element : customer_sale.getItems()){
				set(element, "customer_sales", null);
			}
			mysql_test.runSQL("DELETE FROM Customer_sale WHERE id ="+ customer_sale.getID());
		}
	}
	
	public void deleteSales_item(Sales_item sales_item) {
		if(sales_item!=null){
			mysql_test.runSQL("DELETE FROM Sales_item WHERE id ="+ sales_item.getID());
		}
	}
	
	public void deletePet_care_log(Pet_care_log pet_care_log) {
		if(pet_care_log!=null){
			MongoCollection<Document> table = mongo_newtest.getCollection("Pet_care_log");
			table.deleteOne(Filters.eq("id", pet_care_log.getID()));
		}
	}
	
}

				
