package com.circule.talent;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Locale;

@SpringBootApplication
@EnableJpaAuditing
public class TalentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalentApplication.class, args);
	}

	@Bean
	public Faker getFaker() {
		return new Faker(new Locale("en", "USA"));
	}

}
