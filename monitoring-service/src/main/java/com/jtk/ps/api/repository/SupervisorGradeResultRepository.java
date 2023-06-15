package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGradeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupervisorGradeResultRepository extends JpaRepository<SupervisorGradeResult, Integer> {
    List<SupervisorGradeResult> findBySupervisorGradeId(int id);
//    List<SupervisorGradeResult> findBySupervisorGradeResult(SupervisorGradeResult supervisorGradeResult);
    @Query(value = "select sum(grade) from (select if(sum(sgr.grade)>0, true, false) as grade from supervisor_grade_result sgr join supervisor_grade sg on sg.id = sgr.supervisor_grade_id where sg.participant_id = :participant group by sgr.supervisor_grade_id) as a", nativeQuery = true)
    Integer countTotalGraded(@Param("participant") int participant);
}
