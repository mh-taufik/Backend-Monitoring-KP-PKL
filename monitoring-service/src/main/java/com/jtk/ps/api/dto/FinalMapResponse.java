package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FinalMapResponse{
	@JsonProperty("final_date")
	private String finalDate;
	@JsonProperty("final_mapping")
	private List<FinalMappingItem> finalMapping;
	@JsonProperty("is_final")
	private int isFinal;
	@JsonProperty("publish_date")
	private String publishDate;
	@JsonProperty("is_publish")
	private int isPublish;

	public String getFinalDate(){
		return finalDate;
	}

	public List<FinalMappingItem> getFinalMapping(){
		return finalMapping;
	}

	public int getIsFinal(){
		return isFinal;
	}

	public String getPublishDate(){
		return publishDate;
	}

	public int getIsPublish(){
		return isPublish;
	}
}