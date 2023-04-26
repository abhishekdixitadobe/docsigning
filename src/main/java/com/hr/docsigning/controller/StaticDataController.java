package com.hr.docsigning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hr.docsigning.util.Constants;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class StaticDataController {

	@RequestMapping(Constants.ABOUTUS_DETAILS_ENDPOINT)
	public String defectDetails() {
		return Constants.ABOUT_US_HTML;
	}
}
