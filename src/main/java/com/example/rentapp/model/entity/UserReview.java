package com.example.rentapp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_review")
public class UserReview extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "reviewer_user")
    private UserEntity reviewerUser;

    @ManyToOne
    @JoinColumn(name = "reviewed_user")
    private UserEntity reviewedUser;

    @Column(name = "number_of_stars")
    private Integer numberOfStars;

    @Column(name = "comment")
    private String comment;

    //todo: identifier of operation after review is made

}
