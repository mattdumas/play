package models;

import play.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Entity
public class Topic extends JPAModel {
	
	public String subject;
	public Integer views;
	@ManyToOne public Forum forum;
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="topic") public List<Post> posts;
	
	// ~~~~~~~~~~~~ 
	
	public Topic(Forum forum, User by, String subject, String content) {
		this.forum = forum;
		this.subject = subject;
		this.views = 0;
		new Post(this, by, content);
		save();
	}
	
	// ~~~~~~~~~~~~ 
	
	public Post reply(User by, String content) {
		return new Post(this, by, content);
	}
	
	// ~~~~~~~~~~~~ 
	
	public List<Post> getPosts(int page, int pageSize) {
		return Post.find("topic", this).page(page, pageSize);
	}
	
	public Long getPostsCount() {
		return Post.count("topic", this);
	}
	
	public Long getVoicesCount() {
		return User.count("select count(distinct u) from User u, Topic t, Post p where p.postedBy = u and p.topic = t and t = ?", this);
	}
	
	public Post getLastPost() {
		return Post.find("topic = ? order by postedAt desc", this).one();
	}
		
}
