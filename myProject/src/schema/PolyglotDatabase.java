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
	
		if(!mysql_test.TableIsExit("Post")) 
			mysql_test.runSQL("CREATE TABLE Post(databaseid int,Title varchar(255),Page int,Price double,author int)");
		
		if(!mysql_test.TableIsExit("Author")) 
			mysql_test.runSQL("CREATE TABLE Author(databaseid int,name varchar(255),age int,post int)");
		
		if(!mysql_test.TableIsExit("fans_Author")) 
			mysql_test.runSQL("CREATE TABLE fans_Author(idol int,fans int)");
			
		mongo_newtest.getCollection("Comment");
		
		if(!mysql_test.TableIsExit("Fan")) 
			mysql_test.runSQL("CREATE TABLE Fan(databaseid int,name varchar(255),age int)");
		
		if(!mysql_test.TableIsExit("idol_Fan")) 
			mysql_test.runSQL("CREATE TABLE idol_Fan(fans int,idol int)");
			
	}
	
	public Post createPost() {
		ResultSet rs = mysql_test.findTable("Post");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			Post post = new Post(this);
			post.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Post(databaseid) VALUES("+id+")");
			return post;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Post post, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Post SET "+name+" = null WHERE databaseid = "+post.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Post SET "+name+" = '"+value+"' WHERE databaseid = "+post.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Post SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+post.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Post SET "+name+" = "+value+" WHERE databaseid = "+post.getDatabaseID());
		}
	}
	
	public String get(Post post, String name) {
		ResultSet rs =mysql_test.findTable("Post");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==post.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Author createAuthor() {
		ResultSet rs = mysql_test.findTable("Author");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			Author author = new Author(this);
			author.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Author(databaseid) VALUES("+id+")");
			return author;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Author author, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Author SET "+name+" = null WHERE databaseid = "+author.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Author SET "+name+" = '"+value+"' WHERE databaseid = "+author.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Author SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+author.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Author SET "+name+" = "+value+" WHERE databaseid = "+author.getDatabaseID());
		}
	}
	
	public String get(Author author, String name) {
		ResultSet rs =mysql_test.findTable("Author");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==author.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Comment createComment() {
		MongoCollection<Document> comments = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = comments.find();
		int id =0;
		for (Document document : documents) 
		   id = document.getInteger("databaseid");
		id++;
		Comment comment = new Comment(this);
		comment.setDatabaseID(id);
		Document doc = new Document();
	    doc.put("databaseid", id);
	    comments.insertOne(doc);
		return comment;
	}
	
	public void set(Comment comment, String name, Object value) {
		MongoCollection<Document> comments = mongo_newtest.getCollection("Comment");
		comments.updateOne(Filters.eq("databaseid", comment.getDatabaseID()), new Document("$set",new Document(name,value)));	
	}
	
	public String get(Comment comment, String name) {
		MongoCollection<Document> comments = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = comments.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==comment.getDatabaseID()){
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
	
	public Fan createFan() {
		ResultSet rs = mysql_test.findTable("Fan");
		int id =0;
		try {
			while(rs.next())
				id=rs.getInt("databaseid");
			id++;
			Fan fan = new Fan(this);
			fan.setDatabaseID(id);
			mysql_test.runSQL("INSERT INTO Fan(databaseid) VALUES("+id+")");
			return fan;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void set(Fan fan, String name, Object value) {
		if(value ==null)
			mysql_test.runSQL("UPDATE Fan SET "+name+" = null WHERE databaseid = "+fan.getDatabaseID());
		else {
			if(value.getClass().getSimpleName().equals("String"))
				mysql_test.runSQL("UPDATE Fan SET "+name+" = '"+value+"' WHERE databaseid = "+fan.getDatabaseID());
			if(value.getClass().getSimpleName().equals("Date")){
				java.sql.Date sqlDate = new java.sql.Date(((Date) value).getTime());
				mysql_test.runSQL("UPDATE Fan SET "+name+" = '"+ sqlDate +"' WHERE databaseid = "+fan.getDatabaseID());
			}
			if(value.getClass().getSimpleName().equals("Integer") || value.getClass().getSimpleName().equals("Double"))
				mysql_test.runSQL("UPDATE Fan SET "+name+" = "+value+" WHERE databaseid = "+fan.getDatabaseID());
		}
	}
	
	public String get(Fan fan, String name) {
		ResultSet rs =mysql_test.findTable("Fan");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==fan.getDatabaseID())
					return rs.getString(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setFans(Author author,Fan fan){
		mysql_test.runSQL("INSERT INTO fans_Author(idol ,fans ) VALUES("+author.getDatabaseID()+","+fan.getDatabaseID()+")");
	}
		
	public void setIdol(Fan fan,Author author){
		mysql_test.runSQL("INSERT INTO idol_Fan(fans ,idol ) VALUES("+fan.getDatabaseID()+","+author.getDatabaseID()+")");
	}
		
	public ArrayList<Comment> getComments(Post element) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("post")!=null) {
		    	if(document.getInteger("post")==element.getDatabaseID()) {
		    	Comment comment = new Comment(this);
		    	comment.setDatabaseID(document.getInteger("databaseid"));
		    	comments.add(comment);
		    	}
		    }
		}
		return comments;
	}
		
	public Author getauthor(Post element) {
		ResultSet rs =mysql_test.findTable("Post");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Author author =new Author(this);
					author.setDatabaseID(rs.getInt("author"));
					return author;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Post getpost(Author element) {
		ResultSet rs =mysql_test.findTable("Author");
		try {
			while(rs.next())
			{
				if(rs.getInt("databaseid")==element.getDatabaseID())
				{
					Post post =new Post(this);
					post.setDatabaseID(rs.getInt("post"));
					return post;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Fan> getFans(Author element) {
		ResultSet rs =mysql_test.findTable("fans_Author");
		ArrayList<Fan> fans = new ArrayList<Fan>();
		try {
			while(rs.next())
			{
				if(rs.getInt("idol")==element.getDatabaseID())
				{
					Fan fan =new Fan(this);
					fan.setDatabaseID(rs.getInt("fans"));
					fans.add(fan);
				}
			}
			return fans;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Post getpost(Comment element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("post")!=null) {
		    	Post  post = new Post(this);
		    	post.setDatabaseID(document.getInteger("post"));
		    	return post;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<Comment> getReplies(Comment element) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("replyto")!=null) {
		    	if(document.getInteger("replyto")==element.getDatabaseID()) {
		    	Comment comment = new Comment(this);
		    	comment.setDatabaseID(document.getInteger("databaseid"));
		    	comments.add(comment);
		    	}
		    }
		}
		return comments;
	}
		
	public Comment getreplyto(Comment element) {
		MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		    if(document.getInteger("databaseid")==element.getDatabaseID()) {
		    	if(document.getInteger("replyto")!=null) {
		    	Comment  comment = new Comment(this);
		    	comment.setDatabaseID(document.getInteger("replyto"));
		    	return comment;
		    	}
		    }
		}
		return null;
	}
	
	public ArrayList<Author> getIdol(Fan element) {
		ResultSet rs =mysql_test.findTable("idol_Fan");
		ArrayList<Author> authors = new ArrayList<Author>();
		try {
			while(rs.next())
			{
				if(rs.getInt("fans")==element.getDatabaseID())
				{
					Author author =new Author(this);
					author.setDatabaseID(rs.getInt("idol"));
					authors.add(author);
				}
			}
			return authors;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Post findPostByTitle(String title){
		ResultSet rs =mysql_test.findTable("Post");
		try {
			while(rs.next())
			{	
				if(rs.getString("Title").equals(title))
	
				{
					Post post =new Post(this);
					post.setDatabaseID(rs.getInt("databaseid"));
					return post;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Post Title "+ title+" is not exist");
		return null;
	}
	
	public Author findAuthorByName(String name){
		ResultSet rs =mysql_test.findTable("Author");
		try {
			while(rs.next())
			{	
				if(rs.getString("name").equals(name))
	
				{
					Author author =new Author(this);
					author.setDatabaseID(rs.getInt("databaseid"));
					return author;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Author Name "+ name+" is not exist");
		return null;
	}
	
	public Comment findCommentByTitle(String title){
		MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
		FindIterable<Document> documents = table.find();
		for (Document document : documents) {
		if(document.get("title").equals(title)) {
		    	Comment comment =new Comment(this);
		    	comment.setDatabaseID(document.getInteger("databaseid"));
		    	return comment;
		    }
		}
		System.out.println("Comment Title "+ title+" is not exist");
		return null;
	}
	
	public Fan findFanByName(String name){
		ResultSet rs =mysql_test.findTable("Fan");
		try {
			while(rs.next())
			{	
				if(rs.getString("name").equals(name))
	
				{
					Fan fan =new Fan(this);
					fan.setDatabaseID(rs.getInt("databaseid"));
					return fan;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fan Name "+ name+" is not exist");
		return null;
	}
	
	public void deletePost(Post post) {
		if(post!=null){
			for(Comment element : post.getComments()){
				set(element, "post", null);
			}
			set(post.getAuthor(), "post", null);
			mysql_test.runSQL("DELETE FROM Post WHERE databaseid ="+ post.getDatabaseID());
		}
	}
	
	public void deleteAuthor(Author author) {
		if(author!=null){
			set(author.getPost(), "author", null);
			mysql_test.runSQL("DELETE FROM fans_Author WHERE idol ="+ author.getDatabaseID());
			mysql_test.runSQL("DELETE FROM idol_Fan WHERE idol ="+ author.getDatabaseID());
			mysql_test.runSQL("DELETE FROM Author WHERE databaseid ="+ author.getDatabaseID());
		}
	}
	
	public void deleteComment(Comment comment) {
		if(comment!=null){
			for(Comment element : comment.getReplies()){
				set(element, "replyto", null);
			}
			MongoCollection<Document> table = mongo_newtest.getCollection("Comment");
			table.deleteOne(Filters.eq("databaseid", comment.getDatabaseID()));
		}
	}
	
	public void deleteFan(Fan fan) {
		if(fan!=null){
			mysql_test.runSQL("DELETE FROM idol_Fan WHERE fans ="+ fan.getDatabaseID());
			mysql_test.runSQL("DELETE FROM fans_Author WHERE fans ="+ fan.getDatabaseID());
			mysql_test.runSQL("DELETE FROM Fan WHERE databaseid ="+ fan.getDatabaseID());
		}
	}
	
	public void deleteFans(Author author, Fan fan) {
		mysql_test.runSQL("DELETE FROM fans_Author WHERE fans ="+ author.getDatabaseID() +" and idol ="+ fan.getDatabaseID());			
	}
	
	public void deleteIdol(Fan fan, Author author) {
		mysql_test.runSQL("DELETE FROM idol_Fan WHERE idol ="+ fan.getDatabaseID() +" and fans ="+ author.getDatabaseID());			
	}
	
}

				
