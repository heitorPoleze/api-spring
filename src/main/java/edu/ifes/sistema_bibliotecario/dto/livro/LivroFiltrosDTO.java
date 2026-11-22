package edu.ifes.sistema_bibliotecario.dto.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;
import edu.ifes.sistema_bibliotecario.domain.especificacao.LivroEspecificacoes;

public record LivroFiltrosDTO(
        String autor,
        Boolean disponivel
) {
    public Especificacao<Livro> toEspecificacao() {
        Especificacao<Livro> spec = Especificacao.todos();
        if (autor != null) spec = spec.and(LivroEspecificacoes.autorContem(autor));
        if (disponivel != null) spec = spec.and(LivroEspecificacoes.disponivel(disponivel));
        return spec;
    }
}
