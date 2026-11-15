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
public class Livro {

    private long id;

    private String titulo;

    private String autor;

    private String isbn;

    private Integer anoPublicacao;

    private Integer quantidadeTotal;

    private Integer quantidadeDisponivel;

    public boolean isDisponivel() {
        return this.quantidadeDisponivel != null && this.quantidadeDisponivel > 0;
    }
}