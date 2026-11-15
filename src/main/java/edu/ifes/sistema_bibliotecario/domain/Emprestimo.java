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

    public static EmprestimoBuilder builder(@NonNull Long livroId, @NonNull Long leitorId) {
        return new EmprestimoBuilder()
                .livroId(livroId)
                .leitorId(leitorId);
    }
}
