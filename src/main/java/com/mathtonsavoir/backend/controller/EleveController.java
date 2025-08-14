package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.EleveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eleves")
@RequiredArgsConstructor
@Tag(name = "Élève", description = "Gestion des comptes élèves")
public class EleveController {

    private final EleveService eleveService;

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable Long id) {
        UtilisateurDTO dto = eleveService.getById(id);
        if (dto == null) {
            throw new ResourceNotFoundException("Élève avec ID " + id + " non trouvé");
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        List<UtilisateurDTO> eleves = eleveService.getAll();
        return new ResponseEntity<>(eleves, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> create(@RequestBody UtilisateurDTO dto) {
        UtilisateurDTO created = eleveService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        UtilisateurDTO updated = eleveService.update(id, dto);
        if (updated == null) {
            throw new ResourceNotFoundException("Élève avec ID " + id + " non trouvé");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eleveService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
