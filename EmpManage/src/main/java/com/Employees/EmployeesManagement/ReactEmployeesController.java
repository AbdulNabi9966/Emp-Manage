package com.Employees.EmployeesManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ReactEmployeesController {

	@Autowired
	DatabaseConnection db;
	
	@GetMapping("/reactemployees")
	 public  List<Employee> getJsonEmployees() {
		 List<Employee> listemp=db.getEmployees();
	   
	    return listemp;
	  }
	 
	 @PostMapping("/reactemployees")
	 public String createEmployee(@RequestBody Employee emp) {
	 		 String message ;
	 		
	 		if(db.insertEmployee(emp)>=1)
	 			message ="Employee Added";
	 		else
	 			message= "Employee Failed";
	 		
	 		return message;
	 		
	 	 }

	 @GetMapping("/reactdeleteemployee/{email}")
	 public int deleteEmployee(@PathVariable String email) {


	 		return db.deleteEmployee(email);


	 	 }

	 @PostMapping("/reactupdateemployee")
	 public String updateEmployee(@RequestBody Employee emp) {
		 String message ;
	 		System.out.println(emp.toString());
	 		if( db.updateEmployee(emp)>=1)
	 		message ="Employee Added";
	 		else
	 			message= "Employee Failed";

	 		return message;

	 	 }
	 
	 
	 @PostMapping(value="/reactuploadfile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	 public String fileUpload(@RequestParam int employee_id,@RequestPart MultipartFile file_content) throws IOException {
		System.out.println("employeeid:"+employee_id);
		 System.out.println("filename:"+file_content.getOriginalFilename());
		 Employee_CV cv =new Employee_CV();	
		 cv.setEmployee_id(employee_id);
		cv.setFileName(file_content.getOriginalFilename());
		cv.setContent_type(file_content.getContentType());
		cv.setFile_size(file_content.getSize());
		cv.setFile_content(file_content);
	 db.insertEmployeeCV(cv);
	 
	
	  return "success";
	  
	 }
	 
	 @GetMapping("/reactdownload/{employeeid}")
	 public void downloadfile(@PathVariable int employeeid, HttpServletResponse response) throws IOException {

		 Employee_CV cv=db.getCV(employeeid);
		   response.setContentType("application/octet-stream");
		   String headerKey = "Content-Disposition";
		   String headerValue = "attachment; filename = "+cv.getFileName();
		   response.setHeader(headerKey, headerValue);
		   ServletOutputStream outputStream = response.getOutputStream();
		   outputStream.write(cv.getContent());
		   outputStream.close();

	 }
}
