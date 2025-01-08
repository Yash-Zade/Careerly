package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.MentorProfileDTO;
import com.teamarc.careerlybackend.dto.RatingDTO;
import com.teamarc.careerlybackend.entity.Mentor;
import com.teamarc.careerlybackend.entity.Rating;
import com.teamarc.careerlybackend.entity.Session;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.exceptions.RuntimeConflictException;
import com.teamarc.careerlybackend.repository.MentorRepository;
import com.teamarc.careerlybackend.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;
    private final EmailSenderService emailSenderService;


    public MentorProfileDTO rateMentor(RatingDTO ratingDTO) {
        Mentor mentor = ratingDTO.getSession().getMentor();
        Rating ratingObj = ratingRepository.findBySession(ratingDTO.getSession())
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found for session with id: " + ratingDTO.getSession().getSessionId()));
        if (ratingObj.getRatingValue() != null) throw new RuntimeConflictException("Mentor is already rated");
        ratingObj.setRatingValue(ratingDTO.getRatingValue());
        ratingObj.setComment(ratingDTO.getComment());
        ratingRepository.save(ratingObj);

        Double newRating = ratingRepository.findByMentor(mentor)
                .stream()
                .mapToDouble(Rating::getRatingValue)
                .average().orElse(0.0);
        mentor.setAverageRating(newRating);
        mentor.getRatings().add(ratingObj);
        Mentor savedMentor = mentorRepository.save(mentor);
        emailSenderService.sendEmail(mentor.getUser().getEmail(), "Rating",
                "You have been rated by an applicant "+ratingDTO.getRatingValue()+" stars"+" with comment: "+ratingDTO.getComment());
        return modelMapper.map(savedMentor, MentorProfileDTO.class);
    }





    public void creatNewRating(Session session) {
        Rating rating=Rating.builder()
                .session(session)
                .mentor(session.getMentor())
                .applicant(session.getApplicant())
                .build();

        ratingRepository.save(rating);
    }

    public Page<RatingDTO> getRatings(Integer pageOffset, Integer pageSize) {
        return ratingRepository.findAll(PageRequest.of(pageOffset, pageSize))
                .map(rating -> modelMapper.map(rating, RatingDTO.class));
    }
}
