package com.hr.docsigning.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import com.hr.docsigning.model.SendAgreementVO;
import com.hr.docsigning.util.Constants;
import com.hr.docsigning.util.RestApiAgreements.DocumentIdentifierName;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class AdobeSignServiceTests {

	@Autowired
	AdobeSignService adobeSignService;

	SendAgreementVO sendAgreementVO;

	@Mock
	MultipartFile file1;

	MockMultipartFile testFile = new MockMultipartFile("data", "filename.txt", "text/plain", "test file".getBytes());

	@Before
	public void setup() {
		this.sendAgreementVO = new SendAgreementVO();
		this.sendAgreementVO.setSignerEmail("abhishekd@adobe.com");
		this.sendAgreementVO.setApproverEmail("abhishekdixitg@gmail.com");
		this.sendAgreementVO.setName("Test Message");
		this.sendAgreementVO.setMessage("Please sign");
	}

	@Test
	public void testsendContract() {
		String agreementId = null;
		try {
			final JSONObject uploadDocumentResponse = new JSONObject();
			uploadDocumentResponse.put(DocumentIdentifierName.TRANSIENT_DOCUMENT_ID, "abcf123456");

			final JSONObject sendAgreementResponse = new JSONObject();
			sendAgreementResponse.put(Constants.ID, "abcf123456");

			agreementId = this.adobeSignService.sendContract(this.sendAgreementVO, this.testFile);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(agreementId);

	}

}
