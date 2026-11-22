package edu.ifes.sistema_bibliotecario.service.emprestimo.strategy;

import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public interface RegraEmprestimoStrategy {

    TipoPessoa tipo();

    int limiteAtivos();

    int prazoDias();

    BigDecimal multaDiaria();

    default LocalDate calcularDataPrevistaDevolucao(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(prazoDias());
    }

    default BigDecimal calcularMulta(LocalDate dataPrevista, LocalDate dataDevolucao) {
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        return multaDiaria().multiply(BigDecimal.valueOf(diasAtraso));
    }
}
