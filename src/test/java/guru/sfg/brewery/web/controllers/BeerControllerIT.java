package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
public class BeerControllerIT {

	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMvc;

	@MockBean
	BeerRepository beerRepository;

	@MockBean
	BeerInventoryRepository beerInventoryRepository;

	@MockBean
	BreweryService breweryService;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	BeerService beerService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@WithMockUser("miro")
	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/beers/find"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("beer"))
				.andExpect(view().name("beers/findBeers"));
	}

	@Test
	void findBeers2() throws Exception {
//		mockMvc.perform(get("/beers/find").with(httpBasic("miroo", "murar")))		//S tymto to uz neprejde
		mockMvc.perform(get("/beers/find").with(httpBasic("miro", "murar")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("beer"))
				.andExpect(view().name("beers/findBeers"));
	}

	@Test
	void indexTest() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
	}

	@WithMockUser("miro")
	@Test
	void findBeers3() throws Exception {
		mockMvc.perform(get("/beers/find").with(anonymous()))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("beer"))
				.andExpect(view().name("beers/findBeers"));
	}


	@Nested
	class BeerRestControllerTest {
		@Test
		void findBeers() throws Exception{
			mockMvc.perform(get("/api/v1/beer/"))
					.andExpect(status().isOk());
		}

		@Test
		void findBeerById() throws Exception{
			mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c152"))
					.andExpect(status().isOk());
		}
	}


	@Test
	void initCreationForm() throws Exception {
		mockMvc.perform(get("/beers/new").with(httpBasic("michal", "kurbel")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("beer"))
				.andExpect(view().name("beers/createBeer"));
	}
}
