package com.makethediference.mtdapi.service.auth;

public interface AuthService {
    void authorizeRegisterUser();
    void authorizeRegisterPlaylist();
    void authorizeDisablePlaylist();
    void authorizeEnablePlaylist();
    void authorizeValidateVolunteer();
    void authorizeRegisterArea();
    void authorizeUploadLandingFile();
    void authorizeDisableLandingFile();
    void authorizeAdmin();
}
