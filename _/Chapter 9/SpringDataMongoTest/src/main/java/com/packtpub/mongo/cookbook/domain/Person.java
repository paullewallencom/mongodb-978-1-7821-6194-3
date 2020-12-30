/**
 *
 */
package com.packtpub.mongo.cookbook.domain;

import org.springframework.data.annotation.Id;

/**
 * @author Amol
 *
 */
public class Person {

	@Id
	private Integer id;
	private String firstName;
	private String lastName;
	private Integer age;
	private Gender gender;

	/**
	 *
	 */
	public Person() {

	}

	/**
	 *
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 */
	public Person(Integer id, String firstName, String lastName, Integer age,
			Gender gender) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
	}

	/***
	 *
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
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
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @param age
	 */
	public void setAge(Integer age) {
		this.age = age;
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
	 * @param gender
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", age=" + age + ", gender=" + gender + "]";
	}


}
