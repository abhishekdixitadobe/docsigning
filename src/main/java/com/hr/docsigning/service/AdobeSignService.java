package com.hr.docsigning.service;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hr.docsigning.model.MemberInfo;
import com.hr.docsigning.model.ParticipantSet;
import com.hr.docsigning.model.SendAgreementVO;
import com.hr.docsigning.model.SendVO;
import com.hr.docsigning.util.Constants;
import com.hr.docsigning.util.RestApiAgreements;
import com.hr.docsigning.util.RestApiAgreements.DocumentIdentifierName;
import com.hr.docsigning.util.RestApiUtils;
import com.hr.docsigning.util.RestError;

/**
 * The Class AdobeSignService.
 */
@Service
public class AdobeSignService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AdobeSignService.class);

	/** The Constant mimeType. */
	private static final String MIME_TYPE = RestApiUtils.MimeType.PDF.toString();

	/** The integration key. */
	@Value(value = "${integration-key}")
	private String integrationKey;

	/**
	 * Gets the integration key.
	 *
	 * @return the integration key
	 */
	public String getIntegrationKey() {
		return this.integrationKey;
	}

	/**
	 * Gets the send agreement obj.
	 *
	 * @param sendAgreementVO the send agreement VO
	 * @return the send agreement obj
	 */
	public SendVO getSendAgreementObj(SendAgreementVO sendAgreementVO) {
		final SendVO sendObj = new SendVO();

		final List<ParticipantSet> participantList = new ArrayList<>();

		final List<MemberInfo> sendermemberList = new ArrayList<>();
		int count = 1;
		if (null != sendAgreementVO.getApproverEmail()) {
			final List<MemberInfo> approvermemberList = new ArrayList<>();
			final ParticipantSet approverSet = new ParticipantSet();
			final MemberInfo approverInfo = new MemberInfo();
			approverInfo.setEmail(sendAgreementVO.getApproverEmail());
			approvermemberList.add(approverInfo);
			approverSet.setRole(Constants.signRole.APPROVER.toString());
			approverSet.setOrder(String.valueOf(count));
			approverSet.setMemberInfos(approvermemberList);
			participantList.add(approverSet);
			count++;
		}
		final MemberInfo senderInfo = new MemberInfo();
		senderInfo.setEmail(sendAgreementVO.getSignerEmail());
		sendermemberList.add(senderInfo);

		final ParticipantSet senderSet = new ParticipantSet();
		senderSet.setMemberInfos(sendermemberList);

		senderSet.setRole(Constants.signRole.SIGNER.toString());
		senderSet.setOrder(String.valueOf(count));
		participantList.add(senderSet);

		sendObj.setMessage(sendAgreementVO.getMessage());
		sendObj.setName(sendAgreementVO.getName());
		sendObj.setParticipantSetsInfo(participantList);

		return sendObj;
	}

	/**
	 * Send contract.
	 *
	 * @param sendAgreementVO the send agreement VO
	 * @param file1           the file 1
	 * @return the string
	 */
	public String sendContract(SendAgreementVO sendAgreementVO, MultipartFile file1) {
		final String filePathStr = "output/";
		final String fileName = file1.getOriginalFilename();

		String accessToken = null;
		String agreementId = null;
		try {
			final Path filepath = Paths.get(filePathStr, fileName);
			try (OutputStream os = Files.newOutputStream(filepath)) {
				os.write(file1.getBytes());
			}
			final File file = new File(filePathStr + fileName);
			accessToken = Constants.BEARER + this.getIntegrationKey();
			final JSONObject uploadDocumentResponse = RestApiAgreements.postTransientDocument(accessToken, MIME_TYPE,
					file.getAbsolutePath(), fileName);
			final String transientDocumentId = (String) uploadDocumentResponse
					.get(DocumentIdentifierName.TRANSIENT_DOCUMENT_ID.toString());

			// Send an agreement using the transient document ID derived from above.
			final DocumentIdentifierName idName = DocumentIdentifierName.TRANSIENT_DOCUMENT_ID;
			final ObjectMapper mapper = new ObjectMapper();
			final JSONObject requestJson = mapper.convertValue(this.getSendAgreementObj(sendAgreementVO),
					JSONObject.class);

			final JSONObject sendAgreementResponse = RestApiAgreements.sendAgreement(accessToken, requestJson,
					transientDocumentId, idName);

			// Parse and read response.
			LOGGER.info(Constants.AGREEMENT_SENT_INFO_MSG + sendAgreementResponse.get(Constants.ID));
			agreementId = (String) sendAgreementResponse.get(Constants.ID);
		} catch (final Exception e) {
			LOGGER.error(RestError.OPERATION_EXECUTION_ERROR.errMessage, e.getMessage());
		}
		return agreementId;
	}

	/**
	 * Sets the integration key.
	 *
	 * @param integrationKey the new integration key
	 */
	public void setIntegrationKey(String integrationKey) {
		this.integrationKey = integrationKey;
	}
}
