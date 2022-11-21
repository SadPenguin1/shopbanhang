package project3.Shoppee.Entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="user")
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull
	private Integer id;
	
	private String name;
	
	private String username;
	private String password;
	
	private String email;
	
	@CreatedDate
	//@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date birthdate;
	
//	
//	@ElementCollection
//	@CollectionTable(name ="role", joinColumns = @JoinColumn(name ="user_id"))
//	@Column(name ="role")
	//private List<String> roles;
	//["ADMIN,MEMBER"]

	@OneToMany(mappedBy ="user",fetch = FetchType.EAGER)
	List<UserRole> roles;
	
	@OneToMany(mappedBy ="user",fetch = FetchType.LAZY)
	List<Bill> bills;
	
	
	
	

}
