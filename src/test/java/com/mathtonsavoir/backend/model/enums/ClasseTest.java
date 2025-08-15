package com.mathtonsavoir.backend.model.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClasseTest {

    @Test
    void testGetNomClasse() {
        assertThat(Classe.SIXIEME.getNomClasse()).isEqualTo("6eme");
        assertThat(Classe.CINQUIEME.getNomClasse()).isEqualTo("5eme");
        assertThat(Classe.QUATRIEME.getNomClasse()).isEqualTo("4eme");
        assertThat(Classe.TROISIEME.getNomClasse()).isEqualTo("3eme");
    }

    @Test
    void testFromLabel_valid() {
        assertThat(Classe.fromLabel("6eme")).isEqualTo(Classe.SIXIEME);
        assertThat(Classe.fromLabel("5EME")).isEqualTo(Classe.CINQUIEME); // test insensible à la casse
        assertThat(Classe.fromLabel("4eme")).isEqualTo(Classe.QUATRIEME);
        assertThat(Classe.fromLabel("3eme")).isEqualTo(Classe.TROISIEME);
    }

    @Test
    void testFromLabel_invalid() {
        assertThrows(IllegalArgumentException.class, () -> Classe.fromLabel("2eme"));
        assertThrows(IllegalArgumentException.class, () -> Classe.fromLabel("Unknown"));
        assertThrows(IllegalArgumentException.class, () -> Classe.fromLabel(""));
        assertThrows(IllegalArgumentException.class, () -> Classe.fromLabel(null));
    }
}
