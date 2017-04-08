package com.seawaf.config;

public class User {
	public static int ROLE_ADMIN = 1;
	public static int ROLE_AUDIT = 2;

	private String id;
	private String name;
	private String email;
	private String token;// otp token
	private String openid;
	private int role;

	public User(){
		this.role=ROLE_ADMIN;
	}
	
	public User(String id,String name,String email,int role){
		this.id=id;
		this.name=name;
		this.email=email;
		this.role=role;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
	
	
}
