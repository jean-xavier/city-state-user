package com.challenge.citystateuser;

import com.challenge.citystateuser.domain.models.entities.City;
import com.challenge.citystateuser.domain.models.entities.Customer;
import com.challenge.citystateuser.domain.models.entities.State;
import com.challenge.citystateuser.domain.models.repositories.CityRepository;
import com.challenge.citystateuser.domain.models.repositories.CustomerRepository;
import com.challenge.citystateuser.domain.models.repositories.StateRespository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CityStateUserApplication {

	@Bean
	CommandLineRunner init(CustomerRepository customerRepository, StateRespository stateRespository, CityRepository cityRepository) {
		return args -> {
			final List<State> states = Arrays.asList(
					State.builder().name("São Paulo").build(),
					State.builder().name("Bahia").build());

			stateRespository.saveAll(states);

			final List<City> cities = Arrays.asList(
					City.builder().name("Salvador").state(states.get(0)).build(),
					City.builder().name("São Paulo").state(states.get(1)).build(),
					City.builder().name("Teste").state(states.get(1)).build(),
					City.builder().name("Teste").state(states.get(0)).build()
			);

			cityRepository.saveAll(cities);

			final List<Customer> customers = Arrays.asList(
					Customer.builder().cityLive(cities.get(0)).age(24).fullname("Jean Santos")
							.birthDate(LocalDate.parse("1996-07-11")).gender("Masculino").build(),
					Customer.builder().cityLive(cities.get(1)).age(22).fullname("Geovani Santos")
							.birthDate(LocalDate.parse("1998-12-05")).gender("Masculino").build(),
					Customer.builder().cityLive(cities.get(1)).age(73).fullname("João Santos")
							.birthDate(LocalDate.parse("1948-12-05")).gender("Masculino").build());

			customerRepository.saveAll(customers);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(CityStateUserApplication.class, args);
	}

}
