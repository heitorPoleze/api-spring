package edu.ifes.sistema_bibliotecario.domain;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Emprestimo {
    private Long id;

    private Long livroId;

    private Long leitorId;

    private LocalDate dataEmprestimo;

    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataDevolucao;

    private BigDecimal multa;

    public boolean isAtivo() {
        return this.dataDevolucao == null;
    }

    public boolean isAtrasado(LocalDate dataReferencia) {
        return this.isAtivo()
                && this.dataPrevistaDevolucao != null
                && dataReferencia.isAfter(this.dataPrevistaDevolucao);
    }
}
