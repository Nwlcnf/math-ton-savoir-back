package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.model.PdfLecon;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import com.mathtonsavoir.backend.repository.PdfLeconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lecons")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LeconController {

    private final LeconRepository leconRepository;
    private final ChapitreRepository chapitreRepository;
    private final PdfLeconRepository pdfLeconRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLecon(
            @RequestParam("nomLecon") String nomLecon,
            @RequestParam("chapitreId") Long chapitreId,
            @RequestParam("pdf") MultipartFile pdfFile
    ) {
        try {
            // 1️⃣ Récupérer le chapitre
            Chapitre chapitre = chapitreRepository.findById(chapitreId)
                    .orElseThrow(() -> new RuntimeException("Chapitre introuvable"));

            // 2️⃣ Générer un nom unique pour le PDF
            String filename = UUID.randomUUID() + "_" + pdfFile.getOriginalFilename();

            // 3️⃣ Sauvegarder la leçon
            Lecon lecon = Lecon.builder()
                    .nomLecon(nomLecon)
                    .nomFichierPdf(filename)
                    .chapitre(chapitre)
                    .build();
            leconRepository.save(lecon); // ID généré ici

            // 4️⃣ Sauvegarder le PDF dans la table PdfLecon
            PdfLecon pdfLecon = PdfLecon.builder()
                    .nomPdf(filename)
                    .pdfData(pdfFile.getBytes()) // PostgreSQL attend bytea, pas bigint
                    .lecon(lecon) // référence à la leçon existante
                    .build();
            pdfLeconRepository.save(pdfLecon);

            // 5️⃣ Retourner la leçon créée
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(LeconDTO.fromEntity(lecon));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue : " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<LeconDTO>> getAllLecons() {
        List<LeconDTO> lecons = leconRepository.findAll()
                .stream()
                .map(LeconDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(lecons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeconDTO> getLeconById(@PathVariable Long id) {
        Lecon lecon = leconRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leçon introuvable"));
        return ResponseEntity.ok(LeconDTO.fromEntity(lecon));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdfByLeconId(@PathVariable Long id) {
        PdfLecon pdfLecon = pdfLeconRepository.findByLecon_IdLecon(id)
                .orElseThrow(() -> new RuntimeException("PDF introuvable pour cette leçon"));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdfLecon.getNomPdf() + "\"")
                .body(pdfLecon.getPdfData());
    }
}
