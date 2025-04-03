package com.makethediference.mtdapi.infra.mapper;

import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerForm;
import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerPending;
import com.makethediference.mtdapi.domain.entity.Area;
import com.makethediference.mtdapi.domain.entity.Role;
import com.makethediference.mtdapi.domain.entity.Volunteer;
import com.makethediference.mtdapi.domain.entity.VolunteerStatus;
import com.makethediference.mtdapi.infra.repository.AreaRepository;
import com.makethediference.mtdapi.infra.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class VolunteerMapper {

    private final AreaRepository areaRepository;
    private final VolunteerRepository volunteerRepository;

    public Volunteer toEntity(VolunteerForm form) {
        Area area = areaRepository.findById(form.areaId())
                .orElseThrow(() -> new IllegalArgumentException("Área no encontrada"));

        int age = calculateAge(form.birthdate());
        validateAge(age);

        return Volunteer.builder()
                .name(form.name())
                .paternalSurname(form.paternalSurname())
                .maternalSurname(form.maternalSurname())
                .dni(form.dni())
                .email(form.email())
                .birthdate(form.birthdate())
                .age(age)
                .phoneNumber(form.phoneNumber())
                .codeNumber(form.codeNumber())
                .country(form.country())
                .region(form.region())
                .motivation(form.motivation())
                .estimatedHours(form.estimatedHours())
                .status(VolunteerStatus.PENDING)
                .appliedArea(area)
                .enabled(false)
                .firstLogin(false)
                .role(Role.MAKER)
                .submissionDate(LocalDateTime.now())
                .build();
    }

    public VolunteerPending toPending(Volunteer volunteer) {
        var area = volunteer.getAppliedArea();
        Long areaId = (area != null) ? area.getAreaId() :  null;
        String areaName = (area != null) ? area.getName() : null;
        return new VolunteerPending(
                volunteer.getUserId(),
                volunteer.getName(),
                volunteer.getPaternalSurname(),
                volunteer.getMaternalSurname(),
                volunteer.getEmail(),
                volunteer.getDni(),
                volunteer.getPhoneNumber(),
                volunteer.getCodeNumber(),
                volunteer.getBirthdate(),
                areaId,
                areaName,
                volunteer.getEstimatedHours(),
                volunteer.getMotivation(),
                volunteer.getStatus(),
                volunteer.getSubmissionDate()
        );
    }

    private int calculateAge(LocalDate birthdate) {
        if (birthdate == null) return 0;
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    private void validateAge(int age) {
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("La edad debe estar entre 16 y 100 años.");
        }
    }
}
