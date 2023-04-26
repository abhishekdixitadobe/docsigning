package com.hr.docsigning.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.adobe.pdfservices.operation.ExecutionContext;
import com.adobe.pdfservices.operation.auth.Credentials;
import com.adobe.pdfservices.operation.exception.SdkException;
import com.adobe.pdfservices.operation.exception.ServiceApiException;
import com.adobe.pdfservices.operation.exception.ServiceUsageException;
import com.adobe.pdfservices.operation.io.FileRef;
import com.adobe.pdfservices.operation.pdfops.CombineFilesOperation;
import com.adobe.pdfservices.operation.pdfops.CreatePDFOperation;
import com.adobe.pdfservices.operation.pdfops.DocumentMergeOperation;
import com.adobe.pdfservices.operation.pdfops.ReorderPagesOperation;
import com.adobe.pdfservices.operation.pdfops.options.PageRanges;
import com.adobe.pdfservices.operation.pdfops.options.createpdf.CreatePDFOptions;
import com.adobe.pdfservices.operation.pdfops.options.documentmerge.DocumentMergeOptions;
import com.adobe.pdfservices.operation.pdfops.options.documentmerge.OutputFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.docsigning.model.ExitForm;
import com.hr.docsigning.model.SendAgreementVO;
import com.hr.docsigning.util.Constants;
import com.hr.docsigning.util.RestApiAgreements;
import com.hr.docsigning.util.RestApiAgreements.DocumentIdentifierName;
import com.hr.docsigning.util.RestApiUtils;
import com.hr.docsigning.util.RestError;

/**
 * The Class FileStorageService.
 */
