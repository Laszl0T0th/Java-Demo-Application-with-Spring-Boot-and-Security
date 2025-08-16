package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@IdClass(UserRoleId.class)
@Table(name = "user_roles")
public class UserRole {

    
    @Id
    @Column(name = "user_id") 
    private Long userId;

    @Id
    @Column(name = "role_id") 
    private Integer roleId;


    public UserRole() {}

	public UserRole(Long userId, Integer roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


}
