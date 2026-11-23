package edu.ifes.sistema_bibliotecario.config.carga;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.repository.emprestimo.EmprestimoRepository;
import edu.ifes.sistema_bibliotecario.repository.leitor.LeitorRepository;
import edu.ifes.sistema_bibliotecario.repository.livro.LivroRepository;
import edu.ifes.sistema_bibliotecario.service.emprestimo.strategy.RegraEmprestimoResolver;
import edu.ifes.sistema_bibliotecario.service.emprestimo.strategy.RegraEmprestimoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import edu.ifes.sistema_bibliotecario.config.clock.ClockConfig;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CargaInicialDados {

    private final LeitorRepository leitorRepository;
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final RegraEmprestimoResolver regraEmprestimoResolver;
    private final ClockConfig clockConfig;

    public void carregar() {
        LocalDate hoje = this.clockConfig.getDataAtual();

        List<Leitor> leitores = criarLeitores();
        List<Livro> livros = criarLivros();

        List<Emprestimo> devolvidos = criarEmprestimosDevolvidos(leitores, livros, hoje);
        List<Emprestimo> ativos = criarEmprestimosAtivos(leitores, livros, hoje);
        List<Emprestimo> atrasados = criarEmprestimosAtrasados(leitores, livros, hoje);

        int totalEmprestimos = devolvidos.size() + ativos.size() + atrasados.size();

        System.out.println("Carga inicial concluída: " + leitores.size() + " leitores, "
                + livros.size() + " livros, "
                + totalEmprestimos + " empréstimos ("
                + devolvidos.size() + " devolvidos, "
                + ativos.size() + " ativos, "
                + atrasados.size() + " atrasados).");
    }

    private List<Leitor> criarLeitores() {
        return List.of(
                salvarLeitor("Ana Souza", "ana.souza@email.com", TipoPessoa.ALUNO),
                salvarLeitor("Bruno Lima", "bruno.lima@email.com", TipoPessoa.ALUNO),
                salvarLeitor("Carla Mendes", "carla.mendes@email.com", TipoPessoa.ALUNO),
                salvarLeitor("Daniel Rocha", "daniel.rocha@email.com", TipoPessoa.PROFESSOR),
                salvarLeitor("Eliane Castro", "eliane.castro@email.com", TipoPessoa.PROFESSOR)
        );
    }

    private List<Livro> criarLivros() {
        return List.of(
                salvarLivro("Clean Code", "Robert C. Martin", "9780132350884", 2008, 3),
                salvarLivro("Refactoring", "Martin Fowler", "9780134757599", 2018, 2),
                salvarLivro("Domain-Driven Design", "Eric Evans", "9780321125217", 2003, 2),
                salvarLivro("Design Patterns", "Erich Gamma et al.", "9780201633610", 1994, 3),
                salvarLivro("Effective Java", "Joshua Bloch", "9780134685991", 2018, 2),
                salvarLivro("The Pragmatic Programmer", "David Thomas; Andrew Hunt", "9780135957059", 2019, 2)
        );
    }

    private List<Emprestimo> criarEmprestimosDevolvidos(List<Leitor> leitores, List<Livro> livros, LocalDate hoje) {
        return List.of(
                salvarEmprestimo(livros.get(0), leitores.get(0), hoje.minusDays(30), hoje.minusDays(25)),
                salvarEmprestimo(livros.get(1), leitores.get(1), hoje.minusDays(40), hoje.minusDays(30)),
                salvarEmprestimo(livros.get(2), leitores.get(3), hoje.minusDays(50), hoje.minusDays(31))
        );
    }

    private List<Emprestimo> criarEmprestimosAtivos(List<Leitor> leitores, List<Livro> livros, LocalDate hoje) {
        return List.of(
                salvarEmprestimo(livros.get(3), leitores.get(0), hoje.minusDays(2), null),
                salvarEmprestimo(livros.get(4), leitores.get(2), hoje.minusDays(3), null),
                salvarEmprestimo(livros.get(5), leitores.get(4), hoje.minusDays(5), null)
        );
    }

    private List<Emprestimo> criarEmprestimosAtrasados(List<Leitor> leitores, List<Livro> livros, LocalDate hoje) {
        return List.of(
                salvarEmprestimo(livros.get(0), leitores.get(1), hoje.minusDays(15), null),
                salvarEmprestimo(livros.get(2), leitores.get(1), hoje.minusDays(10), null),
                salvarEmprestimo(livros.get(1), leitores.get(3), hoje.minusDays(20), null)
        );
    }

    private Leitor salvarLeitor(String nome, String email, TipoPessoa tipo) {
        return this.leitorRepository.save(Leitor.builder()
                .nome(nome)
                .email(email)
                .tipoPessoa(tipo)
                .build());
    }

    private Livro salvarLivro(String titulo, String autor, String isbn, int ano, int quantidade) {
        return this.livroRepository.save(Livro.builder()
                .titulo(titulo)
                .autor(autor)
                .isbn(isbn)
                .anoPublicacao(ano)
                .quantidadeTotal(quantidade)
                .quantidadeDisponivel(quantidade)
                .build());
    }

    private Emprestimo salvarEmprestimo(Livro livro, Leitor leitor, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        RegraEmprestimoStrategy regra = this.regraEmprestimoResolver.para(leitor.getTipoPessoa());
        LocalDate dataPrevista = regra.calcularDataPrevistaDevolucao(dataEmprestimo);

        Emprestimo emprestimo = Emprestimo.builder()
                .livroId(livro.getId())
                .leitorId(leitor.getId())
                .dataEmprestimo(dataEmprestimo)
                .dataPrevistaDevolucao(dataPrevista)
                .dataDevolucao(dataDevolucao)
                .build();

        if (dataDevolucao == null) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
            this.livroRepository.update(livro);
        } else if (dataDevolucao.isAfter(dataPrevista)) {
            emprestimo.setMulta(regra.calcularMulta(dataPrevista, dataDevolucao));
        }

        return this.emprestimoRepository.save(emprestimo);
    }
}
