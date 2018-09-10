package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Post{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Post(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addComments(Comment element){
		boolean exist = false;
		for(Comment member: getComments())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setPost(this);
	}
	
	public ArrayList<Comment> getComments(){
		return db.getComments(this);
	}
	
	public void deleteComments(Comment element){
		db.setAttribute(element, "post", null);
	}
	
	public Comment Findcomments(String title) {
		for(Comment element: getComments()) {
			if(element.getTitle().equals(title))
				return element;
		}
		return null;
	}

	public Author getAuthor(){
		return db.getauthor(this);
	}
	
	public void setAuthor(Author element){
		if(this.getAuthor()==null) {
			db.setAttribute(this, "author", element.getDatabaseID());
			element.setPost(this);
		}
		else{
			if(this.getAuthor().getDatabaseID()!=element.getDatabaseID()) {
				db.setAttribute(this.getAuthor(), "Post", null);
				db.setAttribute(this, "author", element.getDatabaseID());
				element.setPost(this);
			}
		}
	}
	
	public String getTitle() {
		return db.getAttribute(this,"Title");
	}
	
	public void setTitle(String Title) {
		db.setAttribute(this,"Title",Title);
	}
	
	public int getPage() {
		return Integer.parseInt(db.getAttribute(this,"Page"));
	}
	
	public void setPage(int Page) {
		db.setAttribute(this,"Page",Page);
	}
	
	public double getPrice() {
		return Double.valueOf(db.getAttribute(this,"Price"));
	}
	
	public void setPrice(double Price) {
		db.setAttribute(this,"Price",Price);
	}
	
}	

	
