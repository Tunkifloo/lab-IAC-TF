package com.makethediference.mtdapi.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity(name = "Volunteer")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "volunteers")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Volunteer extends User{

    @Enumerated(EnumType.STRING)
    private VolunteerStatus status = VolunteerStatus.PENDING;
    private String adminComments;
    private LocalDateTime submissionDate;
    private LocalDateTime validationDate;

}
