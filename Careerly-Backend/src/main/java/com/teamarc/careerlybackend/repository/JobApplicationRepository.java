package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

@Query("SELECT j FROM JobApplication j " +
        "WHERE LOWER(j.job.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(j.job.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(j.job.postedBy.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(j.job.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(CAST(j.applicationStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))"+
        "OR LOWER(j.applicant.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))"
)
Page<JobApplication> searchApplications(@Param("keyword") String keyword, PageRequest pageRequest,
                                        Pageable pageable);
}
