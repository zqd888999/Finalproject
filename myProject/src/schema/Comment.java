package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Comment{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Comment(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public Post getPost(){
		return db.getpost(this);
	}
	
	public void setPost(Post element){
		db.setAttribute(this, "post", element.getDatabaseID());
	}
	
	public void addReplies(Comment element){
		boolean exist = false;
		for(Comment member: getReplies())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist)
			element.setReplyto(this);
	}
	
	public ArrayList<Comment> getReplies(){
		return db.getReplies(this);
	}
	
	public void deleteReplies(Comment element){
		db.setAttribute(element, "replyto", null);
	}
	
	public Comment Findreplies(String title) {
		for(Comment element: getReplies()) {
			if(element.getTitle().equals(title))
				return element;
		}
		return null;
	}

	public Comment getReplyto(){
		return db.getreplyto(this);
	}
	
	public void setReplyto(Comment element){
		db.setAttribute(this, "replyto", element.getDatabaseID());
	}
	
	public String getTitle() {
		return db.getAttribute(this,"title");
	}
	
	public void setTitle(String title) {
		db.setAttribute(this,"title",title);
	}
	
	public String getBody() {
		return db.getAttribute(this,"body");
	}
	
	public void setBody(String body) {
		db.setAttribute(this,"body",body);
	}
	
}	

	
