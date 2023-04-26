package com.hr.docsigning.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hr.docsigning.model.ExitForm;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class FileStorageServiceTests {

	@Autowired
	FileStorageService fileStorageService;

	MockMultipartFile testFile1 = new MockMultipartFile("data1", "filename.pdf", "application/pdf",
			"test1 file".getBytes());
	MockMultipartFile testFile2 = new MockMultipartFile("data2", "filename.pdf", "application/pdf",
			"test2 file".getBytes());

	@Test
	public void testcombineContract() {
		boolean flag = false;
		try {
			final List<File> fileInfos = new ArrayList<>();
			final File file1 = new File("output/contract.pdf");
			final File file2 = new File("output/contract.pdf");
			fileInfos.add(file2);
			fileInfos.add(file1);
			this.fileStorageService.combineContract(fileInfos);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}
		assertEquals(true, flag);
	}

	@Test
	public void testcreateContract() {
		boolean flag = false;
		try {
			final ExitForm exitForm = new ExitForm();
			exitForm.setFirstName("Abhishek");
			exitForm.setLastName("Dixit");
			exitForm.setDesignation("Architect");
			exitForm.setHiringDate("04-10-2021");
			exitForm.setLastDate("04-10-2022");
			exitForm.setResignationDate("04-06-2022");
			this.fileStorageService.createContract(exitForm);
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}
		assertEquals(true, flag);
	}

	@Test
	public void testmergeSelectedFile() {
		boolean flag = false;
		try {

			this.fileStorageService.mergeSelectedFile(new MockMultipartFile[] { this.testFile1, this.testFile2 });
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}
		assertEquals(true, flag);
	}

	@Test
	public void testrearrangeFile() {
		boolean flag = false;
		try {

			this.fileStorageService.rearrangeFile(this.testFile1, "1", "2", "1");
			flag = true;
		} catch (final Exception e) {
			flag = false;
		}
		assertEquals(true, flag);
	}

}
