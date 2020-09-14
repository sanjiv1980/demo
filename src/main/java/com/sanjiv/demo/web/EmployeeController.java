package com.sanjiv.demo.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sanjiv.demo.exception.RecordNotFoundException;
import com.sanjiv.demo.model.EmployeeEntity;
import com.sanjiv.demo.service.EmployeeService;
 
@RestController
@RequestMapping("/employees")
public class EmployeeController
{
    @Autowired
    EmployeeService service;
 
    @GetMapping
    public ModelAndView getAllEmployees() {
        List<EmployeeEntity> list = service.getAllEmployees();
        ModelAndView mav = new ModelAndView("list-employees");
        mav.addObject("employees", list); 
        return mav;
 
       // return new ResponseEntity<List<EmployeeEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeById(@PathVariable("id") Long id) throws RecordNotFoundException {
        EmployeeEntity entity = service.getEmployeeById(id);
        return new ResponseEntity<EmployeeEntity>(entity, new HttpHeaders(), HttpStatus.OK);
    }
    
    @GetMapping("/edit/{id}")
    public ModelAndView updateEmployee(@PathVariable("id") Long id) {
    	
    	ModelAndView mav = new ModelAndView("add-edit-employee");
    	
    	if(!id.equals(null)) {
    		EmployeeEntity updated;
			try {
				updated = service.getEmployeeById(id);
				mav.addObject("employee", updated);
			} catch (RecordNotFoundException e) {
				
				e.printStackTrace();
			}
    	}
    	return mav;
    }
    
    @GetMapping("/add")
    public ModelAndView addEmployee() {
    	
    	ModelAndView mav = new ModelAndView("add-edit-employee");
    	EmployeeEntity employee = new EmployeeEntity();
    	mav.addObject("employee", employee);
    	return mav;
    }
 
    @PostMapping("createEmployee")
    public RedirectView createOrUpdateEmployee(@ModelAttribute EmployeeEntity employee) throws RecordNotFoundException {
        
    	EmployeeEntity updated = service.createOrUpdateEmployee(employee);
    	return new RedirectView("");
        //return new ResponseEntity<EmployeeEntity>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/delete/{id}")
    public void deleteEmployeeById(@PathVariable("id") Long id,HttpServletResponse response) throws RecordNotFoundException {
        service.deleteEmployeeById(id);
        try {
			response.sendRedirect("/demo/employees");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         //return "forward:/employees"; 
        //return HttpStatus.FORBIDDEN;
    }
 
}