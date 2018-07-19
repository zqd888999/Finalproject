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
	
	public void addIdol(Author element){
		boolean exist = false;
		for(Author member: getIdol())
			if(member.getDatabaseID()==element.getDatabaseID())
				exist =true;
		if(!exist){
			db.setIdol(this , element);
			db.setFans(element, this);
		}		
			
	}
	
	public ArrayList<Author> getIdol(){
		return db.getIdol(this);
	}
	
	public void deleteIdol(Author element){
		db.deleteIdol(this , element);
	}
	
	public Author Findidol(String name) {
		for(Author element: getIdol()) {
			if(element.getName().equals(name))
	
				return element;
		}
		return null;
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

