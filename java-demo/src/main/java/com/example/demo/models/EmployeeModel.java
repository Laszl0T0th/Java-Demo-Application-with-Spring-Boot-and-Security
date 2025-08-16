package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Data
@Table(name = "EMPLOYEE")
public class EmployeeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int ID;
	
	@NotEmpty(message = "The First Name is required")
	@Column(name = "FirstName")
	private String FirstName;

	@NotEmpty(message = "The Last Name is required")
	@Column(name = "LastName")
	private String LastName;

	@NotEmpty(message = "The Address is required")
	@Column(name = "Address")
	private String Address;

    @Column(name = "image_url", length = 255)
    private String imageUrl;


	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

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
