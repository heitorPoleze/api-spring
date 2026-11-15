package edu.ifes.sistema_bibliotecario.controller;

import edu.ifes.sistema_bibliotecario.dto.livro.LivroFiltrosDTO;
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

    @PostMapping
    public ResponseEntity<LivroResponseDTO> save(@RequestBody @Valid LivroRequestDTO dto) {
        LivroResponseDTO livroSalvo = this.livroService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(livroSalvo.id())
                .toUri();

        return ResponseEntity.created(location).body(livroSalvo);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> findByFilters(LivroFiltrosDTO filtros) {
        List<LivroResponseDTO> livros = this.livroService.findByFilters(filtros);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> findById(@PathVariable long id) {
        LivroResponseDTO livro = this.livroService.findById(id);
        return ResponseEntity.ok(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> update(
            @PathVariable long id,
            @RequestBody @Valid LivroRequestDTO dto) {
        LivroResponseDTO livroAtualizado = this.livroService.update(id, dto);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        this.livroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}