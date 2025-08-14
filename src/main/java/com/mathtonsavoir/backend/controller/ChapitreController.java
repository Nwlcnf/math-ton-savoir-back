package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.service.ChapitreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapitres")
@RequiredArgsConstructor
@Tag(name = "Chapitre", description = "Opérations liées aux chapitres")
public class ChapitreController {

    private final ChapitreService chapitreService;

    @GetMapping("/{id}")
    public ResponseEntity<Chapitre> getById(@PathVariable Long id) {
        Chapitre chapitre = chapitreService.getById(id);
        if (chapitre == null) {
            throw new ResourceNotFoundException("Chapitre avec ID " + id + " non trouvé");
        }
        return new ResponseEntity<>(chapitre, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Chapitre>> getAll() {
        List<Chapitre> chapitres = chapitreService.getAll();
        return new ResponseEntity<>(chapitres, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChapitreDTO> create(@RequestBody ChapitreDTO chapitreDTO) {
        if (chapitreDTO.nomChapitre() == null || chapitreDTO.nomChapitre().isBlank()) {
            throw new IllegalArgumentException("Le nom du chapitre est requis");
        }
        ChapitreDTO created = chapitreService.create(chapitreDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/test")
    public ResponseEntity<ChapitreDTO> create2(@RequestBody ChapitreDTO chapitreDTO) {
        ChapitreDTO created = chapitreService.create(chapitreDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapitreDTO> update(@PathVariable Long id, @RequestBody ChapitreDTO chapitreDTO) {
        ChapitreDTO updated = chapitreService.update(id, chapitreDTO);
        if (updated == null) {
            throw new ResourceNotFoundException("Impossible de mettre à jour : chapitre " + id + " introuvable");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chapitreService.delete(id); // À adapter : il faut que `delete` lève une exception si l’ID n’existe pas
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
