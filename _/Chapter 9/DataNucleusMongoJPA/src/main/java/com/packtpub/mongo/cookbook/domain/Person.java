/**
 *
 */
package com.packtpub.mongo.cookbook.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author Amol
 *
 */
@Entity
@Table(name="personJPA")
public class Person {

	@Column(name="_id")
	@Id
	private Long id;

	@Column(name="firstName")
	private String firstName;

	@Column(name="ln")
	private String lastName;

	@Column(name="age")
	private int age;

	@Column(name="gender")
	private Gender gender;

	private ResidentialAddress residentialAddress;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public ResidentialAddress getResidentialAddress() {
		return residentialAddress;
	}
	public void setResidentialAddress(ResidentialAddress residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
}
