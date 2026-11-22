package edu.ifes.sistema_bibliotecario.domain.especificacao;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;

public final class LeitorEspecificacoes {

    private LeitorEspecificacoes() {
    }

    public static Especificacao<Leitor> doTipo(TipoPessoa tipo) {
        return leitor -> leitor.getTipoPessoa() == tipo;
    }
}
