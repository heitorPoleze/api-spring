package edu.ifes.sistema_bibliotecario.controller;

import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoFiltrosDTO;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.emprestimo.EmprestimoResponseDTO;
import edu.ifes.sistema_bibliotecario.service.emprestimo.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> save(@RequestBody @Valid EmprestimoRequestDTO dto){
        EmprestimoResponseDTO emprestimoResponse = this.emprestimoService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(emprestimoResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(emprestimoResponse);
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> findByFiltros(EmprestimoFiltrosDTO filtros){
        return ResponseEntity.ok(this.emprestimoService.findByFiltros(filtros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponseDTO> findById(@PathVariable long id){
        return ResponseEntity.ok(this.emprestimoService.buscarPorId(id));
    }

    @PostMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoResponseDTO> devolver(@PathVariable long id){
        return ResponseEntity.ok(this.emprestimoService.devolver(id));
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<EmprestimoResponseDTO>> findAtrasados(){
        return ResponseEntity.ok(this.emprestimoService.buscarAtrasados());
    }
}
