package com.hr.docsigning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hr.docsigning.model.SendAgreementVO;
import com.hr.docsigning.service.AdobeSignService;
import com.hr.docsigning.util.Constants;

import springfox.documentation.annotations.ApiIgnore;

/**
 * The Class AdobeSignController.
 */
@Controller
public class AdobeSignController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdobeSignController.class);

	/** The adobe sign service. */
	@Autowired
	AdobeSignService adobeSignService;

	/**
	 * Send contract method.
	 *
	 * @return the string
	 */
	@ApiIgnore
	@GetMapping(Constants.SEND_PAGE_ENDPOINT)
	public String sendContractMethod() {
		return Constants.SEND_FORM_HTML;
	}

	/**
	 * Send for signature.
	 *
	 * @param sendAgreementVO the send agreement VO
	 * @param file1           the file 1
	 * @param name            the name
	 * @param signerEmail     the signer email
	 * @param message         the message
	 * @param approverEmail   the approver email
	 * @return the string
	 */
	@PostMapping(Constants.SEND_FOR_SIGNATURE_ENDPOINT)
	public String sendForSignature(SendAgreementVO sendAgreementVO,
			@RequestParam(Constants.PARAM_FILE) MultipartFile file1, @RequestParam String name,
			@RequestParam String signerEmail, @RequestParam String message, @RequestParam String approverEmail) {
		sendAgreementVO.setSignerEmail(signerEmail);
		sendAgreementVO.setSignerEmail(approverEmail);
		sendAgreementVO.setMessage(message);
		sendAgreementVO.setName(name);
		final String agreementId = this.adobeSignService.sendContract(sendAgreementVO, file1);
		LOGGER.info(Constants.AGREEMENT_CREATED, agreementId);
		return Constants.COMMON_RETURN_PAGE;
	}
}
