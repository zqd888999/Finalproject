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
	
		mongo_newtest.getCollection("Person");
		
		mongo_newtest.getCollection("sons_Person");
		
		mongo_newtest.getCollection("daughter_Person");
		
	}
	
	public Person createPerson() {
		MongoCollection<Document> persons = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = persons.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("id");
		id++;
		Person person = new Person(this);
		person.setID(id);
		Document doc = new Document();
	    doc.put("id", id);
	    persons.insertOne(doc);
		return person;
	}
	
	public void set(Person person, String name, Object value) {
		MongoCollection<Document> persons = mongo_newtest.getCollection("Person");
		persons.updateOne(Filters.eq("id", person.getID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Person person, String name) {
		MongoCollection<Document> persons = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = persons.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==person.getID())
		    	return document.get(name).toString();
		}
		return null;
	}
	
	public ArrayList<Person> getSons(Person element) {
		ArrayList<Person> persons = new ArrayList<Person>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("father")!=null) {
		    	if(document.getInteger("father")==element.getID()) {
		    	Person person = new Person(this);
		    	person.setID(document.getInteger("id"));
		    	persons.add(person);
		    	}
		    }
		}
		return persons;
	}
		
	public ArrayList<Person> getDaughter(Person element) {
		ArrayList<Person> persons = new ArrayList<Person>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("dad")!=null) {
		    	if(document.getInteger("dad")==element.getID()) {
		    	Person person = new Person(this);
		    	person.setID(document.getInteger("id"));
		    	persons.add(person);
		    	}
		    }
		}
		return persons;
	}
		
	public Person getdad(Person element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==element.getID()) {
		    	if(document.getInteger("dad")!=null) {
		    	Person  person = new Person(this);
		    	person.setID(document.getInteger("dad"));
		    	return person;
		    	}
		    }
		}
		return null;
	}
	
	public Person getfather(Person element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Person");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("id")==element.getID()) {
		    	if(document.getInteger("father")!=null) {
		    	Person  person = new Person(this);
		    	person.setID(document.getInteger("father"));
		    	return person;
		    	}
		    }
		}
		return null;
	}
	
	public void deletePerson(Person person) {
		for(Person element : person.getSons()){
			set(element, "Person", null);
		}
		for(Person element : person.getDaughter()){
			set(element, "Person", null);
		}
		MongoCollection<Document> table = mongo_newtest.getCollection("Person");
		table.deleteOne(Filters.eq("id", person.getID()));
	}
	
	public void deleteSons(Person person, Person person) {
		MongoCollection<Document> table = mongo_newtest.getCollection("sons");
		Document query = new Document("$and", Arrays.asList(new Document("Person", person.getID()), new Document("Person", person.getID())));
	}
	
	public void deleteDaughter(Person person, Person person) {
		MongoCollection<Document> table = mongo_newtest.getCollection("daughter");
		Document query = new Document("$and", Arrays.asList(new Document("Person", person.getID()), new Document("Person", person.getID())));
	}
	
}

				
