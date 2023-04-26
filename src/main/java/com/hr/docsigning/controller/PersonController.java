package com.hr.docsigning.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hr.docsigning.model.ExitForm;
import com.hr.docsigning.model.PersonForm;
import com.hr.docsigning.model.SendAgreementVO;
import com.hr.docsigning.service.AdobeSignService;
import com.hr.docsigning.service.FileStorageService;
import com.hr.docsigning.util.Constants;

import springfox.documentation.annotations.ApiIgnore;

/**
 * The Class PersonController.
 */
@Controller
public class PersonController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

	/** The adobe sign service. */
	@Autowired
	AdobeSignService adobeSignService;

	/** The file storage service. */
	@Autowired
	FileStorageService fileStorageService;

	/**
	 * Check person info.
	 *
	 * @param personForm    the person form
	 * @param bindingResult the binding result
	 * @param model         the model
	 * @return the string
	 */
	@PostMapping(Constants.WELCOME_ENDPOINT)
	public String checkPersonInfo(@Valid PersonForm personForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return Constants.LOGIN_HTML;
		}
		model.addAttribute(Constants.NAME, personForm.getFirstName() + " " + personForm.getLastName());
		model.addAttribute(Constants.FIRST_NAME, personForm.getFirstName());
		model.addAttribute(Constants.LAST_NAME, personForm.getLastName());
		return Constants.WELCOME_HTML_PAGE;
	}

	/**
	 * Combine contract.
	 *
	 * @param personForm    the person form
	 * @param bindingResult the binding result
	 * @return the string
	 */
	@ApiIgnore
	@RequestMapping(Constants.COMBINE_ENDPOINT)
	public String combineContract(@Valid PersonForm personForm, BindingResult bindingResult) {
		return Constants.COMBINE_CONTRACT_HTML;
	}

	/**
	 * Combine contracts.
	 *
	 * @param files      the files
	 * @param modal      the modal
	 * @param attributes the attributes
	 * @return the string
	 */
	@PostMapping(Constants.MERGE_ENDPOINT)
	public String combineContracts(@RequestParam(Constants.PARAM_FILES) MultipartFile[] files, Model modal,
			RedirectAttributes attributes) {
		this.fileStorageService.mergeSelectedFile(files);

		return Constants.COMMON_RETURN_PAGE;
	}

	/**
	 * Creates the PDF.
	 *
	 * @param exitForm      the exit form
	 * @param bindingResult the binding result
	 * @return the string
	 */
	@PostMapping(Constants.CREATE_ENDPOINT)
	public String createPDF(@Valid ExitForm exitForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return Constants.LOGIN_HTML;
		}

		this.fileStorageService.createContract(exitForm);
		return Constants.COMMON_RETURN_PAGE;
	}

	/**
	 * Download contract.
	 *
	 * @param response the response
	 */
	@GetMapping(Constants.PDF_ENDPOINT)
	public void downloadContract(HttpServletResponse response) {
		final Path file = Paths.get(Constants.CONTRACT_FILE_PATH);

		response.setContentType(Constants.APPLICATION_PDF);
		response.addHeader(Constants.CONTENT_DISPOSITION, Constants.HEADER_ATTACHMENT);

		try {
			Files.copy(file, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (final IOException ex) {
			LOGGER.error(Constants.DOWNLOAD_CONTRACT_ERROR_MSG, ex.getMessage());
		}
	}

	/**
	 * Exit form.
	 *
	 * @param exitForm the exit form
	 * @return the string
	 */
	@ApiIgnore
	@PostMapping(Constants.EXIT_FORM_ENDPOINT)
	public String exitForm(@Valid ExitForm exitForm) {

		return Constants.EXIT_FORM_HTML;
	}

	/**
	 * Generate and Send for signature.
	 *
	 * @param sendAgreementVO the send agreement VO
	 * @param file1           the file 1
	 * @param name            the name
	 * @param signerEmail     the signer email
	 * @param message         the message
	 * @param approverEmail   the approver email
	 * @return the string
	 */
	@PostMapping(Constants.GENERATE_AND_SEND_FOR_SIGNATURE_ENDPOINT)
	public String generateAndSendForSignature(SendAgreementVO sendAgreementVO, @RequestParam String name,
			@RequestParam String signerEmail, @RequestParam String message, @RequestParam String approverEmail) {
		sendAgreementVO.setSignerEmail(signerEmail);
		sendAgreementVO.setSignerEmail(approverEmail);
		sendAgreementVO.setMessage(message);
		sendAgreementVO.setName(name);
		final String agreementId = this.fileStorageService.generateAndSendContract(sendAgreementVO);
		LOGGER.info(Constants.AGREEMENT_CREATED, agreementId);
		return Constants.COMMON_RETURN_PAGE;
	}

	@ApiIgnore
	@RequestMapping(Constants.GENERATE_AND_SEND_PDF_ENDPOINT)
	public String generateAndSendPDF(@Valid PersonForm personForm, BindingResult bindingResult) {
		return Constants.GENERATE_AND_SEND_CONTRACT_HTML;
	}

	@PostMapping(Constants.GENERATE_FILE_ENDPOINT)
	public String generateContracts(@RequestParam(Constants.PARAM_FILE) MultipartFile file1) {
		this.fileStorageService.generateFile(file1);
		return Constants.COMMON_RETURN_PAGE;
	}

	@ApiIgnore
	@RequestMapping(Constants.GENERATE_PDF_ENDPOINT)
	public String generatePDF(@Valid PersonForm personForm, BindingResult bindingResult) {
		return Constants.GENERATE_PDF_CONTRACT_HTML;
	}

	/**
	 * Rearrange contract page.
	 *
	 * @param personForm    the person form
	 * @param bindingResult the binding result
	 * @return the string
	 */
	@ApiIgnore
	@RequestMapping(Constants.REARRANGE_PAGE_ENDPOINT)
	public String rearrangeContractPage(@Valid PersonForm personForm, BindingResult bindingResult) {
		return Constants.REARRANGE_CONTRACT_HTML;
	}

	/**
	 * Rearrange contracts.
	 *
	 * @param file1        the file 1
	 * @param startIndex   the start index
	 * @param reorderIndex the reorder index
	 * @param lastIndex    the last index
	 * @return the string
	 */
	@PostMapping(Constants.REARRANGE_FILE_ENDPOINT)
	public String rearrangeContracts(@RequestParam(Constants.PARAM_FILE) MultipartFile file1,
			@RequestParam String startIndex, @RequestParam String reorderIndex, @RequestParam String lastIndex) {
		this.fileStorageService.rearrangeFile(file1, startIndex, lastIndex, reorderIndex);
		return Constants.COMMON_RETURN_PAGE;
	}

	/**
	 * Show form.
	 *
	 * @param personForm the person form
	 * @return the string
	 */
	@ApiIgnore
	@GetMapping(Constants.LOGIN_PAGE_ENDPOINT)
	public String showForm(PersonForm personForm) {
		return Constants.LOGIN_HTML;
	}

}
