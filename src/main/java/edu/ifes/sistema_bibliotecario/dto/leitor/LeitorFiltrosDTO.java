package edu.ifes.sistema_bibliotecario.dto.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;

import static edu.ifes.sistema_bibliotecario.domain.especificacao.LeitorEspecificacoes.doTipo;

public record LeitorFiltrosDTO(
        TipoPessoa tipo
) {
    public Especificacao<Leitor> toEspecificacao() {
        Especificacao<Leitor> spec = Especificacao.todos();
        if (tipo != null) spec = spec.and(doTipo(tipo));
        return spec;
    }
}
