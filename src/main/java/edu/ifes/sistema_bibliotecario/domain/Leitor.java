package edu.ifes.sistema_bibliotecario.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Leitor {

    private long id;

    private String nome;

    private String email;

    private TipoPessoa tipoPessoa;
}
