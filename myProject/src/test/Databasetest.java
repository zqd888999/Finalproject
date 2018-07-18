package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import schema.Author;
import schema.Comment;
import schema.Fan;
import schema.PolyglotDatabase;
import schema.Post;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Databasetest {

	protected PolyglotDatabase db;
	protected Post p;
	protected Author a;
	protected Author a1;
	protected Fan f;
	protected Fan f1;
	protected Comment c1;
	protected Comment c2;
	protected Comment c3;
	protected Comment c4;
	protected Comment c5;
	
	
	@Before
	public void setUp() throws Exception {
		db = new PolyglotDatabase();
		p = db.createPost();
		p.setTitle("i am ok");
		p.setPage(1);
		p.setPrice(1.2);
		a = db.createAuthor();
		a1 = db.createAuthor();
		a.setName("dong");
		a.setAge(23);
		a1.setName("frank");
		a1.setAge(24);
		f = db.createFan();
		f1 = db.createFan();
		f.setName("jack");
		f.setAge(14);
		f1.setName("mary");
		f1.setAge(18);
		c1 = db.createComment();
		c2 = db.createComment();
		c3 = db.createComment();
		c4 = db.createComment();
		c5 = db.createComment();
		c1.setTitle("11");
		c2.setTitle("12");
		c3.setTitle("13");
		c4.setTitle("14");
		c5.setTitle("15");
		c1.setBody("i am 11");
		c2.setBody("i am 12");
		c3.setBody("i am 13");
		c4.setBody("i am 14");
		c5.setBody("i am 15");
	}
	
	
	@Test
	public void Test1_PostGetandSet() {
		assertEquals("i am ok", p.getTitle());
		assertEquals(1,p.getPage());
		assertEquals(1.2,p.getPrice(),1e-5);
		
		//update new data
		p.setPage(2);
		assertEquals(2,p.getPage());
	}
	
	@Test
	public void Test2_AuthorGetandSet(){
		assertEquals("dong", a.getName());
		assertEquals(23,a.getAge());
		assertEquals("frank",a1.getName());
		assertEquals(24,a1.getAge());
	}
	
	@Test
	public void Test3_GetandSetFan(){
		assertEquals("jack", f.getName());
		assertEquals(14,f.getAge());
		assertEquals("mary",f1.getName());
		assertEquals(18,f1.getAge());
	}
	
	@Test
	public void Test4_CommentGetandSet(){
		assertEquals("11", c1.getTitle());
		assertEquals("12", c2.getTitle());
		assertEquals("13", c3.getTitle());
		assertEquals("14", c4.getTitle());
		assertEquals("15", c5.getTitle());
		assertEquals("i am 11", c1.getBody());
		assertEquals("i am 12", c2.getBody());
		assertEquals("i am 13", c3.getBody());
		assertEquals("i am 14", c4.getBody());
		assertEquals("i am 15", c5.getBody());
	}
	
	@Test
	public void Test5_Find(){
		Post p2 = db.findPostByTitle("i am ok");
		assertEquals("i am ok", p2.getTitle());
		assertEquals(1,p2.getPage());
		assertEquals(1.2,p2.getPrice(),1e-5);
		
		Author a2 = db.findAuthorByName("dong");
		assertEquals("dong", a2.getName());
		assertEquals(23,a2.getAge());
		
		Fan f2 = db.findFanByName("jack");
		assertEquals("jack", f2.getName());
		assertEquals(14,f2.getAge());
		
		Comment c6 = db.findCommentByTitle("11");
		assertEquals("i am 11", c6.getBody());
	}
	
	@Test
	public void Test6_setReference(){	
		p.setAuthor(a);
		assertEquals("dong", p.getAuthor().getName());
		assertEquals("i am ok", a.getPost().getTitle());
		
		a.addFans(f);
		a.addFans(f1);
		f.addIdol(a);
		f.addIdol(a1);
		assertEquals(2,a.getFans().size());
		assertEquals(2,f.getIdol().size());
		assertEquals("jack",a.Findfans("jack").getName());
		assertEquals("frank",f.Findidol("frank").getName());
		
		p.addComments(c1);
		p.addComments(c1);
		p.addComments(c2);
		assertEquals(2,p.getComments().size());
		assertEquals("i am 12",p.Findcomments("12").getBody());
		
		c1.addReplies(c3);
		c1.addReplies(c4);
		c3.addReplies(c5);
		assertEquals(3,c1.getAllReplies().size());
		assertEquals("i am 14", c1.Findreplies("14").getBody());
		assertEquals("i am 15", c1.Findreplies("15").getBody());
		
		//update references
		p.setAuthor(a1);
		assertEquals("frank", p.getAuthor().getName());
		assertEquals("i am ok", a1.getPost().getTitle());
		assertEquals(null,db.get(a, "post"));
		a.setPost(p);
		assertEquals("dong", p.getAuthor().getName());
		assertEquals("i am ok", a.getPost().getTitle());
		
		f.deleteIdol(a);
		assertEquals(1,f.getIdol().size());
	}
	
	@After
	public void Delete(){
		db.deletePost(p);
		db.deleteAuthor(a);		
		db.deleteAuthor(a1);
		db.deleteFan(f);
		db.deleteFan(f1);
		db.deleteComment(c1);
		db.deleteComment(c2);
		db.deleteComment(c3);
		db.deleteComment(c4);
		db.deleteComment(c5);
	}
	

}
