package com.mathtonsavoir.backend.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleTest {

    @Test
    void testGetNomRole() {
        assertThat(Role.ADMIN.getNomRole()).isEqualTo("Administrateur");
        assertThat(Role.ENSEIGNANT.getNomRole()).isEqualTo("Enseignant");
        assertThat(Role.ELEVE.getNomRole()).isEqualTo("Élève");
    }

    @Test
    void testFromNomRole_valid() {
        assertThat(Role.fromNomRole("Administrateur")).isEqualTo(Role.ADMIN);
        assertThat(Role.fromNomRole("enseignant")).isEqualTo(Role.ENSEIGNANT);
        assertThat(Role.fromNomRole("ÉLÈVE")).isEqualTo(Role.ELEVE);
    }

    @Test
    void testFromNomRole_invalid() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromNomRole("Prof"));
        assertThrows(IllegalArgumentException.class, () -> Role.fromNomRole(""));
        assertThrows(IllegalArgumentException.class, () -> Role.fromNomRole(null));
    }

    @Test
    void testFromLabel_valid() {
        assertThat(Role.fromLabel("Administrateur")).isEqualTo(Role.ADMIN);
        assertThat(Role.fromLabel("enseignant")).isEqualTo(Role.ENSEIGNANT);
        assertThat(Role.fromLabel("ÉLÈVE")).isEqualTo(Role.ELEVE);
    }

    @Test
    void testFromLabel_invalid() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromLabel("Manager"));
        assertThrows(IllegalArgumentException.class, () -> Role.fromLabel(""));
        assertThrows(IllegalArgumentException.class, () -> Role.fromLabel(null));
    }

    @Test
    void testToUpperCase() {
        assertThat(Role.ADMIN.toUpperCase()).isEqualTo("ADMIN");
        assertThat(Role.ENSEIGNANT.toUpperCase()).isEqualTo("ENSEIGNANT");
        assertThat(Role.ELEVE.toUpperCase()).isEqualTo("ELEVE");
    }
}
