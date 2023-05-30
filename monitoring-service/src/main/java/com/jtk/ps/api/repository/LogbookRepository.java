package com.jtk.ps.api.repository;

import com.jtk.ps.api.dto.logbook.LogbookResponse;
import com.jtk.ps.api.model.Logbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogbookRepository extends JpaRepository<Logbook, Integer> {
    Logbook findById(int id);
//    @Query(value = "select * from Logbook a join a.status s where participant_id = :participant_id", nativeQuery = true)
    List<Logbook> findByParticipantId(@Param("participant_id") int participantId);

    @Query(value = "select if(count(*)<0, 'true', 'false') from Logbook where participant_id = :participant_id and date = :date", nativeQuery = true)
    Boolean logbookExist(@Param("participant_id") int participantId,@Param("date")  LocalDate date);

    @Query(value = "select nif(count(*)<0, 'true', 'false') from Logbook where id = :id and grade is not null", nativeQuery = true)
    Boolean isChecked(@Param("id") int id);
}
