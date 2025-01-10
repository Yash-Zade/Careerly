package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.JobApplication;
import com.teamarc.careerlybackend.entity.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    @Query("SELECT j FROM JobApplication j " +
            "WHERE LOWER(j.job.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.job.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.job.postedBy.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.job.location) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(j.applicationStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "OR LOWER(j.applicant.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<JobApplication> searchApplications(@Param("keyword") String keyword, PageRequest pageRequest,
                                            Pageable pageable);


    Page<JobApplication> findByApplicant(Applicant currentApplicant, PageRequest pageRequest, Pageable pageable);

    Page<JobApplication> findByJob_JobId(Long jobId, PageRequest pageRequest, Pageable pageable);

    @Query("SELECT j FROM JobApplication j " +
            "WHERE LOWER(j.applicant.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.applicant.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(j.appliedDate AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(j.applicant.applicantId AS string))LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CAST(j.applicationStatus AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    Page<Applicant> searchApplicants(String keyword, PageRequest pageRequest, Pageable pageable);


    Optional<JobApplication> findByJob_JobIdAndApplicant_ApplicantId(Long jobId, Long applicantId);

    @Query("SELECT j FROM JobApplication j " +
            "WHERE j.job.jobId = :jobId " +
            "AND j.applicationStatus = :applicationStatus"
    )
    Page<JobApplication> findByApplicationStatus(Long jobId, ApplicationStatus applicationStatus, PageRequest pageRequest, Pageable pageable);
}
