package edu.ifes.sistema_bibliotecario.repository.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;

import java.util.List;
import java.util.Optional;

public interface LeitorRepository {
    Leitor save(Leitor leitor);
    List<Leitor> findAll();
    List<Leitor> findByTipo(TipoPessoa tipo);
    Optional<Leitor> findById(long id);
    Optional<Leitor> findByEmail(String email);
    Optional<Leitor> update(Leitor leitor);
    boolean deleteById(long id);
}
