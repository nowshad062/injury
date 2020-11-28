package net.javaguides.springboot.model;

import com.opencsv.bean.CsvBindByName;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long id;

	@OneToOne(cascade= CascadeType.ALL)
	private Role role;

	@CsvBindByName(column = "name")
	private String name;

	@CsvBindByName(column = "posting")
	private String facility;

	@NotNull
	@CsvBindByName(column = "code")
	@Size(min = 6, max = 6)
//	@Pattern(regexp="^(1[2-3][0-9][0-9][0-9][0-9])", message="Wrong code!")
	private String code;
	@CsvBindByName(column = "contact")
	private String contact;
	@CsvBindByName(column = "email")
	private String email;
	@CsvBindByName(column = "Batch")
	private String batch;

	private String password;
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(
//			name = "users_roles",
//			joinColumns = @JoinColumn(
//		            name = "user_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(
//				            name = "role_id", referencedColumnName = "id"))
	
//	private Collection<Role> roles;

	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<VoterPost> posts;

	@OneToOne
	private VoterPost nominatedPost;

	//private Long voteForChairman;

	//private Long voteForVChairman;

	//private Long voteForSecretary;

	private String voteForJSecretary;

	//private Long voteForKosha;

	private String voteForSSompadok;

	private Long voteForDoptor;

	private Long voteForPcsompadok;
	
	public User() {
		
	}
	
	public User(String name, String facility, String code, String contact, Set<VoterPost> posts) {
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

	public Set<VoterPost> getPosts() {
		return posts;
	}

	public void setPosts(Set<VoterPost> posts) {
		this.posts = posts;
	}

	public VoterPost getNominatedPost() {
		return nominatedPost;
	}

	public void setNominatedPost(VoterPost nominatedPost) {
		this.nominatedPost = nominatedPost;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public Long getVoteForChairman() {
//		return voteForChairman;
//	}
//
//	public void setVoteForChairman(Long voteForChairman) {
//		this.voteForChairman = voteForChairman;
//	}
//
//	public Long getVoteForSecretary() {
//		return voteForSecretary;
//	}
//
//	public void setVoteForSecretary(Long voteForSecretary) {
//		this.voteForSecretary = voteForSecretary;
//	}


//	public Long getVoteForKosha() {
//		return voteForKosha;
//	}
//
//	public void setVoteForKosha(Long voteForKosha) {
//		this.voteForKosha = voteForKosha;
//	}

	public Long getVoteForDoptor() {
		return voteForDoptor;
	}

	public void setVoteForDoptor(Long voteForDoptor) {
		this.voteForDoptor = voteForDoptor;
	}

	public Long getVoteForPcsompadok() {
		return voteForPcsompadok;
	}

	public void setVoteForPcsompadok(Long voteForPcsompadok) {
		this.voteForPcsompadok = voteForPcsompadok;
	}

//	public Long getVoteForVChairman() {
//		return voteForVChairman;
//	}
//
//	public void setVoteForVChairman(Long voteForVChairman) {
//		this.voteForVChairman = voteForVChairman;
//	}


	public String getVoteForJSecretary() {
		return voteForJSecretary;
	}

	public void setVoteForJSecretary(String voteForJSecretary) {
		this.voteForJSecretary = voteForJSecretary;
	}

	public String getVoteForSSompadok() {
		return voteForSSompadok;
	}

	public void setVoteForSSompadok(String voteForSSompadok) {
		this.voteForSSompadok = voteForSSompadok;
	}
}
