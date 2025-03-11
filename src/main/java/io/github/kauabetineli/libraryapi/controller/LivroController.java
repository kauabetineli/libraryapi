package io.github.kauabetineli.libraryapi.controller;

import io.github.kauabetineli.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.kauabetineli.libraryapi.controller.dto.ErroResposta;
import io.github.kauabetineli.libraryapi.controller.mappers.LivroMapper;
import io.github.kauabetineli.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.kauabetineli.libraryapi.model.Livro;
import io.github.kauabetineli.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto){

        try {
            Livro livro = mapper.toEntity(dto);
            service.salvar(livro);
            return ResponseEntity.ok(livro);
        } catch (RegistroDuplicadoException e){
            ErroResposta erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }

}
