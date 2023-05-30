package com.jtk.ps.api.repository;

import com.jtk.ps.api.model.Laporan;
import com.jtk.ps.api.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer>{
    Status findById(int id);
}
