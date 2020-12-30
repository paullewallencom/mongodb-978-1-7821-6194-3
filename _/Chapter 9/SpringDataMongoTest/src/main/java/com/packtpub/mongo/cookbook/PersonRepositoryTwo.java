/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.util.Collection;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.packtpub.mongo.cookbook.domain.Person2;

/**
 * @author Amol
 *
 */
public interface PersonRepositoryTwo extends MongoRepository<Person2 , Integer> {

	/**
	 * Finds all people with age greater than a minimum age
	 *
	 * @param minAge
	 * @return
	 */
	Collection<Person2> findByAgeGreaterThanEqual(int minAge);

	/**
	 * Finds all people with age greater than a minimum age
	 *
	 * @param minAge
	 * @param maxAge
	 * @return
	 */
	Collection<Person2> findByAgeBetween(int minAge, int maxAge);

	/**
	 * Finds all people ordered by their age with the minimum age a given in the first parameter
	 *
	 * @param minAge
	 * @param sort
	 * @return
	 */
	Collection<Person2> findByAgeGreaterThan(int minAge, Sort sort);


	/**
	 *
	 * @param lastNamepattern
	 * @return
	 */
	Collection<Person2> findPeopleByLastNameLike(String lastNamePattern);

	/**
	 *
	 * @param country
	 * @return
	 */
	Collection<Person2> findByResidentialAddressCountry(String country);

	/**
	 *
	 * @param country
	 * @return
	 */
	Collection<Person2> findByResidentialAddress_country(String country);

	//TODO: Put a @Query annotation and a query

	/**
	 *  Finds the {@link Person2} by first name and nationality. This is a method not following
	 *  the standard naming convention
	 *
	 * @param firstName
	 * @param country
	 * @return
	 */
	@Query("{'firstName':?0, 'residentialAddress.country': ?1}")
	Collection<Person2> findByFirstNameAndCountry(String firstName, String country);
}
