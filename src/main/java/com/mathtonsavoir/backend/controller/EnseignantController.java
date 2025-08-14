package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.EnseignantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
@Tag(name = "Enseignant", description = "Gestion des comptes enseignants")
public class EnseignantController {

    private final EnseignantService enseignantService;

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable Long id) {
        UtilisateurDTO dto = enseignantService.getById(id);
        if (dto == null) throw new ResourceNotFoundException("Enseignant " + id + " non trouvé");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        return new ResponseEntity<>(enseignantService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> create(@RequestBody UtilisateurDTO dto) {
        return new ResponseEntity<>(enseignantService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        UtilisateurDTO updated = enseignantService.update(id, dto);
        if (updated == null) throw new ResourceNotFoundException("Enseignant " + id + " non trouvé");
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enseignantService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
