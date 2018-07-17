package schema;

import java.util.ArrayList;

public class Fan{
	
	protected int id;
	protected PolyglotDatabase db;
	
	public Fan(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setID(int id) {
		this.id=id;
		
	}
	
	public int getID() {
		return id;
	}
	
	public void addIdol(Author element){
		boolean exist = false;
		for(Author member: getIdol())
			if(member.getID()==element.getID())
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