@Service
public class FileStorageService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

	/** The contract file path. */
	private static final String CONTRACT_FILE_PATH = "output/contract.pdf";

	private static final String DOC_GEN_ENDPOINT = "/operation/documentgeneration";

	/** The Constant mimeType. */
	private static final String MIME_TYPE = RestApiUtils.MimeType.PDF.toString();

	/**
	 * Sets the custom options and exit data.
	 *
	 * @param htmlToPDFOperation the html to PDF operation
	 * @param personForm         the person form
	 */
	private static void setCustomOptionsAndExitData(CreatePDFOperation htmlToPDFOperation, ExitForm personForm) {
		// Set the dataToMerge field that needs to be populated
		// in the HTML before its conversion
		final JSONObject dataToMerge = new JSONObject();
		dataToMerge.put(Constants.PERSON_FULL_NAME, personForm.getFullName());
		dataToMerge.put(Constants.FIRST_NAME, personForm.getFirstName());
		dataToMerge.put(Constants.LAST_NAME, personForm.getLastName());
		dataToMerge.put(Constants.DESIGNATION, personForm.getDesignation());
		dataToMerge.put(Constants.RESIGNATION_DATE, personForm.getResignationDate());
		dataToMerge.put(Constants.LAST_DATE, personForm.getLastDate());
		dataToMerge.put(Constants.HIRING_DATE, personForm.getHiringDate());
		dataToMerge.put(Constants.RESIGNATION_REASON, personForm.getResignationReason());

		// Set the desired HTML-to-PDF conversion options.
		final CreatePDFOptions htmlToPdfOptions = CreatePDFOptions.htmlOptionsBuilder().includeHeaderFooter(false)
				.withDataToMerge(dataToMerge).build();

		htmlToPDFOperation.setOptions(htmlToPdfOptions);
	}

	/**
	 * Sets the custom options for multiple merge.
	 *
	 * @param htmlToPDFOperation the new custom options for multiple merge
	 */
	private static void setCustomOptionsForMultipleMerge(CreatePDFOperation htmlToPDFOperation) {
		// Set the dataToMerge field that needs to be populated
		// in the HTML before its conversion
		final JSONObject dataToMerge = new JSONObject();
		dataToMerge.put(Constants.PERSON_FULL_NAME, Constants.HR_DEPARTMENT);

		// Set the desired HTML-to-PDF conversion options.
		final CreatePDFOptions htmlToPdfOptions = CreatePDFOptions.htmlOptionsBuilder().includeHeaderFooter(false)
				.withDataToMerge(dataToMerge).build();
		htmlToPDFOperation.setOptions(htmlToPdfOptions);
	}

	@Value("${DocGenAPI}")
	private String docGenAPI;

	/** The file path. */
	@Value(value = "${createContractPath}")
	private String createContractPath;

	/** The file path. */
	@Value(value = "${filePath}")
	private String filePath;

	String input_file = "./receiptTemplate.docx";

	String output_file = "./generatedReceipt.pdf";

	/** The integration key. */
	@Value(value = "${integration-key}")
	private String integrationKey;

	@Autowired
	AdobeSignService adobeSignService;

	/**
	 * Combine contract.
	 *
	 * @param fileInfos the file infos
	 */
	public void combineContract(List<File> fileInfos) {
		try {
			// Initial setup, create credentials instance.
			final ExecutionContext executionContext = this.getContext();
			final CombineFilesOperation combineFilesOperation = CombineFilesOperation.createNew();

			// Set operation input from a source file.
			for (int i = 0; i < fileInfos.size(); i++) {
				combineFilesOperation.addInput(FileRef.createFromLocalFile(fileInfos.get(i).getAbsolutePath()));
			}

			// Execute the operation.
			final FileRef result = combineFilesOperation.execute(executionContext);

			final CreatePDFOperation htmlToPDFOperation = CreatePDFOperation.createNew();

			// Set operation input from a source file.
			htmlToPDFOperation.setInput(result);

			// Provide any custom configuration options for the operation
			// You pass person data here to dynamically fill out the HTML
			setCustomOptionsForMultipleMerge(htmlToPDFOperation);

			// Save the result to the specified location. Delete previous file if exists
			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());

		} catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, ex);
		}
	}

	/**
	 * Creates the contract.
	 *
	 * @param exitForm the exit form
	 */
	public void createContract(ExitForm exitForm) {
		try {

			final ExecutionContext executionContext = this.getContext();
			final CreatePDFOperation htmlToPDFOperation = CreatePDFOperation.createNew();

			// Set operation input from a source file.
			final FileRef source = FileRef.createFromLocalFile(this.createContractPath);
			htmlToPDFOperation.setInput(source);

			// Provide any custom configuration options for the operation
			// You pass person data here to dynamically fill out the HTML
			setCustomOptionsAndExitData(htmlToPDFOperation, exitForm);

			// Execute the operation.
			final FileRef result = htmlToPDFOperation.execute(executionContext);

			// Save the result to the specified location. Delete previous file if exists
			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());

		} catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, ex);
		}
	}

	public String generateAndSendContract(SendAgreementVO sendAgreementVO) {

		String accessToken = null;
		String agreementId = null;
		try {
			FileRef result = generateFileFromTemplate();
			// Save the result to the specified location. Delete previous file if exists
			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());
			accessToken = Constants.BEARER + integrationKey;
			final org.json.simple.JSONObject uploadDocumentResponse = RestApiAgreements
					.postTransientDocument(accessToken, MIME_TYPE, file.getAbsolutePath(), file.getName());
			final String transientDocumentId = (String) uploadDocumentResponse
					.get(DocumentIdentifierName.TRANSIENT_DOCUMENT_ID.toString());

			// Send an agreement using the transient document ID derived from above.
			final DocumentIdentifierName idName = DocumentIdentifierName.TRANSIENT_DOCUMENT_ID;
			final ObjectMapper mapper = new ObjectMapper();
			final org.json.simple.JSONObject requestJson = mapper.convertValue(
					adobeSignService.getSendAgreementObj(sendAgreementVO), org.json.simple.JSONObject.class);

			final org.json.simple.JSONObject sendAgreementResponse = RestApiAgreements.sendAgreement(accessToken,
					requestJson, transientDocumentId, idName);

			// Parse and read response.
			LOGGER.info(Constants.AGREEMENT_SENT_INFO_MSG + sendAgreementResponse.get(Constants.ID));
			agreementId = (String) sendAgreementResponse.get(Constants.ID);
		} catch (final Exception e) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, e.getMessage());
		}
		return agreementId;
	}

	public void generateFile(MultipartFile fileToReorder) {
		try {

			// Execute the operation
			FileRef result = generateFileFromTemplate();

			// Save the result to the specified location. Delete previous file if exists
			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private FileRef generateFileFromTemplate() throws IOException, ServiceUsageException, ServiceApiException {
		final ExecutionContext executionContext = this.getContext();

		Files.deleteIfExists(Paths.get(output_file));

		Path jsonPath = Paths.get("./receipt.json");

		String json = new String(Files.readAllBytes(jsonPath));
		JSONObject jsonDataForMerge = new JSONObject(json);

		DocumentMergeOptions documentMergeOptions = new DocumentMergeOptions(jsonDataForMerge, OutputFormat.PDF);

		DocumentMergeOperation documentMergeOperation = DocumentMergeOperation.createNew(documentMergeOptions);

		// Provide an input FileRef for the operation
		FileRef source = FileRef.createFromLocalFile(input_file);
		documentMergeOperation.setInput(source);

		// Execute the operation
		return documentMergeOperation.execute(executionContext);

	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ExecutionContext getContext() throws IOException {
		final Credentials credentials = Credentials.serviceAccountCredentialsBuilder()
				.fromFile("pdftools-api-credentials.json").build();

		// Create an ExecutionContext using credentials
		// and create a new operation instance.
		return ExecutionContext.create(credentials);
	}

	/**
	 * Merge selected file.
	 *
	 * @param files the files
	 */
	public void mergeSelectedFile(MultipartFile[] files) {
		try {
			final ExecutionContext executionContext = this.getContext();
			final CombineFilesOperation combineFilesOperation = CombineFilesOperation.createNew();

			// Set operation input from a source file.
			for (final MultipartFile file : files) {
				combineFilesOperation.addInput(FileRef.createFromStream(file.getInputStream(),
						CombineFilesOperation.SupportedSourceFormat.PDF.getMediaType()));
			}

			// Execute the operation.
			final FileRef result = combineFilesOperation.execute(executionContext);

			// Save the result to the specified location. Delete previous file if exists
			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());

		} catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, ex);
		}
	}

	/**
	 * Rearrange file.
	 *
	 * @param fileToReorder the file to reorder
	 * @param startIndex    the start index
	 * @param lastIndex     the last index
	 * @param reorderIndex  the reorder index
	 */
	public void rearrangeFile(MultipartFile fileToReorder, String startIndex, String lastIndex, String reorderIndex) {
		try {
			final ExecutionContext executionContext = this.getContext();
			final ReorderPagesOperation reorderPagesOperation = ReorderPagesOperation.createNew();

			// Set operation input from a source file.
			reorderPagesOperation.setInput(FileRef.createFromStream(fileToReorder.getInputStream(),
					ReorderPagesOperation.SupportedSourceFormat.PDF.getMediaType()));
			final PageRanges pageRanges = new PageRanges();
			// Specify order of the pages for an output document.
			// Add pages 3 to 4.
			pageRanges.addRange(Integer.parseInt(startIndex), Integer.parseInt(lastIndex));

			// Add page 1.
			pageRanges.addSinglePage(Integer.parseInt(reorderIndex));
			reorderPagesOperation.setPagesOrder(pageRanges);

			// Execute the operation.
			final FileRef result = reorderPagesOperation.execute(executionContext);

			final File file = new File(CONTRACT_FILE_PATH);
			Files.deleteIfExists(file.toPath());

			result.saveAs(file.getPath());

		} catch (ServiceApiException | IOException | SdkException | ServiceUsageException ex) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, ex);
		}
	}

}
