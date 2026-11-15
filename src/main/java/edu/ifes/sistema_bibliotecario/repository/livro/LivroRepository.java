package edu.ifes.sistema_bibliotecario.repository.livro;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroFiltrosDTO;

import java.util.List;
import java.util.Optional;

public interface LivroRepository {
    Livro save(Livro livro);
    List<Livro> findByFiltro(LivroFiltrosDTO filtrosDTO);
    Optional<Livro> findById(long id);
    Optional<Livro> findByIsbn(String isbn);
    Optional<Livro> update(Livro livro);
    boolean deleteById(long id);
}
