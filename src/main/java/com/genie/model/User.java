package com.genie.model;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.genie.utils.DataFormatter;

/**
 * @author ccubukcu
 * 
 */
@Entity
@Audited
@Table(name = "users")
public class User extends BaseEntity implements LoggableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "username", length = 50, unique = true)
	private String username;

	@Column(name = "password", length = 255)
	private String password;

	@Column(name = "name")
	private String fullName;

	@Column(name = "email")
	private String email;
	
	@Column(name = "gender")
	private Integer gender;
	
	@Column(name = "birthdate")
	private Date birthdate;
	
	@Column(name = "biography", length=3000)
	private String biography;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Authority> authorities;

	@Column(name = "cover_picture")
	@Lob
	private Blob coverPicture;

	@Column(name = "pass_token")
	private String passToken;

	@Column(name = "valid_until")
	private Date tokenValidUntil;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StudentGrade> grades;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StudentAttendance> attendances;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (username.equals(other.username)) {
			return true;
		}
		if (username == null || other.username == null) {
			return false;
		}
		if (!username.equals("") && !other.username.equals("")) {
			if (!username.equals(other.username)) {
				return false;
			}
		}

		return true;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public Blob getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(Blob coverPicture) {
		this.coverPicture = coverPicture;
	}

	public String getPassToken() {
		return passToken;
	}

	public void setPassToken(String passToken) {
		this.passToken = passToken;
	}

	public Date getTokenValidUntil() {
		return tokenValidUntil;
	}

	public void setTokenValidUntil(Date tokenValidUntil) {
		this.tokenValidUntil = tokenValidUntil;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public List<StudentGrade> getGrades() {
		return grades;
	}

	public void setGrades(List<StudentGrade> grades) {
		this.grades = grades;
	}

	public List<StudentAttendance> getAttendances() {
		return attendances;
	}

	public void setAttendances(List<StudentAttendance> attendances) {
		this.attendances = attendances;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getEncodedImage() {
		if(coverPicture != null)
			return DataFormatter.getBase64EncodedImage(coverPicture);
		else return "";
	}
}
