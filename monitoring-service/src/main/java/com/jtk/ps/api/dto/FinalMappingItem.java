package com.jtk.ps.api.dto;

import com.jtk.ps.api.dto.supervisor_mapping.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalMappingItem{
	private Company company;
	private List<Participant> participant;

	public Company getCompany(){
		return company;
	}

	public List<Participant> getParticipant(){
		return participant;
	}
}