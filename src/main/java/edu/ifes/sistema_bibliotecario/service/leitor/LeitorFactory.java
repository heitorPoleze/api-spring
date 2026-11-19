package edu.ifes.sistema_bibliotecario.service.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Component
public class LeitorFactory {

    @Value("${biblioteca.emprestimo.regras.ALUNO.limite-ativos:3}")
    private int limiteAtivosAluno;

    @Value("${biblioteca.emprestimo.regras.ALUNO.prazo-dias:7}")
    private int prazoAluno;

    @Value("${biblioteca.emprestimo.regras.ALUNO.multa-diaria:1.00}")
    private BigDecimal multaAluno;

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.limite-ativos:5}")
    private int limiteAtivosProfessor;

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.prazo-dias:14}")
    private int prazoProfessor;

    @Value("${biblioteca.emprestimo.regras.PROFESSOR.multa-diaria:0.50}")
    private BigDecimal multaProfessor;

    public Leitor leitorComValoresDeRegraEmprestimo(Leitor leitor) {
        RegraEmprestimo regra = regraPara(leitor.getTipoPessoa());

        leitor.setLimiteAtivos(regra.limiteAtivos());
        leitor.setPrazoParaRetorno(regra.prazoDias());
        leitor.setValorMultaDiario(regra.multaDiaria());
        return leitor;
    }

    private RegraEmprestimo regraPara(TipoPessoa tipoPessoa) {
        return switch (tipoPessoa) {
            case ALUNO -> new RegraEmprestimo(limiteAtivosAluno, prazoAluno, multaAluno);
            case PROFESSOR -> new RegraEmprestimo(limiteAtivosProfessor, prazoProfessor, multaProfessor);
        };
    }

    private record RegraEmprestimo(
            int limiteAtivos,
            int prazoDias,
            BigDecimal multaDiaria)
    {}
}
