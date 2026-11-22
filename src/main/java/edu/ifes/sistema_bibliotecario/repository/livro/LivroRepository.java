package edu.ifes.sistema_bibliotecario.repository.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;

import java.util.List;
import java.util.Optional;

public interface LivroRepository {
    Livro save(Livro livro);
    List<Livro> findAll(Especificacao<Livro> spec);
    Optional<Livro> findById(long id);
    Optional<Livro> findByIsbn(String isbn);
    Optional<Livro> update(Livro livro);
    boolean deleteById(long id);
}
