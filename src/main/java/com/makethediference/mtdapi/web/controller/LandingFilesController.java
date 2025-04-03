    package com.makethediference.mtdapi.web.controller;

    import com.makethediference.mtdapi.domain.entity.FileSector;
    import com.makethediference.mtdapi.domain.entity.LandingFiles;
    import com.makethediference.mtdapi.service.LandingFilesService;
    import com.makethediference.mtdapi.service.auth.AuthService;
    import com.makethediference.mtdapi.service.aws.R2Service;
    import lombok.AllArgsConstructor;
    import org.jetbrains.annotations.Nullable;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.net.URL;
    import java.util.List;
    import java.util.Optional;
    import java.util.Set;

    @RestController
    @AllArgsConstructor
    @RequestMapping("/api/v1/landing-files")
    @CrossOrigin("*")
    public class LandingFilesController {

        private final AuthService authService;
        private final LandingFilesService landingFilesService;
        private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg", "image/webp", "application/pdf");
        private final R2Service r2Service;

        @PostMapping("/register")
        @Transactional
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> uploadFile(
                @RequestParam("file") MultipartFile file,
                @RequestParam("adminId") Long adminId,
                @RequestParam("fileSector") FileSector fileSector,
                @RequestParam(value = "makerName", required = false) String makerName,
                @RequestParam(value = "teamName", required = false) String teamName,
                @RequestParam(value = "stand", required = false) String stand,
                @RequestParam(value = "description", required = false) String description) {

            if (!ALLOWED_TYPES.contains(file.getContentType())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tipo de archivo no permitido. Solo se aceptan PNG, JPG, WEBP y PDF.");
            }

        authService.authorizeUploadLandingFile();
        LandingFiles savedFile = landingFilesService.saveLandingFile(file, adminId, fileSector, makerName, description, teamName, stand);
        return ResponseEntity.ok(savedFile);
    }

        @GetMapping("/{id}")
        public ResponseEntity<?> getLandingFileById(@PathVariable Long id) {
            Optional<LandingFiles> landingFileOpt = landingFilesService.getLandingFileById(id);
            if (landingFileOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            LandingFiles landingFile = landingFileOpt.get();
            String fileKey = landingFile.getFileName();

            if (!r2Service.doesObjectExist(fileKey)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El recurso no se encuentra disponible");
            }

            return ResponseEntity.ok(r2Service.getFileUrl(fileKey));
        }

        @GetMapping("/all")
        public ResponseEntity<List<LandingFiles>> getAllLandingFiles() {
            List<LandingFiles> files = landingFilesService.getAllLandingFiles();
            return ResponseEntity.ok(files);
        }

        @PutMapping("/update/{id}")
        @Transactional
        public ResponseEntity<LandingFiles> updateLandingFile(
                @PathVariable Long id,
                @RequestParam("file") MultipartFile file) {
            Optional<LandingFiles> updatedFile = landingFilesService.updateLandingFile(id, file);
            return updatedFile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PatchMapping("/disable/{id}")
        @Transactional
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> disableLandingFile(@PathVariable Long id) {
            this.authService.authorizeDisableLandingFile();
            boolean disabled = landingFilesService.disableLandingFile(id);
            return disabled
                    ? ResponseEntity.ok("Se ha deshabilitado la imagen correctamente")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la imagen a deshabilitar");
        }

        @GetMapping("/download/{id}")
        public ResponseEntity<?> downloadLandingFile(@PathVariable Long id) {
            Optional<LandingFiles> landingFileOpt = landingFilesService.getLandingFileById(id);
            if (landingFileOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            LandingFiles landingFile = landingFileOpt.get();
            String originalFileName = getOriginalFileName(landingFile);

            // Verificamos que el objeto exista usando el key original.
            if (!r2Service.doesObjectExist(originalFileName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El recurso no se encuentra disponible");
            }

            // Descargamos el archivo desde S3 usando el key original.
            byte[] fileContent = r2Service.downloadFile(originalFileName);
            if (fileContent == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al descargar el archivo desde S3");
            }

            HttpHeaders headers = new HttpHeaders();
            // Establecemos el nombre de archivo limpio en el header Content-Disposition
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"");
            String contentType = landingFile.getFileTypes() != null ? landingFile.getFileTypes() : "application/pdf";
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileContent);
        }

        @Nullable
        private static String getOriginalFileName(LandingFiles landingFile) {
            String storedValue = landingFile.getFileName();
            String originalFileName = storedValue;
            try {
                URL url = new URL(storedValue);
                // url.getPath() retorna algo como "/1740758337052_CV%20Zahir%20Aredo.pdf"
                originalFileName = url.getPath();
                // Quitamos la "/" inicial si existe
                if (originalFileName.startsWith("/")) {
                    originalFileName = originalFileName.substring(1);
                }
            } catch (Exception e) {
                // Si no se puede parsear como URL, buscamos el caracter "?" para quitar la query
                int pos = storedValue.indexOf("?");
                if (pos > 0) {
                    originalFileName = storedValue.substring(0, pos);
                }
            }
            return originalFileName;
        }
    }
