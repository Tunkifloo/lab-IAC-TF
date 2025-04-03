package com.makethediference.mtdapi.service;

import com.makethediference.mtdapi.domain.dto.volunteer.ValidateVolunteer;
import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerForm;
import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerPending;

import java.util.List;
import java.util.Optional;

public interface VolunteerService {
    void submitVolunteerForm(VolunteerForm form);
    List<VolunteerPending> getPendingVolunteers();
    Optional<VolunteerPending> getVolunteerById(Long id);
    void validateRequest(ValidateVolunteer dto);
}
