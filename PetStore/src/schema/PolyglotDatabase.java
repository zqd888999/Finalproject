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
import org.bson.conversions.Bson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
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
	
		if(!mysql_test.TableIsExit("Supplier")) 
			mysql_test.runSQL("CREATE TABLE Supplier(databaseid int,SupplierID varchar(255),Name varchar(255),ContactName varchar(255),Phone varchar(255),Supplier_city int)");
		
		if(!mysql_test.TableIsExit("City")) 
			mysql_test.runSQL("CREATE TABLE City(databaseid int,CityID varchar(255),ZipCode varchar(255),CityName varchar(255),Supplier_city int,Employee_city int,Customer_city int)");
		
		mongo_newtest.getCollection("Employee");
		
		mongo_newtest.getCollection("Animal");
		
		mongo_newtest.getCollection("Animals_Animal");
		
		mongo_newtest.getCollection("Animalsale_Animal");
		
		if(!mysql_test.TableIsExit("Customer")) 
			mysql_test.runSQL("CREATE TABLE Customer(databaseid int,CustomerID varchar(255),Name varchar(255),Phone varchar(255),Customer_city int)");
		
		mongo_newtest.getCollection("Sale");
		
		mongo_newtest.getCollection("SaleAnimal_Sale");
		
		mongo_newtest.getCollection("SaleItem_Sale");
		
		if(!mysql_test.TableIsExit("Merchandise")) 
			mysql_test.runSQL("CREATE TABLE Merchandise(databaseid int,ItemID varchar(255),Description varchar(255))");
		
		if(!mysql_test.TableIsExit("Items_Merchandise")) 
			mysql_test.runSQL("CREATE TABLE Items_Merchandise(SaleItem int,Items int,Quantity varchar(255),Price double)");
			
		if(!mysql_test.TableIsExit("MerchandiseOrder_Merchandise")) 
			mysql_test.runSQL("CREATE TABLE MerchandiseOrder_Merchandise(OrderItem int,MerchandiseOrder int,Quantity varchar(255),Price double)");
			
		mongo_newtest.getCollection("MerchandiseOrder");
		
		mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		
		if(!mysql_test.TableIsExit("AnimalOrder")) 
			mysql_test.runSQL("CREATE TABLE AnimalOrder(databaseid int,PoNumber int,OrderDate Date,ReceiveDate Date,Cost double,AnimalorderSupplier int,AnimalOrderEmployee int)");
		
		if(!mysql_test.TableIsExit("AnimalOrderItem_AnimalOrder")) 
			mysql_test.runSQL("CREATE TABLE AnimalOrderItem_AnimalOrder(Animals int,AnimalOrderItem int,cost double)");
			
	}
	
	public Supplier createSupplier() {
		ResultSet rs = mysql_test.findTable("Supplier");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			Supplier supplier = new Supplier(this);
			supplier.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Supplier(databaseid) VALUES("+id+")");
			return supplier;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Supplier supplier, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Supplier SET "+name+" = null WHERE databaseid = "+supplier.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Supplier SET "+name+" = '"+value+"' WHERE databaseid = "+supplier.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Supplier SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+supplier.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Supplier SET "+name+" = "+value+" WHERE databaseid = "+supplier.getDatabaseID());
		}
	}
	
	public String get(Supplier supplier, String name) {
		ResultSet rs =mysql_test.findTable("Supplier");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==supplier.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public City createCity() {
		ResultSet rs = mysql_test.findTable("City");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			City city = new City(this);
			city.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO City(databaseid) VALUES("+id+")");
			return city;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(City city, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE City SET "+name+" = null WHERE databaseid = "+city.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE City SET "+name+" = '"+value+"' WHERE databaseid = "+city.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE City SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+city.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE City SET "+name+" = "+value+" WHERE databaseid = "+city.getDatabaseID());
		}
	}
	
	public String get(City city, String name) {
		ResultSet rs =mysql_test.findTable("City");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==city.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Employee createEmployee() {
		MongoCollection<Document> employees = mongo_newtest.getCollection("Employee");
		FindIterable<Document> documents = employees.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("databaseid");
		id++;
		Employee employee = new Employee(this);
		employee.setDatabaseID(id);
		Document doc = new Document();
	    doc.put("databaseid", id);
	    employees.insertOne(doc);
		return employee;
	}
	
	public void set(Employee employee, String name, Object value) {
		MongoCollection<Document> employees = mongo_newtest.getCollection("Employee");
		employees.updateOne(Filters.eq("databaseid", employee.getDatabaseID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Employee employee, String name) {
		MongoCollection<Document> employees = mongo_newtest.getCollection("Employee");
		FindIterable<Document> documents = employees.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==employee.getDatabaseID()){
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
	
	public Animal createAnimal() {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animal");
		FindIterable<Document> documents = animals.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("databaseid");
		id++;
		Animal animal = new Animal(this);
		animal.setDatabaseID(id);
		Document doc = new Document();
	    doc.put("databaseid", id);
	    animals.insertOne(doc);
		return animal;
	}
	
	public void set(Animal animal, String name, Object value) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animal");
		animals.updateOne(Filters.eq("databaseid", animal.getDatabaseID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Animal animal, String name) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animal");
		FindIterable<Document> documents = animals.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==animal.getDatabaseID()){
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
				id=rs.getInt("databaseid");
			id++;
			Customer customer = new Customer(this);
			customer.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Customer(databaseid) VALUES("+id+")");
			return customer;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Customer customer, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Customer SET "+name+" = null WHERE databaseid = "+customer.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Customer SET "+name+" = '"+value+"' WHERE databaseid = "+customer.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Customer SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+customer.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Customer SET "+name+" = "+value+" WHERE databaseid = "+customer.getDatabaseID());
		}
	}
	
	public String get(Customer customer, String name) {
		ResultSet rs =mysql_test.findTable("Customer");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==customer.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Sale createSale() {
		MongoCollection<Document> sales = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = sales.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("databaseid");
		id++;
		Sale sale = new Sale(this);
		sale.setDatabaseID(id);
		Document doc = new Document();
	    doc.put("databaseid", id);
	    sales.insertOne(doc);
		return sale;
	}
	
	public void set(Sale sale, String name, Object value) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("Sale");
		sales.updateOne(Filters.eq("databaseid", sale.getDatabaseID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Sale sale, String name) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = sales.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==sale.getDatabaseID()){
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
	
	public Merchandise createMerchandise() {
		ResultSet rs = mysql_test.findTable("Merchandise");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			Merchandise merchandise = new Merchandise(this);
			merchandise.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Merchandise(databaseid) VALUES("+id+")");
			return merchandise;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Merchandise merchandise, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Merchandise SET "+name+" = null WHERE databaseid = "+merchandise.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Merchandise SET "+name+" = '"+value+"' WHERE databaseid = "+merchandise.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Merchandise SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+merchandise.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Merchandise SET "+name+" = "+value+" WHERE databaseid = "+merchandise.getDatabaseID());
		}
	}
	
	public String get(Merchandise merchandise, String name) {
		ResultSet rs =mysql_test.findTable("Merchandise");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==merchandise.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public MerchandiseOrder createMerchandiseOrder() {
		MongoCollection<Document> merchandiseorders = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = merchandiseorders.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("databaseid");
		id++;
		MerchandiseOrder merchandiseorder = new MerchandiseOrder(this);
		merchandiseorder.setDatabaseID(id);
		Document doc = new Document();
	    doc.put("databaseid", id);
	    merchandiseorders.insertOne(doc);
		return merchandiseorder;
	}
	
	public void set(MerchandiseOrder merchandiseorder, String name, Object value) {
		MongoCollection<Document> merchandiseorders = mongo_newtest.getCollection("MerchandiseOrder");
		merchandiseorders.updateOne(Filters.eq("databaseid", merchandiseorder.getDatabaseID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(MerchandiseOrder merchandiseorder, String name) {
		MongoCollection<Document> merchandiseorders = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = merchandiseorders.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==merchandiseorder.getDatabaseID()){
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
	
	public AnimalOrder createAnimalOrder() {
		ResultSet rs = mysql_test.findTable("AnimalOrder");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			AnimalOrder animalorder = new AnimalOrder(this);
			animalorder.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO AnimalOrder(databaseid) VALUES("+id+")");
			return animalorder;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(AnimalOrder animalorder, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE AnimalOrder SET "+name+" = null WHERE databaseid = "+animalorder.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE AnimalOrder SET "+name+" = '"+value+"' WHERE databaseid = "+animalorder.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE AnimalOrder SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+animalorder.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE AnimalOrder SET "+name+" = "+value+" WHERE databaseid = "+animalorder.getDatabaseID());
		}
	}
	
	public String get(AnimalOrder animalorder, String name) {
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==animalorder.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setAnimals(Animal animal,AnimalOrder animalorder){
		MongoCollection<Document> table = mongo_newtest.getCollection("Animals_Animal");
		Document doc = new Document();
	    doc.put("AnimalOrderItem", animal.getDatabaseID());
	    doc.put("Animals", animalorder.getDatabaseID());
		table.insertOne(doc);
	}
	public void setAnimals(Animal animal, int id, String name, Object value) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animals_Animal");
		Bson and = Filters.and(Filters.eq("AnimalOrderItem", animal.getDatabaseID()), Filters.eq("Animals", id));
		animals.updateOne(and, new Document("$set",new Document(name,value)));	
	}
	
	public String getAnimals(Animal animal, int id, String name) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animals_Animal");
		FindIterable<Document> documents = animals.find();
		for (Document document : documents) {
		    if(document.getInteger("AnimalOrderItem")==animal.getDatabaseID() && document.getInteger("Animals")==id){
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
	public void setAnimalsale(Animal animal,Sale sale){
		MongoCollection<Document> table = mongo_newtest.getCollection("Animalsale_Animal");
		Document doc = new Document();
	    doc.put("SaleAnimal", animal.getDatabaseID());
	    doc.put("Animalsale", sale.getDatabaseID());
		table.insertOne(doc);
	}
	public void setAnimalsale(Animal animal, int id, String name, Object value) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animalsale_Animal");
		Bson and = Filters.and(Filters.eq("SaleAnimal", animal.getDatabaseID()), Filters.eq("Animalsale", id));
		animals.updateOne(and, new Document("$set",new Document(name,value)));	
	}
	
	public String getAnimalsale(Animal animal, int id, String name) {
		MongoCollection<Document> animals = mongo_newtest.getCollection("Animalsale_Animal");
		FindIterable<Document> documents = animals.find();
		for (Document document : documents) {
		    if(document.getInteger("SaleAnimal")==animal.getDatabaseID() && document.getInteger("Animalsale")==id){
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
	public void setSaleAnimal(Sale sale,Animal animal){
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleAnimal_Sale");
		Document doc = new Document();
	    doc.put("Animalsale", sale.getDatabaseID());
	    doc.put("SaleAnimal", animal.getDatabaseID());
		table.insertOne(doc);
	}
	public void setSaleAnimal(Sale sale, int id, String name, Object value) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("SaleAnimal_Sale");
		Bson and = Filters.and(Filters.eq("Animalsale", sale.getDatabaseID()), Filters.eq("SaleAnimal", id));
		sales.updateOne(and, new Document("$set",new Document(name,value)));	
	}
	
	public String getSaleAnimal(Sale sale, int id, String name) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("SaleAnimal_Sale");
		FindIterable<Document> documents = sales.find();
		for (Document document : documents) {
		    if(document.getInteger("Animalsale")==sale.getDatabaseID() && document.getInteger("SaleAnimal")==id){
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
	public void setSaleItem(Sale sale,Merchandise merchandise){
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleItem_Sale");
		Document doc = new Document();
	    doc.put("Items", sale.getDatabaseID());
	    doc.put("SaleItem", merchandise.getDatabaseID());
		table.insertOne(doc);
	}
	public void setSaleItem(Sale sale, int id, String name, Object value) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("SaleItem_Sale");
		Bson and = Filters.and(Filters.eq("Items", sale.getDatabaseID()), Filters.eq("SaleItem", id));
		sales.updateOne(and, new Document("$set",new Document(name,value)));	
	}
	
	public String getSaleItem(Sale sale, int id, String name) {
		MongoCollection<Document> sales = mongo_newtest.getCollection("SaleItem_Sale");
		FindIterable<Document> documents = sales.find();
		for (Document document : documents) {
		    if(document.getInteger("Items")==sale.getDatabaseID() && document.getInteger("SaleItem")==id){
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
	public void setItems(Merchandise merchandise,Sale sale){
		mysql_test.runSQL("INSERT INTO Items_Merchandise(SaleItem ,Items ) VALUES("+merchandise.getDatabaseID()+","+sale.getDatabaseID()+")");
	}
		
	public void setItems(Merchandise merchandise,int id, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Items_Merchandise SET "+name+" = null WHERE SaleItem = "+merchandise.getDatabaseID()+" AND Items = " + id );
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Items_Merchandise SET "+name+" = '"+ value +"' WHERE SaleItem = "+merchandise.getDatabaseID()+" AND Items = " + id );
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Items_Merchandise SET "+name+" = '"+ sqlDate +"' WHERE SaleItem = "+merchandise.getDatabaseID()+" AND Items = " + id );
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Items_Merchandise SET "+name+" = "+ value +" WHERE SaleItem = "+merchandise.getDatabaseID()+" AND Items = " + id );

		}
	}

	public String getItems(Merchandise merchandise, int id, String name) {
		ResultSet rs =mysql_test.findTable("Items_Merchandise");
		try {
			while(rs.next())
			{
				if(rs.getInt("SaleItem")==merchandise.getDatabaseID() && rs.getInt("Items")== id)
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setMerchandiseOrder(Merchandise merchandise,MerchandiseOrder merchandiseorder){
		mysql_test.runSQL("INSERT INTO MerchandiseOrder_Merchandise(OrderItem ,MerchandiseOrder ) VALUES("+merchandise.getDatabaseID()+","+merchandiseorder.getDatabaseID()+")");
	}
		
	public void setMerchandiseOrder(Merchandise merchandise,int id, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE MerchandiseOrder_Merchandise SET "+name+" = null WHERE OrderItem = "+merchandise.getDatabaseID()+" AND MerchandiseOrder = " + id );
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE MerchandiseOrder_Merchandise SET "+name+" = '"+ value +"' WHERE OrderItem = "+merchandise.getDatabaseID()+" AND MerchandiseOrder = " + id );
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE MerchandiseOrder_Merchandise SET "+name+" = '"+ sqlDate +"' WHERE OrderItem = "+merchandise.getDatabaseID()+" AND MerchandiseOrder = " + id );
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE MerchandiseOrder_Merchandise SET "+name+" = "+ value +" WHERE OrderItem = "+merchandise.getDatabaseID()+" AND MerchandiseOrder = " + id );

		}
	}

	public String getMerchandiseOrder(Merchandise merchandise, int id, String name) {
		ResultSet rs =mysql_test.findTable("MerchandiseOrder_Merchandise");
		try {
			while(rs.next())
			{
				if(rs.getInt("OrderItem")==merchandise.getDatabaseID() && rs.getInt("MerchandiseOrder")== id)
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setOrderItem(MerchandiseOrder merchandiseorder,Merchandise merchandise){
		MongoCollection<Document> table = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		Document doc = new Document();
	    doc.put("MerchandiseOrder", merchandiseorder.getDatabaseID());
	    doc.put("OrderItem", merchandise.getDatabaseID());
		table.insertOne(doc);
	}
	public void setOrderItem(MerchandiseOrder merchandiseorder, int id, String name, Object value) {
		MongoCollection<Document> merchandiseorders = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		Bson and = Filters.and(Filters.eq("MerchandiseOrder", merchandiseorder.getDatabaseID()), Filters.eq("OrderItem", id));
		merchandiseorders.updateOne(and, new Document("$set",new Document(name,value)));	
	}
	
	public String getOrderItem(MerchandiseOrder merchandiseorder, int id, String name) {
		MongoCollection<Document> merchandiseorders = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		FindIterable<Document> documents = merchandiseorders.find();
		for (Document document : documents) {
		    if(document.getInteger("MerchandiseOrder")==merchandiseorder.getDatabaseID() && document.getInteger("OrderItem")==id){
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
	public void setAnimalOrderItem(AnimalOrder animalorder,Animal animal){
		mysql_test.runSQL("INSERT INTO AnimalOrderItem_AnimalOrder(Animals ,AnimalOrderItem ) VALUES("+animalorder.getDatabaseID()+","+animal.getDatabaseID()+")");
	}
		
	public void setAnimalOrderItem(AnimalOrder animalorder,int id, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE AnimalOrderItem_AnimalOrder SET "+name+" = null WHERE Animals = "+animalorder.getDatabaseID()+" AND AnimalOrderItem = " + id );
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE AnimalOrderItem_AnimalOrder SET "+name+" = '"+ value +"' WHERE Animals = "+animalorder.getDatabaseID()+" AND AnimalOrderItem = " + id );
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE AnimalOrderItem_AnimalOrder SET "+name+" = '"+ sqlDate +"' WHERE Animals = "+animalorder.getDatabaseID()+" AND AnimalOrderItem = " + id );
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE AnimalOrderItem_AnimalOrder SET "+name+" = "+ value +" WHERE Animals = "+animalorder.getDatabaseID()+" AND AnimalOrderItem = " + id );

		}
	}

	public String getAnimalOrderItem(AnimalOrder animalorder, int id, String name) {
		ResultSet rs =mysql_test.findTable("AnimalOrderItem_AnimalOrder");
		try {
			while(rs.next())
			{
				if(rs.getInt("Animals")==animalorder.getDatabaseID() && rs.getInt("AnimalOrderItem")== id)
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public City getSupplier_city(Supplier element) {
		ResultSet rs =mysql_test.findTable("Supplier");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					City city =new City(this);
					city.setDatabaseID(rs.getInt("Supplier_city"));
					return city;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<AnimalOrder> getAnimalorderSupplier(Supplier element) {
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		ArrayList<AnimalOrder> animalorders = new ArrayList<AnimalOrder>();
		try {
			while(rs.next())
			{
				if(rs.getInt("AnimalorderSupplier")==element.getDatabaseID()) 
				{
					AnimalOrder animalorder =new AnimalOrder(this);
					animalorder.setDatabaseID(rs.getInt("databaseid"));
					animalorders.add(animalorder);
				}
			}
			return animalorders;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<MerchandiseOrder> getMerchandiseorderSupplier(Supplier element) {
		ArrayList<MerchandiseOrder> merchandiseorders = new ArrayList<MerchandiseOrder>();
		MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("MerchandiseorderSupplier")!=null) {
		    	if(document.getInteger("MerchandiseorderSupplier")==element.getDatabaseID()) {
		    	MerchandiseOrder merchandiseorder = new MerchandiseOrder(this);
		    	merchandiseorder.setDatabaseID(document.getInteger("databaseid"));
		    	merchandiseorders.add(merchandiseorder);
		    	}
		    }
		}
		return merchandiseorders;
	}
		
	public Supplier getSupplier_city(City element) {
		ResultSet rs =mysql_test.findTable("City");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Supplier supplier =new Supplier(this);
					supplier.setDatabaseID(rs.getInt("Supplier_city"));
					return supplier;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Employee getEmployee_city(City element) {
		ResultSet rs =mysql_test.findTable("City");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Employee employee =new Employee(this);
					employee.setDatabaseID(rs.getInt("Employee_city"));
					return employee;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer getCustomer_city(City element) {
		ResultSet rs =mysql_test.findTable("City");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Customer customer =new Customer(this);
					customer.setDatabaseID(rs.getInt("Customer_city"));
					return customer;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public City getEmployee_city(Employee element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Employee");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("Employee_city")!=null) {
		    	City  city = new City(this);
		    	city.setDatabaseID(document.getInteger("Employee_city"));
		    	return city;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<MerchandiseOrder> getMenchandiseOrderEmployee(Employee element) {
		ArrayList<MerchandiseOrder> merchandiseorders = new ArrayList<MerchandiseOrder>();
		MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("MenchandiseOrderEmployee")!=null) {
		    	if(document.getInteger("MenchandiseOrderEmployee")==element.getDatabaseID()) {
		    	MerchandiseOrder merchandiseorder = new MerchandiseOrder(this);
		    	merchandiseorder.setDatabaseID(document.getInteger("databaseid"));
		    	merchandiseorders.add(merchandiseorder);
		    	}
		    }
		}
		return merchandiseorders;
	}
		
	public ArrayList<AnimalOrder> getAnimalOrderEmployee(Employee element) {
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		ArrayList<AnimalOrder> animalorders = new ArrayList<AnimalOrder>();
		try {
			while(rs.next())
			{
				if(rs.getInt("AnimalOrderEmployee")==element.getDatabaseID()) 
				{
					AnimalOrder animalorder =new AnimalOrder(this);
					animalorder.setDatabaseID(rs.getInt("databaseid"));
					animalorders.add(animalorder);
				}
			}
			return animalorders;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Sale> getEmployeeSale(Employee element) {
		ArrayList<Sale> sales = new ArrayList<Sale>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("EmployeeSale")!=null) {
		    	if(document.getInteger("EmployeeSale")==element.getDatabaseID()) {
		    	Sale sale = new Sale(this);
		    	sale.setDatabaseID(document.getInteger("databaseid"));
		    	sales.add(sale);
		    	}
		    }
		}
		return sales;
	}
		
	public ArrayList<AnimalOrder> getAnimals(Animal element) {
		ArrayList<AnimalOrder> animalorders = new ArrayList<AnimalOrder>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Animals_Animal");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) { 
		   	if(document.getInteger("AnimalOrderItem")==element.getDatabaseID()) {
		    	AnimalOrder animalorder = new AnimalOrder(this);
		    	animalorder.setDatabaseID(document.getInteger("Animals"));
		    	animalorders.add(animalorder);
		   	} 
		}
		return animalorders;
	}
	
	public ArrayList<Sale> getAnimalsale(Animal element) {
		ArrayList<Sale> sales = new ArrayList<Sale>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Animalsale_Animal");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) { 
		   	if(document.getInteger("SaleAnimal")==element.getDatabaseID()) {
		    	Sale sale = new Sale(this);
		    	sale.setDatabaseID(document.getInteger("Animalsale"));
		    	sales.add(sale);
		   	} 
		}
		return sales;
	}
	
	public City getCustomer_city(Customer element) {
		ResultSet rs =mysql_test.findTable("Customer");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					City city =new City(this);
					city.setDatabaseID(rs.getInt("Customer_city"));
					return city;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Sale> getCustomerSale(Customer element) {
		ArrayList<Sale> sales = new ArrayList<Sale>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("CustomerSale")!=null) {
		    	if(document.getInteger("CustomerSale")==element.getDatabaseID()) {
		    	Sale sale = new Sale(this);
		    	sale.setDatabaseID(document.getInteger("databaseid"));
		    	sales.add(sale);
		    	}
		    }
		}
		return sales;
	}
		
	public ArrayList<Animal> getSaleAnimal(Sale element) {
		ArrayList<Animal> animals = new ArrayList<Animal>();
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleAnimal_Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) { 
		   	if(document.getInteger("Animalsale")==element.getDatabaseID()) {
		    	Animal animal = new Animal(this);
		    	animal.setDatabaseID(document.getInteger("SaleAnimal"));
		    	animals.add(animal);
		   	} 
		}
		return animals;
	}
	
	public ArrayList<Merchandise> getSaleItem(Sale element) {
		ArrayList<Merchandise> merchandises = new ArrayList<Merchandise>();
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleItem_Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) { 
		   	if(document.getInteger("Items")==element.getDatabaseID()) {
		    	Merchandise merchandise = new Merchandise(this);
		    	merchandise.setDatabaseID(document.getInteger("SaleItem"));
		    	merchandises.add(merchandise);
		   	} 
		}
		return merchandises;
	}
	
	public Customer getCustomerSale(Sale element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("CustomerSale")!=null) {
		    	Customer  customer = new Customer(this);
		    	customer.setDatabaseID(document.getInteger("CustomerSale"));
		    	return customer;
		    	}
		    }
		}
		return null;
	}
	
	public Employee getEmployeeSale(Sale element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("EmployeeSale")!=null) {
		    	Employee  employee = new Employee(this);
		    	employee.setDatabaseID(document.getInteger("EmployeeSale"));
		    	return employee;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<Sale> getItems(Merchandise element) {
		ResultSet rs =mysql_test.findTable("Items_Merchandise");
		ArrayList<Sale> sales = new ArrayList<Sale>();
		try {
			while(rs.next())
			{
				if(rs.getInt("SaleItem")==element.getDatabaseID())
				{
					Sale sale =new Sale(this);
					sale.setDatabaseID(rs.getInt("Items"));
					sales.add(sale);
				}
			}
			return sales;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<MerchandiseOrder> getMerchandiseOrder(Merchandise element) {
		ResultSet rs =mysql_test.findTable("MerchandiseOrder_Merchandise");
		ArrayList<MerchandiseOrder> merchandiseorders = new ArrayList<MerchandiseOrder>();
		try {
			while(rs.next())
			{
				if(rs.getInt("OrderItem")==element.getDatabaseID())
				{
					MerchandiseOrder merchandiseorder =new MerchandiseOrder(this);
					merchandiseorder.setDatabaseID(rs.getInt("MerchandiseOrder"));
					merchandiseorders.add(merchandiseorder);
				}
			}
			return merchandiseorders;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Supplier getMerchandiseorderSupplier(MerchandiseOrder element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("MerchandiseorderSupplier")!=null) {
		    	Supplier  supplier = new Supplier(this);
		    	supplier.setDatabaseID(document.getInteger("MerchandiseorderSupplier"));
		    	return supplier;
		    	}
		    }
		}
		return null;
	}
	
	public Employee getMenchandiseOrderEmployee(MerchandiseOrder element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("MenchandiseOrderEmployee")!=null) {
		    	Employee  employee = new Employee(this);
		    	employee.setDatabaseID(document.getInteger("MenchandiseOrderEmployee"));
		    	return employee;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<Merchandise> getOrderItem(MerchandiseOrder element) {
		ArrayList<Merchandise> merchandises = new ArrayList<Merchandise>();
		MongoCollection<Document> table = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) { 
		   	if(document.getInteger("MerchandiseOrder")==element.getDatabaseID()) {
		    	Merchandise merchandise = new Merchandise(this);
		    	merchandise.setDatabaseID(document.getInteger("OrderItem"));
		    	merchandises.add(merchandise);
		   	} 
		}
		return merchandises;
	}
	
	public Supplier getAnimalorderSupplier(AnimalOrder element) {
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Supplier supplier =new Supplier(this);
					supplier.setDatabaseID(rs.getInt("AnimalorderSupplier"));
					return supplier;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Employee getAnimalOrderEmployee(AnimalOrder element) {
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Employee employee =new Employee(this);
					employee.setDatabaseID(rs.getInt("AnimalOrderEmployee"));
					return employee;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Animal> getAnimalOrderItem(AnimalOrder element) {
		ResultSet rs =mysql_test.findTable("AnimalOrderItem_AnimalOrder");
		ArrayList<Animal> animals = new ArrayList<Animal>();
		try {
			while(rs.next())
			{
				if(rs.getInt("Animals")==element.getDatabaseID())
				{
					Animal animal =new Animal(this);
					animal.setDatabaseID(rs.getInt("AnimalOrderItem"));
					animals.add(animal);
				}
			}
			return animals;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Supplier findSupplierBySupplierID(String supplierid){
		ResultSet rs =mysql_test.findTable("Supplier");
		try {
			while(rs.next())
			{	
				if(rs.getString("SupplierID").equals(supplierid))
	
				{
					Supplier supplier =new Supplier(this);
					supplier.setDatabaseID(rs.getInt("databaseid"));
					return supplier;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Supplier SupplierID "+ supplierid+" is not exist");
		return null;
	}
	
	public City findCityByCityID(String cityid){
		ResultSet rs =mysql_test.findTable("City");
		try {
			while(rs.next())
			{	
				if(rs.getString("CityID").equals(cityid))
	
				{
					City city =new City(this);
					city.setDatabaseID(rs.getInt("databaseid"));
					return city;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("City CityID "+ cityid+" is not exist");
		return null;
	}
	
	public Employee findEmployeeByEmployeeID(String employeeid){
		MongoCollection<Document> table = mongo_newtest.getCollection("Employee");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("EmployeeID").equals(employeeid)) {
		    	Employee employee =new Employee(this);
		    	employee.setDatabaseID(document.getInteger("databaseid"));
		    	return employee;
		    }
		}
		System.out.println("Employee EmployeeID "+ employeeid+" is not exist");
		return null;
	}
	
	public Animal findAnimalByAnimalID(String animalid){
		MongoCollection<Document> table = mongo_newtest.getCollection("Animal");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("AnimalID").equals(animalid)) {
		    	Animal animal =new Animal(this);
		    	animal.setDatabaseID(document.getInteger("databaseid"));
		    	return animal;
		    }
		}
		System.out.println("Animal AnimalID "+ animalid+" is not exist");
		return null;
	}
	
	public Customer findCustomerByCustomerID(String customerid){
		ResultSet rs =mysql_test.findTable("Customer");
		try {
			while(rs.next())
			{	
				if(rs.getString("CustomerID").equals(customerid))
	
				{
					Customer customer =new Customer(this);
					customer.setDatabaseID(rs.getInt("databaseid"));
					return customer;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Customer CustomerID "+ customerid+" is not exist");
		return null;
	}
	
	public Sale findSaleBySaleID(String saleid){
		MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("SaleID").equals(saleid)) {
		    	Sale sale =new Sale(this);
		    	sale.setDatabaseID(document.getInteger("databaseid"));
		    	return sale;
		    }
		}
		System.out.println("Sale SaleID "+ saleid+" is not exist");
		return null;
	}
	
	public Merchandise findMerchandiseByItemID(String itemid){
		ResultSet rs =mysql_test.findTable("Merchandise");
		try {
			while(rs.next())
			{	
				if(rs.getString("ItemID").equals(itemid))
	
				{
					Merchandise merchandise =new Merchandise(this);
					merchandise.setDatabaseID(rs.getInt("databaseid"));
					return merchandise;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Merchandise ItemID "+ itemid+" is not exist");
		return null;
	}
	
	public MerchandiseOrder findMerchandiseOrderByOrderID(String orderid){
		MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("OrderID").equals(orderid)) {
		    	MerchandiseOrder merchandiseorder =new MerchandiseOrder(this);
		    	merchandiseorder.setDatabaseID(document.getInteger("databaseid"));
		    	return merchandiseorder;
		    }
		}
		System.out.println("MerchandiseOrder OrderID "+ orderid+" is not exist");
		return null;
	}
	
	public AnimalOrder findAnimalOrderByPoNumber(int ponumber){
		ResultSet rs =mysql_test.findTable("AnimalOrder");
		try {
			while(rs.next())
			{	
				if(rs.getInt("PoNumber")==ponumber)
	
				{
					AnimalOrder animalorder =new AnimalOrder(this);
					animalorder.setDatabaseID(rs.getInt("databaseid"));
					return animalorder;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("AnimalOrder PoNumber "+ ponumber+" is not exist");
		return null;
	}
	
	public void deleteSupplier(Supplier member) {
		Supplier supplier = null;
		supplier = findSupplierBySupplierID(member.getSupplierID());
		if(supplier!=null){
			set(supplier.getSupplier_city(), "Supplier_city", null);
			for(AnimalOrder element : supplier.getAnimalorderSupplier()){
				set(element, "AnimalorderSupplier", null);
			}
			for(MerchandiseOrder element : supplier.getMerchandiseorderSupplier()){
				set(element, "MerchandiseorderSupplier", null);
			}
			mysql_test.runSQL("DELETE FROM Supplier WHERE databaseid ="+ supplier.getDatabaseID());
		}
	}
	
	public void deleteCity(City member) {
		City city = null;
		city = findCityByCityID(member.getCityID());
		if(city!=null){
			set(city.getSupplier_city(), "Supplier_city", null);
			set(city.getEmployee_city(), "Employee_city", null);
			set(city.getCustomer_city(), "Customer_city", null);
			mysql_test.runSQL("DELETE FROM City WHERE databaseid ="+ city.getDatabaseID());
		}
	}
	
	public void deleteEmployee(Employee member) {
		Employee employee = null;
		employee = findEmployeeByEmployeeID(member.getEmployeeID());
		if(employee!=null){
			set(employee.getEmployee_city(), "Employee_city", null);
			for(MerchandiseOrder element : employee.getMenchandiseOrderEmployee()){
				set(element, "MenchandiseOrderEmployee", null);
			}
			for(AnimalOrder element : employee.getAnimalOrderEmployee()){
				set(element, "AnimalOrderEmployee", null);
			}
			for(Sale element : employee.getEmployeeSale()){
				set(element, "EmployeeSale", null);
			}
			MongoCollection<Document> table = mongo_newtest.getCollection("Employee");
			table.deleteOne(Filters.eq("databaseid", employee.getDatabaseID()));
		}
	}
	
	public void deleteAnimal(Animal member) {
		Animal animal = null;
		animal = findAnimalByAnimalID(member.getAnimalID());
		if(animal!=null){
			MongoCollection<Document> table_Animals_Animal = mongo_newtest.getCollection("Animals_Animal");
			table_Animals_Animal.deleteMany(Filters.eq("AnimalOrderItem", animal.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM AnimalOrderItem_AnimalOrder WHERE AnimalOrderItem ="+ animal.getDatabaseID());
			MongoCollection<Document> table_Animalsale_Animal = mongo_newtest.getCollection("Animalsale_Animal");
			table_Animalsale_Animal.deleteMany(Filters.eq("SaleAnimal", animal.getDatabaseID()));
			MongoCollection<Document> table_SaleAnimal_Sale = mongo_newtest.getCollection("SaleAnimal_Sale");
			table_SaleAnimal_Sale.deleteMany(Filters.eq("SaleAnimal", animal.getDatabaseID()));
			MongoCollection<Document> table = mongo_newtest.getCollection("Animal");
			table.deleteOne(Filters.eq("databaseid", animal.getDatabaseID()));
		}
	}
	
	public void deleteCustomer(Customer member) {
		Customer customer = null;
		customer = findCustomerByCustomerID(member.getCustomerID());
		if(customer!=null){
			set(customer.getCustomer_city(), "Customer_city", null);
			for(Sale element : customer.getCustomerSale()){
				set(element, "CustomerSale", null);
			}
			mysql_test.runSQL("DELETE FROM Customer WHERE databaseid ="+ customer.getDatabaseID());
		}
	}
	
	public void deleteSale(Sale member) {
		Sale sale = null;
		sale = findSaleBySaleID(member.getSaleID());
		if(sale!=null){
			MongoCollection<Document> table_SaleAnimal_Sale = mongo_newtest.getCollection("SaleAnimal_Sale");
			table_SaleAnimal_Sale.deleteMany(Filters.eq("Animalsale", sale.getDatabaseID()));
			MongoCollection<Document> table_Animalsale_Animal = mongo_newtest.getCollection("Animalsale_Animal");
			table_Animalsale_Animal.deleteMany(Filters.eq("Animalsale", sale.getDatabaseID()));
			MongoCollection<Document> table_SaleItem_Sale = mongo_newtest.getCollection("SaleItem_Sale");
			table_SaleItem_Sale.deleteMany(Filters.eq("Items", sale.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM Items_Merchandise WHERE Items ="+ sale.getDatabaseID());
			MongoCollection<Document> table = mongo_newtest.getCollection("Sale");
			table.deleteOne(Filters.eq("databaseid", sale.getDatabaseID()));
		}
	}
	
	public void deleteMerchandise(Merchandise member) {
		Merchandise merchandise = null;
		merchandise = findMerchandiseByItemID(member.getItemID());
		if(merchandise!=null){
			mysql_test.runSQL("DELETE FROM Items_Merchandise WHERE SaleItem ="+ merchandise.getDatabaseID());
			MongoCollection<Document> table_SaleItem_Sale = mongo_newtest.getCollection("SaleItem_Sale");
			table_SaleItem_Sale.deleteMany(Filters.eq("SaleItem", merchandise.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM MerchandiseOrder_Merchandise WHERE OrderItem ="+ merchandise.getDatabaseID());
			MongoCollection<Document> table_OrderItem_MerchandiseOrder = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
			table_OrderItem_MerchandiseOrder.deleteMany(Filters.eq("OrderItem", merchandise.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM Merchandise WHERE databaseid ="+ merchandise.getDatabaseID());
		}
	}
	
	public void deleteMerchandiseOrder(MerchandiseOrder member) {
		MerchandiseOrder merchandiseorder = null;
		merchandiseorder = findMerchandiseOrderByOrderID(member.getOrderID());
		if(merchandiseorder!=null){
			MongoCollection<Document> table_OrderItem_MerchandiseOrder = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
			table_OrderItem_MerchandiseOrder.deleteMany(Filters.eq("MerchandiseOrder", merchandiseorder.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM MerchandiseOrder_Merchandise WHERE MerchandiseOrder ="+ merchandiseorder.getDatabaseID());
			MongoCollection<Document> table = mongo_newtest.getCollection("MerchandiseOrder");
			table.deleteOne(Filters.eq("databaseid", merchandiseorder.getDatabaseID()));
		}
	}
	
	public void deleteAnimalOrder(AnimalOrder member) {
		AnimalOrder animalorder = null;
		animalorder = findAnimalOrderByPoNumber(member.getPoNumber());
		if(animalorder!=null){
			mysql_test.runSQL("DELETE FROM AnimalOrderItem_AnimalOrder WHERE Animals ="+ animalorder.getDatabaseID());
			MongoCollection<Document> table_Animals_Animal = mongo_newtest.getCollection("Animals_Animal");
			table_Animals_Animal.deleteMany(Filters.eq("Animals", animalorder.getDatabaseID()));
			mysql_test.runSQL("DELETE FROM AnimalOrder WHERE databaseid ="+ animalorder.getDatabaseID());
		}
	}
	
	public void deleteAnimals(Animal animal, AnimalOrder animalorder) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Animals_Animal");
		Document query = new Document("$and", Arrays.asList(new Document("Animals", animal.getDatabaseID()), new Document("AnimalOrderItem", animalorder.getDatabaseID())));
		table.deleteOne(query);
	}
	
	public void deleteAnimalsale(Animal animal, Sale sale) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Animalsale_Animal");
		Document query = new Document("$and", Arrays.asList(new Document("Animalsale", animal.getDatabaseID()), new Document("SaleAnimal", sale.getDatabaseID())));
		table.deleteOne(query);
	}
	
	public void deleteSaleAnimal(Sale sale, Animal animal) {
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleAnimal_Sale");
		Document query = new Document("$and", Arrays.asList(new Document("SaleAnimal", sale.getDatabaseID()), new Document("Animalsale", animal.getDatabaseID())));
		table.deleteOne(query);
	}
	
	public void deleteSaleItem(Sale sale, Merchandise merchandise) {
		MongoCollection<Document> table = mongo_newtest.getCollection("SaleItem_Sale");
		Document query = new Document("$and", Arrays.asList(new Document("SaleItem", sale.getDatabaseID()), new Document("Items", merchandise.getDatabaseID())));
		table.deleteOne(query);
	}
	
	public void deleteItems(Merchandise merchandise, Sale sale) {
		mysql_test.runSQL("DELETE FROM Items_Merchandise WHERE Items ="+ merchandise.getDatabaseID() +" and SaleItem ="+ sale.getDatabaseID());			
	}
	
	public void deleteMerchandiseOrder(Merchandise merchandise, MerchandiseOrder merchandiseorder) {
		mysql_test.runSQL("DELETE FROM MerchandiseOrder_Merchandise WHERE MerchandiseOrder ="+ merchandise.getDatabaseID() +" and OrderItem ="+ merchandiseorder.getDatabaseID());			
	}
	
	public void deleteOrderItem(MerchandiseOrder merchandiseorder, Merchandise merchandise) {
		MongoCollection<Document> table = mongo_newtest.getCollection("OrderItem_MerchandiseOrder");
		Document query = new Document("$and", Arrays.asList(new Document("OrderItem", merchandiseorder.getDatabaseID()), new Document("MerchandiseOrder", merchandise.getDatabaseID())));
		table.deleteOne(query);
	}
	
	public void deleteAnimalOrderItem(AnimalOrder animalorder, Animal animal) {
		mysql_test.runSQL("DELETE FROM AnimalOrderItem_AnimalOrder WHERE AnimalOrderItem ="+ animalorder.getDatabaseID() +" and Animals ="+ animal.getDatabaseID());			
	}
	
}

				
