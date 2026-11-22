package edu.ifes.sistema_bibliotecario.controller;

import edu.ifes.sistema_bibliotecario.domain.Livro;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroFiltrosDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroMapper;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.livro.LivroResponseDTO;
import edu.ifes.sistema_bibliotecario.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> save(@RequestBody @Valid LivroRequestDTO dto) {
        Livro livroSalvo = this.livroService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(livroSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(this.livroMapper.toResponse(livroSalvo));
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> findByFiltros(LivroFiltrosDTO filtros) {
        List<LivroResponseDTO> livros = this.livroService.findByFiltros(filtros).stream()
                .map(this.livroMapper::toResponse)
                .toList();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> findById(@PathVariable long id) {
        Livro livro = this.livroService.findById(id);
        return ResponseEntity.ok(this.livroMapper.toResponse(livro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> update(
            @PathVariable long id,
            @RequestBody @Valid LivroRequestDTO dto) {
        Livro livroAtualizado = this.livroService.update(id, dto);
        return ResponseEntity.ok(this.livroMapper.toResponse(livroAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        this.livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
