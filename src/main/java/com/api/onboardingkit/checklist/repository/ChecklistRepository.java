package com.api.onboardingkit.checklist.repository;

import com.api.onboardingkit.checklist.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByUserNo(Long userNo);
    Optional<Checklist> findTopByUserNoOrderByCreatedTimeDesc(Long userNo);
}