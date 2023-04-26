package com.hr.docsigning.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.net.URI;

import org.apache.commons.collections.map.MultiValueMap;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.hr.docsigning.model.ExitForm;
import com.hr.docsigning.model.PersonForm;
import com.hr.docsigning.service.FileStorageService;
import com.hr.docsigning.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestRestTemplate restTemplate;

	@Mock
	FileStorageService fileStorageService;

	@LocalServerPort
	int randomServerPort;

	MockMultipartFile testFile1 = new MockMultipartFile("data1", "filename.pdf", "application/pdf",
			"test1 file".getBytes());
	MockMultipartFile testFile2 = new MockMultipartFile("data2", "filename.pdf", "application/pdf",
			"test2 file".getBytes());

	@Test
	public void testcheckPersonInfo() throws Exception {
		final String uriStr = Constants.WELCOME_ENDPOINT;
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<ExitForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testcombineContract() throws Exception {
		final String uriStr = Constants.COMBINE_ENDPOINT;
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<ExitForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	// @Test
	public void testcombineContracts() throws Exception {
		final String uriStr = Constants.MERGE_ENDPOINT + "?" + Constants.PARAM_FILES + "="
				+ new MockMultipartFile[] { this.testFile1, this.testFile2 };
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final MockMultipartFile mockMultipartFile[] = new MockMultipartFile[] { this.testFile1, this.testFile2 };
		final MultiValueMap map = new MultiValueMap();
		map.put(Constants.PARAM_FILES, map);

		final URI uri = new URI(uriStr);

		final MvcResult requestResult = this.mockMvc.perform(multipart(uriStr)).andReturn();

		assertEquals(200, requestResult.getResponse().getStatus());
	}

	@Test
	public void testcreatePDF() throws Exception {
		final String uriStr = Constants.CREATE_ENDPOINT;
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<ExitForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testdownloadContract() throws Exception {
		final String uriStr = Constants.LOGIN_PAGE_ENDPOINT;
		final PersonForm personForm = new PersonForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<PersonForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testexitForm() throws Exception {
		final String uriStr = Constants.EXIT_FORM_ENDPOINT;
		final ExitForm personForm = new ExitForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<ExitForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testrearrangeContractPage() throws Exception {
		final String uriStr = Constants.REARRANGE_PAGE_ENDPOINT;
		final PersonForm personForm = new PersonForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<PersonForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void testshowForm() throws Exception {
		final String uriStr = Constants.PDF_ENDPOINT;
		final PersonForm personForm = new PersonForm();
		personForm.setFirstName("Abhishek");

		final URI uri = new URI(uriStr);
		final HttpHeaders headers = new HttpHeaders();

		final HttpEntity<PersonForm> request = new HttpEntity<>(personForm, headers);

		final ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);

		assertEquals(200, result.getStatusCodeValue());
	}

}
