package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class EmployeeModelCreate {

	@NotEmpty(message = "The First Name is required")
	private String FirstName;

	@NotEmpty(message = "The Last Name is required")
	private String LastName;

	@NotEmpty(message = "The Address is required")
	private String Address;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    
	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
