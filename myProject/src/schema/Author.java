package schema;

import java.util.ArrayList;

public class Author{
	
	protected int id;
	protected PolyglotDatabase db;
	
	public Author(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setID(int id) {
		this.id=id;
		
	}
	
	public int getID() {
		return id;
	}
	
	public Post getPost(){
		return db.getpost(this);
	}
	
	public void setPost(Post element){
		if(this.getPost()==null) {
			db.set(this, "post", element.getID());
			element.setAuthor(this);
		}
		else{
			if(this.getPost().getID()!=element.getID()) {
				db.set(this.getPost(), "Author", null);
				db.set(this, "post", element.getID());
				element.setAuthor(this);
			}
		}
	}
	
	public void addFans(Fan element){
		boolean exist = false;
		for(Fan member: getFans())
			if(member.getID()==element.getID())
				exist =true;
		if(!exist){
			db.setFans(this , element);
			db.setIdol(element, this);
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

