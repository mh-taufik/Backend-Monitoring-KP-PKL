package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGrade;
import com.jtk.ps.api.model.SupervisorMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupervisorMappingRepository extends JpaRepository<SupervisorMapping, Integer> {
    SupervisorMapping findById(int id);
    SupervisorMapping findByParticipantId(int participantId);
    List<SupervisorMapping> findByLecturerId(int lecturerId);
    List<SupervisorMapping> findByProdiId(int prodiId);
    @Query(value = "select * from supervisor_mapping where prodi_id = :prodi group by company_id",nativeQuery = true)
    List<SupervisorMapping> findAllGroupByCompanyId(@Param("prodi") int prodi);
    @Query(value = "select * from supervisor_mapping where lecturer_id = :lecturer group by company_id",nativeQuery = true)
    List<SupervisorMapping> findByLecturerIdGroupByCompanyId(@Param("lecturer") int lecturerId);
    List<SupervisorMapping> findByCompanyId(int companyId);
    @Query(value = "update supervisor_mapping set lecturer_id = :lecturer, create_by = :creator where company_id = :company",nativeQuery = true)
    List<SupervisorMapping> updateByCompanyId(@Param("lecturer") int lecturerId, @Param("company") int companyId, @Param("creator") int creatorId);
}
