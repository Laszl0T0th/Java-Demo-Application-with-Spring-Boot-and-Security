package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.models.EmployeeModel;
import com.example.demo.models.EmployeeModelCreate;
import com.example.demo.service.EmployeeService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
    private EmployeeService employeeService;

    
    @GetMapping({"", "/"})
    public CompletableFuture<String> getEmployee(Model model) {
        System.out.println("getEmployee call");
        return employeeService.getEmployeesAsync()
            .thenApply(emps -> {
                model.addAttribute("emps", emps); 
                return "employees/index"; 
            });
    }
   
    @GetMapping("/create")
    public CompletableFuture<String> showCreateEmployeeForm(Model model) {
        System.out.println("showCreateEmployeeForm call");
        return CompletableFuture.supplyAsync(() -> {
            model.addAttribute("employee", new EmployeeModelCreate());
            return "employees/create";
        });
    }
    
    @PostMapping("/create")
    public CompletableFuture<String> createEmployee(@RequestParam(value = "image", required = false) MultipartFile file, @Valid @ModelAttribute("employee") EmployeeModelCreate employeeModelCreate, BindingResult bindingResult) {
        System.out.println("createEmployee call");
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture("employees/create"); 
        }
        
        if (file != null && !file.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
                logger.info("------ Upload Directory: " + uploadDir);

                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String newFilename = uuid + "_" + originalFilename; 

                logger.info("------ New Filename: " + newFilename);

                String imagePath = uploadDir + newFilename;
                File destinationFile = new File(imagePath);
                file.transferTo(destinationFile);
                employeeModelCreate.setImageUrl("/images/" + newFilename); 

            } catch (IOException e) {
                e.printStackTrace();
                logger.info("------ createProduct: error " + e.getMessage());
                return CompletableFuture.completedFuture("employee/create"); 
            }
        }
        
        return employeeService.createEmployeeAsync(employeeModelCreate)
            .thenApply(message -> {
                return "redirect:/employee"; 
            })
            .exceptionally(e -> {
                e.printStackTrace();
                return "employee/create"; 
            });
    }

    @GetMapping("/edit")
    public CompletableFuture<String> editEmployeeForm(@RequestParam int id, Model model) {
        System.out.println("editEmployeeForm call");
        return employeeService.getEmployeeByIdAsync(id) 
            .thenApply(emp -> {
                if (emp == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
                }
                model.addAttribute("employee", emp);
                return "employees/edit"; 
            });
    }    
    
    @PostMapping("/edit")
    public CompletableFuture<String> updateEmployee(@RequestParam(value = "image", required = false) MultipartFile file,
    												@Valid @ModelAttribute("employee") EmployeeModel employeeModel, 
    												BindingResult bindingResult) {
    	
        System.out.println("updateEmployee post call");
        
        System.out.println("Controller Employee ID: " + employeeModel.getID());
        System.out.println("Controller First Name: " + employeeModel.getFirstName());
        System.out.println("Controller Last Name: " + employeeModel.getLastName());
        System.out.println("Controller Address: " + employeeModel.getAddress());
        System.out.println("Controller ImageUrl: " + employeeModel.getImageUrl());
        
        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture("employees/edit"); 
        }
        
        return employeeService.getEmployeeByIdAsync(employeeModel.getID())
                .thenCompose(oldEmployee -> {
                    String oldImageUrl = oldEmployee.getImageUrl(); 
                    logger.info("----- oldImageUrl: " + oldImageUrl);
                    if (file != null && !file.isEmpty()) {
                        try {
                            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";

                            String originalFilename = file.getOriginalFilename();
                            String uuid = UUID.randomUUID().toString();
                            String newFilename = uuid + "_" + originalFilename; 

                            String imagePath = uploadDir + newFilename; 
                            File destinationFile = new File(imagePath);
                            file.transferTo(destinationFile); 
                            employeeModel.setImageUrl("/images/" + newFilename); 

                            if (oldImageUrl != null && !oldImageUrl.isEmpty() && !oldImageUrl.equals(employeeModel.getImageUrl())) {
                                System.out.println("Upload Image");
                                String oldImagePath = System.getProperty("user.dir") + "/uploads/images/" + oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                                logger.info("----- Path to the image you want to delete: " + oldImagePath);
                                File oldImageFile = new File(oldImagePath);
                                if (oldImageFile.exists()) {
                                    oldImageFile.delete(); 
                                    logger.info("----- Old image deleted: " + oldImagePath);
                                } else {
                                	logger.info("----- The image you want to delete cannot be found: " + oldImagePath);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return CompletableFuture.completedFuture("employees/edit"); 
                        }
                    } 
                    else {
                    	employeeModel.setImageUrl(oldImageUrl);
                        logger.info("----- file is null or Empty");
    	            }
    	           
                    return employeeService.updateEmployeeAsync(employeeModel)
                        .thenApply(message -> {
                            return "redirect:/employee";
                        })
                        .exceptionally(e -> {
                            e.printStackTrace();
                            return "employee/edit";
                        });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return "employee/edit"; 
                });
    }


    @GetMapping("/delete")
    public CompletableFuture<String> deleteEmployee(@RequestParam int id) {
        System.out.println("deleteEmployee call");
        
        return employeeService.deleteEmployeeAsync(id)
            .thenApply(v -> {
                return "redirect:/employee"; 
            })
            .exceptionally(e -> {
                e.printStackTrace();
                return "redirect:/employee"; 
            });
    }

    
}
