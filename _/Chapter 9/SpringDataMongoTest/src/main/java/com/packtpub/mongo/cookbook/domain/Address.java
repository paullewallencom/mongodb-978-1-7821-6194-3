/**
 *
 */
package com.packtpub.mongo.cookbook.domain;

/**
 * @author Amol
 *
 */
public class Address {

	private final String addressLineOne;
	private final String addressLineTwo;
	private final String city;
	private final String state;
	private final String country;
	private final String zip;

	/**
	 *
	 * @param addressLineOne
	 * @param addressLineTwo
	 * @param city
	 * @param state
	 * @param country
	 * @param zip
	 */
	public Address(String addressLineOne, String addressLineTwo, String city,
			String state, String country, String zip) {
		super();
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zip = zip;
	}

	/**
	 *
	 * @return
	 */
	public String getAddressLineOne() {
		return addressLineOne;
	}

	/**
	 *
	 * @return
	 */
	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	/**
	 *
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 *
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 *
	 * @return
	 */
	public String getCountry() {
		return country;
	}

	/**
	 *
	 * @return
	 */
	public String getZip() {
		return zip;
	}
}
