package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.SupervisorGrade;
import com.jtk.ps.api.model.SupervisorMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisorMappingRepository extends JpaRepository<SupervisorMapping, Integer> {
    SupervisorMapping findById(int id);
    SupervisorMapping findByParticipantId(int participantId);
    List<SupervisorMapping> findByLecturerId(int lecturerId);
    List<SupervisorMapping> findByProdiId(int prodiId);
}
