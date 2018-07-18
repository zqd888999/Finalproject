package schema;

import java.util.ArrayList;

public class Person{
	
	protected int id;
	protected PolyglotDatabase db;
	
	public Person(PolyglotDatabase db){
		this.db = db;
	}
	
	public void setID(int id) {
		this.id=id;
		
	}
	
	public int getID() {
		return id;
	}
	
	public void addSons(Person element){
		element.setFather(this);
	}
	
	public ArrayList<Person> getSons(){
		return db.getSons(this);
	}
	
	public ArrayList<Person> getAllSons(){
		ArrayList<Person> all = (ArrayList<Person>) getSons().clone(); 
		for(Person member :getSons()) {
			if(member.getSons().size()!=0) {
				ArrayList<Person> allchild = member.getAllSons();
				if(allchild.size()>0)
					all.addAll(allchild);
			}
		}

		for(Person member :getDaughter()) {
			if(member.getSons().size()!=0) {
				ArrayList<Person> allchild = member.getAllSons();
				if(allchild.size()>0)
					all.addAll(allchild);
			}
		}

		return all;
	}
	
	public void addDaughter(Person element){
		element.setDad(this);
	}
	
	public ArrayList<Person> getDaughter(){
		return db.getDaughter(this);
	}
	
	public ArrayList<Person> getAllDaughter(){
		ArrayList<Person> all = (ArrayList<Person>) getDaughter().clone(); 
		for(Person member :getSons()) {
			if(member.getDaughter().size()!=0) {
				ArrayList<Person> allchild = member.getAllDaughter();
				if(allchild.size()>0)
					all.addAll(allchild);
			}
		}

		for(Person member :getDaughter()) {
			if(member.getDaughter().size()!=0) {
				ArrayList<Person> allchild = member.getAllDaughter();
				if(allchild.size()>0)
					all.addAll(allchild);
			}
		}

		return all;
	}
	
	public Person getDad(){
		return db.getdad(this);
	}
	
	public void setDad(Person element){
		db.set(this, "dad", element.getID());
	}
	
	public Person getFather(){
		return db.getfather(this);
	}
	
	public void setFather(Person element){
		db.set(this, "father", element.getID());
	}
	
}	
