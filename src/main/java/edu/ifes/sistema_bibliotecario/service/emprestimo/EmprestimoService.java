package edu.ifes.sistema_bibliotecario.service.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoMapper;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoResponseDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.emprestimo.EmprestimoRepository;
import edu.ifes.sistema_bibliotecario.service.LivroService;
import edu.ifes.sistema_bibliotecario.service.leitor.LeitorFactory;
import edu.ifes.sistema_bibliotecario.service.leitor.LeitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoMapper emprestimoMapper;
    private final LivroService livroService;
    private final LeitorService leitorService;
    private final LeitorFactory leitorFactory;
    private final Clock clock;

    public EmprestimoResponseDTO save(EmprestimoRequestDTO dto){
        Leitor leitor = this.leitorService.findById(dto.leitorId());
        leitor = leitorFactory.leitorComValoresDeRegraEmprestimo(leitor);

        LocalDate hoje = getDataDeHoje();
        List<Emprestimo> listaEmprestimosAtrasadosLeitor = emprestimoRepository.findAtrasadosByLeitorId(leitor.getId(), hoje);
        if(!listaEmprestimosAtrasadosLeitor.isEmpty()){
            throw new RegraDeNegocioVioladaException("Leitor não pode ter mais um empréstimo, pois tem livros atrasados.");
        }
        List<Emprestimo> listaAtivosLeitor = emprestimoRepository.findAtivosByLeitorId(leitor.getId());
        if(listaAtivosLeitor.size() >= leitor.getLimiteAtivos()){
            throw new RegraDeNegocioVioladaException("Leitor já ultrapassou o limite de empréstimos ativos (" + leitor.getLimiteAtivos() + ").");
        }

        Livro livro = livroService.findById(dto.livroId());
        if (!livro.isDisponivel()) {
            throw new RegraDeNegocioVioladaException("Livro sem exemplares disponíveis.");
        }

        Emprestimo emprestimo = emprestimoMapper.toDomain(dto);
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(hoje.plusDays(leitor.getPrazoParaRetorno()));

        this.livroService.diminuirQtdDisponivel(livro);
        Emprestimo emprestimoSalvo = this.emprestimoRepository.save(emprestimo);

        return emprestimoMapper.toResponse(emprestimoSalvo, livro, leitor);
    }

    public List<EmprestimoResponseDTO> listarTodosPorFiltro(){}

    public EmprestimoResponseDTO buscarPorId(long id){
        Emprestimo emprestimo = this.emprestimoRepository.findById(id)
                .orElseThrow(()-> new NaoEncontradoException("Livro não encontrado."));
        return this.mapLivroELeitorPorEmprestimo(emprestimo);
    }

    public List<EmprestimoResponseDTO> buscarAtrasados(){
        LocalDate hoje = getDataDeHoje();
        List<Emprestimo> atrasados = this.emprestimoRepository.findAtrasadosAPartirDe(hoje);
        return atrasados.stream()
                .map(emprestimo -> mapLivroELeitorPorEmprestimo(emprestimo, null, null))
                .toList();
    }

    public EmprestimoResponseDTO devolver(long id){
        LocalDate hoje = getDataDeHoje();

        Emprestimo emprestimo = this.emprestimoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Empréstimo não encontrado."));

        Leitor leitorQueEmprestou = this.leitorService.findById(emprestimo.getLeitorId());

        emprestimo.setDataDevolucao(hoje);

        if(emprestimo.isAtrasado(hoje)){
            BigDecimal valorMulta = this.calcularValorMulta(emprestimo.getDataPrevistaDevolucao(), hoje, leitorQueEmprestou.getValorMultaDiario());
            emprestimo.setMulta(valorMulta);
        }

        return mapLivroELeitorPorEmprestimo(emprestimo, null, leitorQueEmprestou);
    }

    public boolean isLivroEmprestado(long idLivro){
        return !this.emprestimoRepository.findAtivosByLivroId(idLivro).isEmpty();
    }

    public boolean isLeitorComEmprestimoAtivo(long idLeitor){
        return !this.emprestimoRepository.findAtivosByLeitorId(idLeitor).isEmpty();
    }

    private BigDecimal calcularValorMulta(LocalDate dataPrevista, LocalDate dataDevolucao, BigDecimal valorMultaDiario) {
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        return valorMultaDiario.multiply(BigDecimal.valueOf(diasAtraso));
    }

    private LocalDate getDataDeHoje(){
        return LocalDate.now(this.clock);
    }

    private EmprestimoResponseDTO mapLivroELeitorPorEmprestimo(Emprestimo emprestimo, Livro livro, Leitor leitor){
        if(livro == null){
            livro = this.livroService.findById(emprestimo.getLivroId());
        }
        if(leitor == null){
            leitor = this.leitorService.findById(emprestimo.getLeitorId());
        }
        return emprestimoMapper.toResponse(emprestimo, livro, leitor);
    }
}
