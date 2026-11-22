package edu.ifes.sistema_bibliotecario.repository.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.especificacao.Especificacao;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmprestimoRepositoryMemoria implements  EmprestimoRepository {
    private final List<Emprestimo> banco = new ArrayList<>();
    private long contador = 0;

    @Override
    public Emprestimo save(Emprestimo emprestimo){
        emprestimo.setId(this.incrementarId());
        this.banco.add(emprestimo);
        return emprestimo;
    }

    @Override
    public List<Emprestimo> findAll(Especificacao<Emprestimo> spec){
        return this.banco.stream()
                .filter(emprestimo -> spec.atende(emprestimo))
                .toList();
    }

    @Override
    public Optional<Emprestimo> findById(long id){
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.getId() == id)
                .findFirst();
    }

    @Override
    public List<Emprestimo> findByLeitorId(long id){
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.getLeitorId() == id)
                .toList();
    }

    @Override
    public List<Emprestimo> findAtivosByLeitorId(long id){
        List<Emprestimo> emprestimosDoLeitor = this.findByLeitorId(id);
        return emprestimosDoLeitor.stream()
                .filter(Emprestimo::isAtivo)
                .toList();
    }

    @Override
    public List<Emprestimo> findByLivroId(long id){
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.getLivroId() == id)
                .toList();
    }

    @Override
    public List<Emprestimo> findAtivosByLivroId(long id){
        List<Emprestimo> emprestimosDoLivro = this.findByLivroId(id);
        return emprestimosDoLivro.stream()
                .filter(Emprestimo::isAtivo)
                .toList();
    }

    @Override
    public List<Emprestimo> findAtrasadosAPartirDe(LocalDate dataReferencia){
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.isAtrasado(dataReferencia))
                .toList();
    }

    @Override
    public List<Emprestimo> findAtrasadosByLeitorId(long leitorId, LocalDate dataReferencia){
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.isAtrasado(dataReferencia) && emprestimo.getLeitorId() == leitorId)
                .toList();
    }

    @Override
    public Optional<Emprestimo> atribuirDataDevolucao(long id, LocalDate dataDevolucao) {
        return this.banco.stream()
                .filter(emprestimo -> emprestimo.getId() == id)
                .findFirst()
                .map(emprestimo -> {
                    emprestimo.setDataDevolucao(dataDevolucao);
                    return emprestimo;
                });
    }

    private long incrementarId(){
        return ++contador;
    }
}
