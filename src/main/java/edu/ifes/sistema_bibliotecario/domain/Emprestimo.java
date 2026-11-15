package edu.ifes.sistema_bibliotecario.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Emprestimo {
    private Long id;

    @NonNull
    @Setter(AccessLevel.NONE)
    private Long livroId;

    @NonNull
    @Setter(AccessLevel.NONE)
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
