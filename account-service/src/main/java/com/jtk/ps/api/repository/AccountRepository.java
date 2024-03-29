package com.jtk.ps.api.repository;

import com.jtk.ps.api.dto.AccountResponse;
import com.jtk.ps.api.dto.CommitteeResponse;

import com.jtk.ps.api.model.Account;
import com.jtk.ps.api.model.EProdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findById(int id);

    List<Account> findByIdIn(@Param("id") List<Integer> id);

    @Query("SELECT new com.jtk.ps.api.dto.AccountResponse(b.id, a.name, b.username, b.role, a.prodi) FROM Account b INNER JOIN b.lecturer a WHERE a.prodi = :prodi AND b.role NOT IN (0,3)")
    List<AccountResponse> getAllAccountForCommittee(@Param("prodi")EProdi prodi);

    @Query("SELECT new com.jtk.ps.api.dto.AccountResponse(b.id, a.name, b.username, b.role, a.prodi) FROM Account b INNER JOIN b.lecturer a WHERE a.prodi = :prodi")
    List<AccountResponse> getAllAccountForHeadStudyProgram(@Param("prodi")EProdi prodi);

    @Query("SELECT new com.jtk.ps.api.dto.AccountResponse(b.id, a.name, b.username, b.role, a.prodi) FROM Account b INNER JOIN b.lecturer a WHERE a.prodi = :prodi AND b.role = 4")
    List<AccountResponse> getAllAccountForSupervisor(@Param("prodi")EProdi prodi);

    @Query("SELECT new com.jtk.ps.api.dto.CommitteeResponse(a.id, a.name, a.email) FROM Account b INNER JOIN b.lecturer a WHERE b.role = 'COMMITTEE'")
    List<CommitteeResponse> fetchCommitteeResponseDataInnerJoin();

    @Query("SELECT new com.jtk.ps.api.dto.CommitteeResponse(a.id, a.name, a.email) FROM Account b INNER JOIN b.lecturer a WHERE b.role = 'COMMITTEE' AND a.id = :id")
    CommitteeResponse fetchCommitteeResponseDataInnerJoin(@Param("id") int id);

    @Query("SELECT new com.jtk.ps.api.dto.CommitteeResponse(b.id, a.name, a.email) FROM Account b INNER JOIN b.lecturer a WHERE b.role = 4")
    List<CommitteeResponse> fetchSupervisorResponseDataInnerJoin();

    @Query("SELECT new com.jtk.ps.api.dto.CommitteeResponse(b.id, a.name, a.email) FROM Account b INNER JOIN b.lecturer a WHERE b.role = 4 AND a.id = :id")
    CommitteeResponse fetchSupervisorResponseDataInnerJoin(@Param("id") int id);
}
