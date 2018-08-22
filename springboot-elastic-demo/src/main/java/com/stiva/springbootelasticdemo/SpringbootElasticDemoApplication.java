package com.stiva.springbootelasticdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={ElasticsearchDataAutoConfiguration.class})
public class SpringbootElasticDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootElasticDemoApplication.class, args);
	}
}
