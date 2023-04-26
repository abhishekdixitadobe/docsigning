package com.hr.docsigning.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.hr.docsigning.model.ExitForm;
import com.hr.docsigning.service.FileStorageService;
import com.hr.docsigning.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StaticDataControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate restTemplate;

	@Mock
	FileStorageService fileStorageService;

	@LocalServerPort
	int randomServerPort;

	@Test
	public void testdefectDetails() throws Exception {
		final String uriStr = Constants.ABOUTUS_DETAILS_ENDPOINT;
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<ExitForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

}
