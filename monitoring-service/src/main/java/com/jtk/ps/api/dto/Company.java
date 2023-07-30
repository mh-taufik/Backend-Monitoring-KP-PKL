package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Company{
	@JsonProperty("quota")
	private int quota;
	@JsonProperty("name")
	private String name;
	@JsonProperty("id")
	private int id;

	public int getQuota(){
		return quota;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}
}
