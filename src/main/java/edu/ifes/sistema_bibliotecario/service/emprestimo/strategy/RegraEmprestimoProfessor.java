package edu.ifes.sistema_bibliotecario.service.emprestimo.strategy;

import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegraEmprestimoProfessor implements RegraEmprestimoStrategy {

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.limite-ativos:5}")
    private int limiteAtivos;

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.prazo-dias:14}")
    private int prazoDias;

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.multa-diaria:0.50}")
    private BigDecimal multaDiaria;

    @Override
    public TipoPessoa tipo() {
        return TipoPessoa.PROFESSOR;
    }

    @Override
    public int limiteAtivos() {
        return limiteAtivos;
    }

    @Override
    public int prazoDias() {
        return prazoDias;
    }

    @Override
    public BigDecimal multaDiaria() {
        return multaDiaria;
    }
}
