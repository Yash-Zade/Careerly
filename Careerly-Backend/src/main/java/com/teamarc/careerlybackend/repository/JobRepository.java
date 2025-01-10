package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j " +
            "WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.postedBy.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR EXISTS (SELECT skill FROM j.skillsRequired skill WHERE LOWER(skill) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(j.jobStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<Job> searchJobs(String keyword, PageRequest pageRequest, Pageable pageable);


//    Page<Job> findRecommendedJobs(@Param("applicant") Applicant applicant, @Param("skillMatchWeight") double skillMatchWeight, @Param("locationWeight") double locationWeight, Pageable pageable);

}