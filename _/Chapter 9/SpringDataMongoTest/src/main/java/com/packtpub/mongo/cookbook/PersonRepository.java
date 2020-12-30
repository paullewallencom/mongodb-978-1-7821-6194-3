/**
 *
 */
package com.packtpub.mongo.cookbook;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.packtpub.mongo.cookbook.domain.Person;



/**
 * @author Amol
 *
 */
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>{

	/**
	 *
	 * @param lastName
	 * @return
	 */
	Person findByLastName(String lastName);
}
