package com.mathtonsavoir.backend.model;

import com.mathtonsavoir.backend.model.Lecon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfLecon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomPdf;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_data", columnDefinition = "bytea")
    private byte[] pdfData;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecon_id", referencedColumnName = "idLecon")
    private Lecon lecon;
}
