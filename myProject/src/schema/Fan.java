package schema;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Fan{
	
	private int id;
	protected PolyglotDatabase db;
	
	public Fan(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setDatabaseID(int id) {
		this.id=id;
		
	}
	
	public int getDatabaseID() {
		return id;
	}
	
	public void addIdols(Author element){
		boolean exist = false;
		for(Author member: getIdols())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setIdols(this , element);
			db.setFans(element, this);
		}		
			
	}
	
	public ArrayList<Author> getIdols(){
		return db.getIdols(this);
	}
	
	public void deleteIdols(Author element){
		db.deleteIdols(this , element);
	}
	
	public Author Findidols(String name) {
		for(Author element: getIdols()) {
			if(element.getName().equals(name))
	
				return element;
		}
		return null;
	}	

	public int getAuthorYear(Author author) {
		return Integer.parseInt(db.getIdols(this,author.getDatabaseID(),"year"));
	}
	
	public void setAuthorYear(Author author, int year) {
		db.setIdols(this,author.getDatabaseID(),"year",year);
		db.setFans(author,id,"year",year);
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

	
