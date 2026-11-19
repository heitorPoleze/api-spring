package edu.ifes.sistema_bibliotecario.controller;

import edu.ifes.sistema_bibliotecario.domain.Leitor;
import edu.ifes.sistema_bibliotecario.domain.TipoPessoa;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorMapper;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorRequestDTO;
import edu.ifes.sistema_bibliotecario.dto.leitor.LeitorResponseDTO;
import edu.ifes.sistema_bibliotecario.service.leitor.LeitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/leitores")
@RequiredArgsConstructor
public class LeitorController {

    private final LeitorService leitorService;
    private final LeitorMapper leitorMapper;

    @PostMapping
    public ResponseEntity<LeitorResponseDTO> save(@RequestBody @Valid LeitorRequestDTO dto) {
        Leitor leitorSalvo = this.leitorService.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(leitorSalvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(this.leitorMapper.toResponse(leitorSalvo));
    }

    @GetMapping
    public ResponseEntity<List<LeitorResponseDTO>> findAll(@RequestParam(required = false) TipoPessoa tipo) {
        List<LeitorResponseDTO> leitores = this.leitorService.findAll(tipo).stream()
                .map(this.leitorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(leitores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeitorResponseDTO> findById(@PathVariable long id) {
        Leitor leitor = this.leitorService.findById(id);
        return ResponseEntity.ok(this.leitorMapper.toResponse(leitor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeitorResponseDTO> update(
            @PathVariable long id,
            @RequestBody @Valid LeitorRequestDTO dto) {
        Leitor leitorAtualizado = this.leitorService.update(id, dto);
        return ResponseEntity.ok(this.leitorMapper.toResponse(leitorAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        this.leitorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
