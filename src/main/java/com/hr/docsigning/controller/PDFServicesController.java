package com.hr.docsigning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hr.docsigning.service.FileStorageService;
import com.hr.docsigning.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "PDF Services API")
public class PDFServicesController {

	/** The file storage service. */
	@Autowired
	FileStorageService fileStorageService;

	@Operation(summary = "Merge files", description = "Returns combined PDF file")
	@PostMapping(Constants.MERGE_DOCUMENT_ENDPOINT)
	public String combineContracts(
			@RequestParam(Constants.PARAM_FILES) @Parameter(name = "files", description = "Files to combine", example = "1") MultipartFile[] files) {
		this.fileStorageService.mergeSelectedFile(files);

		return Constants.COMMON_RETURN_PAGE;
	}
}
