package edu.ifes.sistema_bibliotecario.domain.especificacao;

import edu.ifes.sistema_bibliotecario.domain.Livro;

public final class LivroEspecificacoes {

    private LivroEspecificacoes() {
    }

    public static Especificacao<Livro> autorContem(String autor) {
        String autorBuscado = autor.toLowerCase();
        return livro -> livro.getAutor() != null
                && livro.getAutor().toLowerCase().contains(autorBuscado);
    }

    public static Especificacao<Livro> disponivel(boolean valor) {
        return livro -> livro.isDisponivel() == valor;
    }
}
