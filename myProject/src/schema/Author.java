package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Author{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Author(PolyglotDatabase db){
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
		if(this.getPost()==null) {
			db.set(this, "post", element.getDatabaseID());
			element.setAuthor(this);
		}
		else{
			if(this.getPost().getDatabaseID()!=element.getDatabaseID()) {
				db.set(this.getPost(), "Author", null);
				db.set(this, "post", element.getDatabaseID());
				element.setAuthor(this);
			}
		}
	}
	
	public void addFans(Fan element){
		boolean exist = false;
		for(Fan member: getFans())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setFans(this , element);
			db.setIdols(element, this);
		}		
			
	}
	
	public ArrayList<Fan> getFans(){
		return db.getFans(this);
	}
	
	public void deleteFans(Fan element){
		db.deleteFans(this , element);
	}
	
	public Fan Findfans(String name) {
		for(Fan element: getFans()) {
			if(element.getName().equals(name))
	
				return element;
		}
		return null;
	}	

	public int getFanYear(Fan fan) {
		return Integer.parseInt(db.get(this,fan.getDatabaseID(),"year"));
	}
	
	public void setFanYear(Fan fan, int year) {
		db.set(this,fan.getDatabaseID(),"year",year);
		db.set(fan,id,"year",year);
	}
	public String getName() {
		return db.get(this,"name");
	}
	
	public void setName(String name) {
		db.set(this,"name",name);
	}
	
	public int getAge() {
		return Integer.parseInt(db.get(this,"age"));
	}
	
	public void setAge(int age) {
		db.set(this,"age",age);
	}
	
}	

	
