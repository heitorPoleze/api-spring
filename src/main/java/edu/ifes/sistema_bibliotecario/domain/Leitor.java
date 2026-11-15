package edu.ifes.sistema_bibliotecario.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Setter
@RequiredArgsConstructor
public class Leitor {

    private long id;

    @NonNull
    private String nome;

    @NonNull
    private String email;

    @NonNull
    private TipoPessoa tipoPessoa;
}
