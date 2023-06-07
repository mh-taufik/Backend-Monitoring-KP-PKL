package com.jtk.ps.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiburNasionalResponse {
	@JsonProperty("holiday_date")
	private LocalDate holidayDate;
	@JsonProperty("holiday_name")
	private String holidayName;
	@JsonProperty("is_national_holiday")
	private boolean isNationalHoliday;
}
