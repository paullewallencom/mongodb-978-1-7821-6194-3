/**
 *
 */
package com.packtpub.mongo.cookbook;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.packtpub.mongo.cookbook.domain.Person;



/**
 * @author Amol
 *
 */
@RepositoryRestResource(path="people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Integer> {

	/**
	 *
	 * @param lastName
	 * @return
	 */
	Person findByLastName(@Param("lastName") String lastName);
}
