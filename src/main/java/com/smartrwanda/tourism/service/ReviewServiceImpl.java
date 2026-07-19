package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.ReviewFilterRequest;
import com.smartrwanda.tourism.dto.request.ReviewRequest;
import com.smartrwanda.tourism.dto.response.RatingDistributionResponse;
import com.smartrwanda.tourism.dto.response.ReviewResponse;
import com.smartrwanda.tourism.dto.response.ReviewSummaryResponse;
import com.smartrwanda.tourism.entity.Review;
import com.smartrwanda.tourism.entity.ReviewHelpful;
import com.smartrwanda.tourism.entity.Role;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.enums.ReviewStatus;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.ReviewHelpfulRepository;
import com.smartrwanda.tourism.repository.ReviewRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewHelpfulRepository reviewHelpfulRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest request) {
        User currentUser = getCurrentUser();

        if (reviewRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), request.getTargetType(), request.getTargetId())) {
            throw new IllegalStateException(
                    "You have already reviewed this " + request.getTargetType().name().toLowerCase()
                            + ". You can update your existing review instead.");
        }

        Review review = new Review();
        review.setUser(currentUser);
        review.setTargetType(request.getTargetType());
        review.setTargetId(request.getTargetId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(ReviewStatus.ACTIVE);
        review.setImageUrls(request.getImageUrls());

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        User currentUser = getCurrentUser();
        Review review = findReviewById(reviewId);

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You can only update your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setImageUrls(request.getImageUrls());

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        User currentUser = getCurrentUser();
        Review review = findReviewById(reviewId);

        boolean isOwner = review.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new BadRequestException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewResponse> getReviews(ReviewFilterRequest filter) {
        List<Review> reviews;

        if (filter.getTargetType() != null && filter.getTargetId() != null && filter.getRating() != null) {
            reviews = reviewRepository.findByTargetTypeAndTargetIdAndRatingAndStatus(
                    filter.getTargetType(), filter.getTargetId(), filter.getRating(), ReviewStatus.ACTIVE);
        } else if (filter.getTargetType() != null && filter.getTargetId() != null) {
            reviews = reviewRepository.findByTargetTypeAndTargetIdAndStatus(
                    filter.getTargetType(), filter.getTargetId(), ReviewStatus.ACTIVE);
        } else {
            reviews = reviewRepository.findByStatus(ReviewStatus.ACTIVE);
        }

        return reviews.stream().map(this::mapToResponse).toList();
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
    }

    @Override
    public ReviewSummaryResponse getSummary(ReviewTargetType targetType, Long targetId) {
        Double average = reviewRepository.findAverageRating(targetType, targetId);
        long total = reviewRepository.countByTargetTypeAndTargetIdAndStatus(
                targetType, targetId, ReviewStatus.ACTIVE);

        RatingDistributionResponse distribution = RatingDistributionResponse.builder()
                .fiveStars(countByRating(targetType, targetId, 5))
                .fourStars(countByRating(targetType, targetId, 4))
                .threeStars(countByRating(targetType, targetId, 3))
                .twoStars(countByRating(targetType, targetId, 2))
                .oneStar(countByRating(targetType, targetId, 1))
                .build();

        return ReviewSummaryResponse.builder()
                .averageRating(average != null ? Math.round(average * 10.0) / 10.0 : 0.0)
                .totalReviews(total)
                .distribution(distribution)
                .build();
    }

    @Override
    public void markHelpful(Long reviewId) {
        User currentUser = getCurrentUser();
        Review review = findReviewById(reviewId);

        if (review.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You cannot mark your own review as helpful");
        }

        if (reviewHelpfulRepository.existsByReviewIdAndUserId(reviewId, currentUser.getId())) {
            throw new IllegalStateException("You have already marked this review as helpful");
        }

        ReviewHelpful helpful = new ReviewHelpful();
        helpful.setReview(review);
        helpful.setUser(currentUser);
        reviewHelpfulRepository.save(helpful);
    }

    @Override
    public void unmarkHelpful(Long reviewId) {
        User currentUser = getCurrentUser();

        ReviewHelpful helpful = reviewHelpfulRepository
                .findByReviewIdAndUserId(reviewId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "You have not marked this review as helpful"));

        reviewHelpfulRepository.delete(helpful);
    }

    @Override
    public void reportReview(Long reviewId) {
        Review review = findReviewById(reviewId);

        if (review.getStatus() == ReviewStatus.HIDDEN) {
            throw new BadRequestException("This review is already hidden");
        }

        review.setStatus(ReviewStatus.REPORTED);
        reviewRepository.save(review);
    }

    @Override
    public void hideReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        review.setStatus(ReviewStatus.HIDDEN);
        reviewRepository.save(review);
    }

    @Override
    public void unhideReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        review.setStatus(ReviewStatus.ACTIVE);
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getReportedReviews() {
        return reviewRepository.findByStatus(ReviewStatus.REPORTED)
                .stream().map(this::mapToResponse).toList();
    }

    private long countByRating(ReviewTargetType targetType, Long targetId, int rating) {
        return reviewRepository.countByTargetTypeAndTargetIdAndRatingAndStatus(
                targetType, targetId, rating, ReviewStatus.ACTIVE);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewerName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .targetType(review.getTargetType())
                .targetId(review.getTargetId())
                .rating(review.getRating())
                .comment(review.getComment())
                .status(review.getStatus())
                .helpfulCount(reviewHelpfulRepository.countByReviewId(review.getId()))
                .imageUrls(review.getImageUrls())
                .createdAt(review.getCreatedAt())
                .build();
    }
}