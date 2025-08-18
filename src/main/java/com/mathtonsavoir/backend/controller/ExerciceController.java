package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.service.ExerciceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/exercices")
@RequiredArgsConstructor
@Tag(name = "Exercice", description = "Opérations liées aux exercices")
public class ExerciceController {

    private static final Logger log = LoggerFactory.getLogger(ExerciceController.class);

    private final ExerciceService exerciceService;

    @GetMapping("/{id}")
    public ResponseEntity<ExerciceDTO> getById(@PathVariable Long id) {
        log.info("Test log from Spring Boot");
        ExerciceDTO dto = exerciceService.getById(id);
        if (dto == null) throw new ResourceNotFoundException("Exercice " + id + " non trouvé");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Exercice>> getAll() {
        return new ResponseEntity<>(exerciceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/dtos")
    public ResponseEntity<List<ExerciceDTO>> getAllAsDto() {
        return new ResponseEntity<>(exerciceService.getAllAsDto(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ExerciceDTO> create(@RequestBody ExerciceDTO exerciceDTO) {
        return new ResponseEntity<>(exerciceService.create(exerciceDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciceDTO> update(@PathVariable Long id, @RequestBody ExerciceDTO exerciceDTO) {
        ExerciceDTO updated = exerciceService.update(id, exerciceDTO);
        if (updated == null) throw new ResourceNotFoundException("Exercice " + id + " non trouvé");
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
