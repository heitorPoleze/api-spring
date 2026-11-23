package edu.ifes.sistema_bibliotecario.service.emprestimo;

import edu.ifes.sistema_bibliotecario.domain.Emprestimo;
import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoFiltrosDTO;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoMapper;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoResponseDTO;
import edu.ifes.sistema_bibliotecario.exception.NaoEncontradoException;
import edu.ifes.sistema_bibliotecario.exception.RegraDeNegocioVioladaException;
import edu.ifes.sistema_bibliotecario.repository.emprestimo.EmprestimoRepository;
import edu.ifes.sistema_bibliotecario.service.LivroService;
import edu.ifes.sistema_bibliotecario.service.emprestimo.strategy.RegraEmprestimoResolver;
import edu.ifes.sistema_bibliotecario.service.emprestimo.strategy.RegraEmprestimoStrategy;
import edu.ifes.sistema_bibliotecario.service.leitor.LeitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import edu.ifes.sistema_bibliotecario.config.clock.ClockConfig;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoMapper emprestimoMapper;
    private final LivroService livroService;
    private final LeitorService leitorService;
    private final RegraEmprestimoResolver regraEmprestimoResolver;
    private final ClockConfig clockConfig;

    public EmprestimoResponseDTO save(EmprestimoRequestDTO dto){
        Leitor leitor = this.leitorService.findById(dto.leitorId());
        RegraEmprestimoStrategy regra = this.regraEmprestimoResolver.para(leitor.getTipoPessoa());

        LocalDate hoje = this.clockConfig.getDataAtual();
        List<Emprestimo> listaEmprestimosAtrasadosLeitor = emprestimoRepository.findAtrasadosByLeitorId(leitor.getId(), hoje);
        if(!listaEmprestimosAtrasadosLeitor.isEmpty()){
            throw new RegraDeNegocioVioladaException("Leitor não pode ter mais um empréstimo, pois tem livros atrasados.");
        }
        List<Emprestimo> listaAtivosLeitor = emprestimoRepository.findAtivosByLeitorId(leitor.getId());
        if(listaAtivosLeitor.size() >= regra.limiteAtivos()){
            throw new RegraDeNegocioVioladaException("Leitor já ultrapassou o limite de empréstimos ativos (" + regra.limiteAtivos() + ").");
        }

        Livro livro = livroService.findById(dto.livroId());
        if (!livro.isDisponivel()) {
            throw new RegraDeNegocioVioladaException("Livro sem exemplares disponíveis.");
        }

        Emprestimo emprestimo = emprestimoMapper.toDomain(dto);
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(regra.calcularDataPrevistaDevolucao(hoje));

        this.livroService.diminuirQtdDisponivel(livro);
        Emprestimo emprestimoSalvo = this.emprestimoRepository.save(emprestimo);

        return emprestimoMapper.toResponse(emprestimoSalvo, livro, leitor);
    }

    public List<EmprestimoResponseDTO> findByFiltros(EmprestimoFiltrosDTO filtros){
        return this.emprestimoRepository.findAll(filtros.toEspecificacao()).stream()
                .map(emprestimo -> mapLivroELeitorPorEmprestimo(emprestimo, null, null))
                .toList();
    }

    public EmprestimoResponseDTO buscarPorId(long id){
        Emprestimo emprestimo = this.emprestimoRepository.findById(id)
                .orElseThrow(()-> new NaoEncontradoException("Empréstimo não encontrado."));
        return this.mapLivroELeitorPorEmprestimo(emprestimo, null, null);
    }

    public List<EmprestimoResponseDTO> buscarAtrasados(){
        LocalDate hoje = this.clockConfig.getDataAtual();
        List<Emprestimo> atrasados = this.emprestimoRepository.findAtrasadosAPartirDe(hoje);
        return atrasados.stream()
                .map(emprestimo -> mapLivroELeitorPorEmprestimo(emprestimo, null, null))
                .toList();
    }

    public EmprestimoResponseDTO devolver(long id){
        LocalDate hoje = this.clockConfig.getDataAtual();

        Emprestimo emprestimo = this.emprestimoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Empréstimo não encontrado."));

        Leitor leitorQueEmprestou = this.leitorService.findById(emprestimo.getLeitorId());

        emprestimo.setDataDevolucao(hoje);

        if(emprestimo.isAtrasado(hoje)){
            BigDecimal valorMulta = this.regraEmprestimoResolver
                    .para(leitorQueEmprestou.getTipoPessoa())
                    .calcularMulta(emprestimo.getDataPrevistaDevolucao(), hoje);
            emprestimo.setMulta(valorMulta);
        }
        Livro livro = this.livroService.findById(emprestimo.getLivroId());
        this.livroService.aumentarQtdDisponivel(livro);
        return mapLivroELeitorPorEmprestimo(emprestimo, null, leitorQueEmprestou);
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
