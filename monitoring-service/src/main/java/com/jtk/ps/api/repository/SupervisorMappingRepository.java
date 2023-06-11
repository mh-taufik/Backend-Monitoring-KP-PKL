package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupervisorMappingRepository extends JpaRepository<SupervisorMapping, Integer> {
    SupervisorMapping findById(int id);
    SupervisorMapping findByParticipantId(int participantId);
    List<SupervisorMapping> findByLecturerId(int lecturerId);
    List<SupervisorMapping> findByProdiId(int prodiId);
    @Query(value = "select * from supervisor_mapping where prodi_id = :prodi group by company_id",nativeQuery = true)
    List<SupervisorMapping> findByProdiGroupByCompanyId(@Param("prodi") int prodi);
    @Query(value = "select * from supervisor_mapping where lecturer_id = :lecturer group by company_id",nativeQuery = true)
    List<SupervisorMapping> findByLecturerIdGroupByCompanyId(@Param("lecturer") int lecturerId);
    List<SupervisorMapping> findByCompanyId(int companyId);
    @Query(value = "select * from supervisor_mapping where year(date) = :year",nativeQuery = true)
    List<SupervisorMapping> findByYear(int year);
    @Query(value = "select count(*) from supervisor_mapping where year(date) = :year and prodi_id = :prodi",nativeQuery = true)
    Integer countByYear(int year, int prodi);
    @Query(value = "select lecturer_id from supervisor_mapping where participant_id = :participant",nativeQuery = true)
    Optional<Integer> findLecturerId(int participant);
}
