package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PersonService {
	private static final String JSON_FOLDER = "src/main/resources/json/";
	private static final String EXCEL_FILE_PATH = "src/main/resources/output/person_data.xlsx";
	
	public void generateExcelFromJSON() throws Exception {
		System.out.println("Under service");
		ObjectMapper objectMapper = new ObjectMapper();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Person Data");
		
		// header row
		String[] headers = {"Id", "Name", "Age", "Gender", "Company", "Address"};
		Row headerRow = sheet.createRow(0);
		for(int i=0; i<headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}
		
		// Reading all JSON and converting them to person objects
		File folder = new File(JSON_FOLDER);
		File[] jsonFiles = folder.listFiles();
		List<Person> personList = new ArrayList<>();
		
		if(jsonFiles != null) {
			for(File file: jsonFiles) {
				FileInputStream fis = new FileInputStream(file);
				Person person = objectMapper.readValue(fis, Person.class);
				personList.add(person);
				fis.close();
			}
		}
		
		// Writing the person data to Excel
		int rowCount = 1;
		for(Person person: personList) {
			Row row = sheet.createRow(rowCount++);
			row.createCell(0).setCellValue(person.getId());
			row.createCell(1).setCellValue(person.getName());
			row.createCell(2).setCellValue(person.getAge());
			row.createCell(3).setCellValue(person.getGender());
			row.createCell(4).setCellValue(person.getCompany());
			row.createCell(5).setCellValue(person.getAddress());
		}
		
		// Writing the workbook to a file
		try(FileOutputStream fos = new FileOutputStream(EXCEL_FILE_PATH)) {
			workbook.write(fos);
		}
		
		workbook.close();
	}
}
