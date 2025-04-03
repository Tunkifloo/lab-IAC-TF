package com.makethediference.mtdapi.service.impl;

import com.makethediference.mtdapi.domain.dto.volunteer.ValidateVolunteer;
import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerForm;
import com.makethediference.mtdapi.domain.dto.volunteer.VolunteerPending;
import com.makethediference.mtdapi.domain.entity.Role;
import com.makethediference.mtdapi.domain.entity.Volunteer;
import com.makethediference.mtdapi.domain.entity.VolunteerStatus;
import com.makethediference.mtdapi.infra.mapper.VolunteerMapper;
import com.makethediference.mtdapi.infra.repository.UserRepository;
import com.makethediference.mtdapi.infra.repository.VolunteerRepository;
import com.makethediference.mtdapi.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserNameGeneratorServiceImpl userNameGeneratorServiceImpl;
    private final EmailNotificationServiceImpl emailNotificationServiceImpl;
    private static final String ALPHANUMERIC_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;

    @Override
    public void submitVolunteerForm(VolunteerForm form) {
        if (volunteerRepository.existsByEmail(form.email())) {
            throw new IllegalArgumentException("El email ya está en uso en voluntarios pendientes.");
        }
        if (volunteerRepository.existsByDni(form.dni())) {
            throw new IllegalArgumentException("El DNI ya está en uso en voluntarios pendientes.");
        }
        if (volunteerRepository.existsByPhoneNumber(form.phoneNumber())) {
            throw new IllegalArgumentException("El teléfono ya está en uso en voluntarios pendientes.");
        }
        if (userRepository.existsByEmail(form.email())) {
            throw new IllegalArgumentException("El email de la solicitud ya está registrado en un usuario del sistema");
        }
        if (userRepository.existsByPhoneNumber(form.phoneNumber())) {
            throw new IllegalArgumentException("El numero de la solicitud ya está registrado en un usuario del sistema");
        }
        if (userRepository.existsByDni(form.dni())) {
            throw new IllegalArgumentException("El DNI de la solicitud ya está registrado en un usuario del sistema");
        }

        Volunteer request = volunteerMapper.toEntity(form);
        volunteerRepository.save(request);
    }

    @Override
    public List<VolunteerPending> getPendingVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findByStatus(VolunteerStatus.PENDING);
        return volunteers.stream()
                .map(volunteerMapper::toPending)
                .toList();
    }

    @Override
    public Optional<VolunteerPending> getVolunteerById(Long id) {
        return volunteerRepository.findById(id)
                .filter(volunteer ->  volunteer.getStatus() == VolunteerStatus.PENDING)
                .map(volunteerMapper::toPending);
    }

    @Override
    public void validateRequest(ValidateVolunteer dto) {
        Volunteer volunteer = volunteerRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Solicitud de voluntario no encontrada con ID: " + dto.userId()));

        if (volunteer.getAppliedArea() == null) {
            throw new IllegalStateException("El voluntario no tiene un area asignada");
        }

        if (volunteer.getStatus() != VolunteerStatus.PENDING) {
            throw new IllegalStateException("La solicitud ya fue procesada. Estado actual: " + volunteer.getStatus());
        }

        volunteer.setAdminComments(dto.adminComments());

        if (dto.approved()) {
            String autoUsername = userNameGeneratorServiceImpl.generateUsername(
                    volunteer.getName(),
                    volunteer.getPaternalSurname(),
                    volunteer.getMaternalSurname()
            );
            String randomPassword = generateRandomPassword();

            volunteer.setUsername(autoUsername);
            volunteer.setPassword(passwordEncoder.encode(randomPassword));
            volunteer.setEnabled(true);
            volunteer.setFirstLogin(true);
            volunteer.setRole(Role.MAKER);
            volunteer.setStatus(VolunteerStatus.APPROVED);

            emailNotificationServiceImpl.sendVolunteerApprovalEmail(
                    volunteer.getEmail(),
                    autoUsername,
                    randomPassword,
                    volunteer.getRole().name()
            );
            volunteer.setValidationDate(LocalDateTime.now());
            userRepository.save(volunteer);
        } else {
            volunteerRepository.delete(volunteer);
        }
    }

    private String generateRandomPassword() {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARS.length());
            sb.append(ALPHANUMERIC_CHARS.charAt(index));
        }
        return sb.toString();
    }
}
