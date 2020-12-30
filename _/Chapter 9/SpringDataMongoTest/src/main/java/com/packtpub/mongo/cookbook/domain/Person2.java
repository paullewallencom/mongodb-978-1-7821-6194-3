/**
 *
 */
package com.packtpub.mongo.cookbook.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Amol
 *
 */
@Document(collection="personTwo")
public class Person2 {

	@Id
	private final Integer id;
	private final String firstName;
	private final String lastName;
	private final Integer age;
	private final Gender gender;
	private final Address residentialAddress;


	/**
	 *
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param residentialAddress
	 */
	public Person2(Integer id, String firstName, String lastName, Integer age,
			Gender gender, Address residentialAddress) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.residentialAddress = residentialAddress;
	}




	/**
	 *
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 *
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 *
	 * @return
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 *
	 * @return
	 */
	public Gender getGender() {
		return gender;
	}


	/**
	 *
	 * @return
	 */
	public Address getResidentialAddress() {
		return residentialAddress;
	}


	@Override
	public String toString() {
		return "Person2 [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", age=" + age + ", gender=" + gender + "]";
	}
}
