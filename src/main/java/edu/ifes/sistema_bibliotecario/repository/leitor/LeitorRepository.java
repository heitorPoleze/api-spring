package edu.ifes.sistema_bibliotecario.repository.leitor;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;

import java.util.List;
import java.util.Optional;

public interface LeitorRepository {
    Leitor save(Leitor leitor);
    List<Leitor> findAll();
    List<Leitor> findAll(Especificacao<Leitor> spec);
    Optional<Leitor> findById(long id);
    Optional<Leitor> findByEmail(String email);
    Optional<Leitor> update(Leitor leitor);
    boolean deleteById(long id);
}
