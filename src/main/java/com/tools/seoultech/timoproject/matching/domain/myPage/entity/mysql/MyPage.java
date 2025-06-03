package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.ReviewStatus;
import com.tools.seoultech.timoproject.matching.service.converter.ReviewConverter;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.review.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "matching_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MyPage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="mypage_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MatchingCategory matchingCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatchingStatus matchingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acceptor_id")
    private Member acceptor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id")
    private Member requestor;


    /** Review 관련 */
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Convert(converter = ReviewConverter.class)
    @Column(columnDefinition = "JSON")
    private Review acceptorReview;

    @Convert(converter = ReviewConverter.class)
    @Column(columnDefinition = "JSON")
    private Review requestorReview;

    protected MyPage(MatchingCategory matchingCategory, MatchingStatus status, Member acceptor, Member requestor){
        this.matchingCategory = matchingCategory;
        this.matchingStatus = status;
        this.acceptor = acceptor;
        this.requestor = requestor;
        this.reviewStatus = ReviewStatus.UNREVIEWED;
        this.acceptorReview = new Review();
        this.requestorReview = new Review();
    }

    public  MyPage updateReview(Review review, Boolean isAcceptor){
        if(isAcceptor){
            this.acceptorReview = review;
            this.reviewStatus = reviewStatus.nextStatus(isAcceptor);
        } else {
            this.requestorReview = review;
            this.reviewStatus = reviewStatus.nextStatus(isAcceptor);
        }
        return this;
    }
}
