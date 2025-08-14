package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
