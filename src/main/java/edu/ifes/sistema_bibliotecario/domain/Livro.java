package edu.ifes.sistema_bibliotecario.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

@Getter
@Setter
@RequiredArgsConstructor
public class Livro {
    @Setter(AccessLevel.NONE)
    private long id;

    @NonNull
    private String titulo;

    @NonNull
    private String autor;

    @NonNull
    private String isbn;

    @NonNull
    private Integer anoPublicacao;

    @NonNull
    private Integer quantidadeTotal;

    private Integer quantidadeDisponivel;
}
