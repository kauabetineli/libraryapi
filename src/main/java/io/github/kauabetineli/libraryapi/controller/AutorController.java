package io.github.kauabetineli.libraryapi.controller;

import io.github.kauabetineli.libraryapi.controller.dto.AutorDTO;
import io.github.kauabetineli.libraryapi.controller.dto.ErroResposta;
import io.github.kauabetineli.libraryapi.controller.mappers.AutorMapper;
import io.github.kauabetineli.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.kauabetineli.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
// http://localhost:8080/autores/
public class AutorController implements GenericController{

    private final AutorService service;
    private final AutorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto) { //@Valid faz a validacao na entrada

        try {
            Autor autor = mapper.toEntity(dto);
            service.salvar(autor);

// http://localhost:8080/autores/4ac5faac-04b8-4761-9025-444be9e9f3ef
//            URI location = ServletUriComponentsBuilder
//                    .fromCurrentRequest()
//                    .path("{id}")
//                    .buildAndExpand(autor.getId())
//                    .toUri();
            URI location = gerarHeaderLocation(autor.getId());

            return ResponseEntity.created(location).build();

        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        UUID idAutor = UUID.fromString(id);

        //Antes
//        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        return service
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build());

        //Antes
//        if (autorOptional.isPresent()) {
//            Autor autor = autorOptional.get();
//            AutorDTO dto = mapper.toDTO(autor);
//            return ResponseEntity.ok(dto);
//        }
//        return ResponseEntity.notFound().build();
    }

    //idempotente -> esquisar mais sobre
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {

        try {
            UUID idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.obterPorId(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            service.deletar(autorOptional.get());
            return ResponseEntity.noContent().build();

        } catch (OperacaoNaoPermitidaException e) {
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false /*nao é obrigatorio passar esse parametro */) String nacionalidade) {
        List<Autor> resultado = service.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {

        try {
            UUID idAutor = UUID.fromString(id);

            Optional<Autor> autorOptional = service.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();

            //nao utilizar o mapper pois se for mapear e atualizar esses 3 campos, o mapper vai atribuir nulo aos demais campos do Autor
            autor.setNacionalidade(autorDTO.nacionalidade());
            autor.setNome(autorDTO.nome());
            autor.setDataNascimento(autorDTO.dataNascimento());

            service.atualizar(autor);

            return ResponseEntity.noContent().build();

        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }


}
