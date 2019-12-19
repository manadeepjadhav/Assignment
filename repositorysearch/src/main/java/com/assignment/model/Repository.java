package com.assignment.model;

public class Repository {

	public Repository() {

	}

	public Repository(String id, String name, String owner, String httpsUrl) {
		super();
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.httpsUrl = httpsUrl;
	}

	private String id;
	private String name;
	private String owner;
	private String httpsUrl;
	private String sshUrl;
    private String provider;
	
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getHttpsUrl() {
		return httpsUrl;
	}

	public void setHttpsUrl(String httpsUrl) {
		this.httpsUrl = httpsUrl;
	}

	public String getSshUrl() {
		return sshUrl;
	}

	public void setSshUrl(String sshUrl) {
		this.sshUrl = sshUrl;
	}
	
	
}
