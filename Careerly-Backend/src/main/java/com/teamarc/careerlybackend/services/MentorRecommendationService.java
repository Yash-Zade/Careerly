package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.MentorDTO;
import com.teamarc.careerlybackend.dto.RecommendationRequestDTO;
import com.teamarc.careerlybackend.entity.Applicant;
import com.teamarc.careerlybackend.entity.Job;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorRecommendationService {

    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    private static final double SKILL_MATCH_WEIGHT = 0.5;
    private static final double RATING_WEIGHT = 0.3;
    private static final double RELEVANCE_WEIGHT = 0.2;
    private static final double THRESHOLD = 0.7;

    public Page<MentorDTO> recommendMentors(RecommendationRequestDTO recommendationRequest, PageRequest pageRequest) {
        List<Mentor> allMentors = mentorRepository.findAll();
        List<MentorDTO> recommendedMentor = allMentors.stream()
                .map(mentor -> {
                    double score = calculateFinalScore(recommendationRequest.getApplicant(), recommendationRequest.getJob(), mentor);
                    return score >= THRESHOLD ? modelMapper.map(mentor, MentorDTO.class) : null;
                })
                .filter(mentorDTO -> mentorDTO != null)
                .collect(Collectors.toList());
        return new PageImpl<>(recommendedMentor, pageRequest, recommendedMentor.size());
    }

    private double calculateFinalScore(Applicant applicant, Job job, Mentor mentor) {
        double skillMatch = calculateSkillMatch(job.getSkillsRequired(), mentor.getExpertise());
        double rating = mentor.getAverageRating();
        double relevance = calculateRelevance(applicant, mentor);
        return skillMatch * SKILL_MATCH_WEIGHT + rating * RATING_WEIGHT + relevance * RELEVANCE_WEIGHT;
    }

    private double calculateSkillMatch(List<String> jobSkills, List<String> mentorExpertise) {
        if (jobSkills.isEmpty()) return 0;
        long matchCount = jobSkills.stream().filter(mentorExpertise::contains).count();
        return (double) matchCount / jobSkills.size();
    }

    private double calculateRelevance(Applicant applicant, Mentor mentor) {
        if (applicant.getSkills().isEmpty()) return 0;
        long matchCount = applicant.getSkills().stream().filter(mentor.getExpertise()::contains).count();
        return (double) matchCount / applicant.getSkills().size();
    }
}
