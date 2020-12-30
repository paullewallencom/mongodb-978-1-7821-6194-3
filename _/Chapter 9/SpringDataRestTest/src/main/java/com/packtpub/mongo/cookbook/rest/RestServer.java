/**
 *
 */
package com.packtpub.mongo.cookbook.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 * @author Amol
 *
 */
@Configuration
@EnableMongoRepositories(basePackages="com.packtpub.mongo.cookbook")
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
public class RestServer {

	public static void main(String[] args) {
		SpringApplication.run(RestServer.class, args);
	}
}
