package edu.ifes.sistema_bibliotecario.service.emprestimo.strategy;

import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegraEmprestimoAluno implements RegraEmprestimoStrategy {

    @Value("${biblioteca.emprestimo.regras.ALUNO.limite-ativos:3}")
    private int limiteAtivos;

    @Value("${biblioteca.emprestimo.regras.ALUNO.prazo-dias:7}")
    private int prazoDias;

    @Value("${biblioteca.emprestimo.regras.ALUNO.multa-diaria:1.00}")
    private BigDecimal multaDiaria;

    @Override
    public TipoPessoa tipo() {
        return TipoPessoa.ALUNO;
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
