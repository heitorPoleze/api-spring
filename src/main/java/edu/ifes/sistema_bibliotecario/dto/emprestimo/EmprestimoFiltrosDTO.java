package edu.ifes.sistema_bibliotecario.dto.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;

import static edu.ifes.sistema_bibliotecario.domain.especificacao.EmprestimoEspecificacoes.ativo;
import static edu.ifes.sistema_bibliotecario.domain.especificacao.EmprestimoEspecificacoes.doLeitor;

public record EmprestimoFiltrosDTO(
        Long leitorId,
        Boolean ativos
) {
    public Especificacao<Emprestimo> toEspecificacao() {
        Especificacao<Emprestimo> spec = Especificacao.todos();
        if (leitorId != null) spec = spec.and(doLeitor(leitorId));
        if (ativos != null) spec = spec.and(ativo(ativos));
        return spec;
    }
}
