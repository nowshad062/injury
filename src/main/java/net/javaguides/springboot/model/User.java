package net.javaguides.springboot.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class User {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;

	@CsvBindByName(column = "Provider")
	private String name;

	@CsvBindByName(column = "Facility")
	private String facility;
	@CsvBindByName(column = "Code")
	private String code;
	@CsvBindByName(column = "Contact")
	private String contact;
	@CsvBindByName(column = "Batch")
	private String batch;
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(
//			name = "users_roles",
//			joinColumns = @JoinColumn(
//		            name = "user_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(
//				            name = "role_id", referencedColumnName = "id"))
	
//	private Collection<Role> roles;

	@OneToMany(fetch=FetchType.EAGER)
	private List<VoterPost> posts;

	@OneToOne
	private VoterPost nominatedPost;
	
	public User() {
		
	}
	
	public User(String name, String facility, String code, String contact, List<VoterPost> posts) {
		super();
		this.name = name;
		this.facility = facility;
		this.code = code;
		this.contact = contact;
		this.posts = posts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public List<VoterPost> getPosts() {
		return posts;
	}

	public void setPosts(List<VoterPost> posts) {
		this.posts = posts;
	}

	public VoterPost getNominatedPost() {
		return nominatedPost;
	}

	public void setNominatedPost(VoterPost nominatedPost) {
		this.nominatedPost = nominatedPost;
	}
}
