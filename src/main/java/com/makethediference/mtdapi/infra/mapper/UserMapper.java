package com.makethediference.mtdapi.infra.mapper;

import com.makethediference.mtdapi.domain.dto.user.ListUser;
import com.makethediference.mtdapi.domain.dto.user.MyProfile;
import com.makethediference.mtdapi.domain.dto.user.RegisterUser;
import com.makethediference.mtdapi.domain.dto.user.UpdateProfile;
import com.makethediference.mtdapi.domain.entity.EstimatedHours;
import com.makethediference.mtdapi.domain.entity.Role;
import com.makethediference.mtdapi.domain.entity.User;
import com.makethediference.mtdapi.service.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class UserMapper {

    /**
     * Convierte un DTO de registro (RegisterUser) en la entidad User (subclase según el Role).
     * Se apoya en UserFactory para obtener la subclase correcta.
     *
     * @param registerUser DTO de entrada con los datos para crear al usuario
     * @param role         Rol del nuevo usuario
     * @return instancia concreta de User (Admin, Maker, Council o Coordinator)
     */
    public User toEntity(RegisterUser registerUser, Role role) {
        User user = UserFactory.createUser(role);

        fillCommonFields(
                user,
                registerUser.name(),
                registerUser.paternalSurname(),
                registerUser.maternalSurname(),
                registerUser.dni(),
                registerUser.email(),
                registerUser.birthdate(),
                registerUser.phoneNumber(),
                registerUser.codeNumber(),
                registerUser.country(),
                registerUser.region(),
                registerUser.motivation(),
                registerUser.estimatedHours()
        );
        user.setRole(role);

        return user;
    }


    /**
     * Convierte la entidad User en un DTO ListUser para mostrar info resumida.
     */
    public ListUser toDto(User user) {
        return new ListUser(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.getName(),
                user.getPaternalSurname(),
                user.getMaternalSurname(),
                user.getDni(),
                user.getEmail(),
                user.getAge(),
                user.getBirthdate(),
                user.getPhoneNumber(),
                user.getCodeNumber(),
                user.getCountry(),
                user.getRegion(),
                user.getMotivation(),
                user.getEstimatedHours(),
                user.getAppliedArea() != null ? user.getAppliedArea().getName() : null
        );
    }

    /**
     * Convierte la entidad User en un DTO MyProfile.
     * (Puedes usar el mismo approach: o bien modelMapper, o mapeo manual)
     */
    public MyProfile toMyProfile(User user) {
        return new MyProfile(
                user.getUsername(),
                user.getRole(),
                user.getName(),
                user.getPaternalSurname(),
                user.getMaternalSurname(),
                user.getDni(),
                user.getEmail(),
                user.getAge(),
                user.getBirthdate(),
                user.getPhoneNumber(),
                user.getCodeNumber(),
                user.getCountry(),
                user.getRegion(),
                user.getMotivation(),
                user.getEstimatedHours()
        );
    }

    public void updateFromProfile(UpdateProfile dto, User user) {
        fillCommonFields(
                user,
                dto.name(),
                dto.paternalSurname(),
                dto.maternalSurname(),
                dto.dni(),
                dto.email(),
                dto.birthdate(),
                dto.phoneNumber(),
                dto.codeNumber(),
                dto.country(),
                dto.region(),
                dto.motivation(),
                dto.estimatedHours()
        );
    }


    public UpdateProfile toUpdateProfile(User user) {
        return new UpdateProfile(
                user.getName(),
                user.getPaternalSurname(),
                user.getMaternalSurname(),
                user.getDni(),
                user.getEmail(),
                user.getBirthdate(),
                user.getPhoneNumber(),
                user.getCodeNumber(),
                user.getCountry(),
                user.getRegion(),
                user.getMotivation(),
                user.getEstimatedHours()
        );
    }


    private void setBirthdateAndAge(User user, LocalDate birthdate) {
        user.setBirthdate(birthdate);
        if (birthdate != null) {
            int years = Period.between(birthdate, LocalDate.now()).getYears();
            user.setAge(Math.max(years, 0));
        } else {
            user.setAge(0);
        }
    }

    private void fillCommonFields(
            User user,
            String name,
            String paternal,
            String maternal,
            String dni,
            String email,
            LocalDate birthdate,
            String phoneNumber,
            String codeNumber,
            String country,
            String region,
            String motivation,
            EstimatedHours estimatedHours
    ) {
        user.setName(name);
        user.setPaternalSurname(paternal);
        user.setMaternalSurname(maternal);
        user.setDni(dni);
        user.setEmail(email);
        setBirthdateAndAge(user, birthdate);
        int age = user.getAge();
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("La edad debe estar entre 16 y 100 años.");
        }
        user.setPhoneNumber(phoneNumber);
        user.setCodeNumber(codeNumber);
        user.setCountry(country);
        user.setRegion(region);
        user.setMotivation(motivation);
        user.setEstimatedHours(estimatedHours);
    }
}
