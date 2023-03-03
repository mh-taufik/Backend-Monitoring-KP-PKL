package com.jtk.ps.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Table(name = "evaluation_form")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EvaluationForm {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "prodi_id")
    @JsonProperty("prodi_id")
    private Integer prodiId;

    @Column(name = "num_evaluation")
    @JsonProperty("num_evaluation")
    private Integer numEvaluation;
}
