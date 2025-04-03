package com.makethediference.mtdapi.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService implements AuthService {

    @Override
    public void authorizeRegisterUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden registrar usuarios.");
        }
    }

    @Override
    public void authorizeRegisterPlaylist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden registrar playlists.");
        }
    }

    @Override
    public void authorizeDisablePlaylist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden deshabilitar playlists.");
        }
    }

    @Override
    public void authorizeEnablePlaylist()  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden habilitar playlists.");
        }
    }

    @Override
    public void authorizeValidateVolunteer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden validar voluntarios.");
        }
    }

    @Override
    public void authorizeRegisterArea() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden registrar areas.");
        }
    }

    @Override
    public void authorizeUploadLandingFile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden subir archivos para el Landing.");
        }
    }

    @Override
    public void authorizeDisableLandingFile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: solo los administradores pueden deshabilitar archivos para el Landing.");
        }
    }

    public void authorizeAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new SecurityException("Acceso denegado: Se requiere rol ADMIN.");
        }
    }
}
