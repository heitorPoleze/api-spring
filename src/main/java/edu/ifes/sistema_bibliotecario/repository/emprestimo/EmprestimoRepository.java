package edu.ifes.sistema_bibliotecario.repository.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {
    Emprestimo save(Emprestimo emprestimo);
    List<Emprestimo> findAll(Especificacao<Emprestimo> spec);
    Optional<Emprestimo> findById(long id);
    List<Emprestimo> findByLeitorId(long id);
    List<Emprestimo> findAtivosByLeitorId(long id);
    List<Emprestimo> findByLivroId(long id);
    List<Emprestimo> findAtivosByLivroId(long livroId);
    List<Emprestimo> findAtrasadosAPartirDe(LocalDate dataReferencia);
    List<Emprestimo> findAtrasadosByLeitorId(long leitorId, LocalDate dataReferencia);
    Optional<Emprestimo> atribuirDataDevolucao(long id, LocalDate dataDevolucao);
}
