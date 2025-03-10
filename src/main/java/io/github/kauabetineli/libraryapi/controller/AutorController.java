package io.github.kauabetineli.libraryapi.controller;

import io.github.kauabetineli.libraryapi.controller.dto.AutorDTO;
import io.github.kauabetineli.libraryapi.controller.dto.ErroResposta;
import io.github.kauabetineli.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.kauabetineli.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autor) { //@Valid faz a validacao na entrada

        try {
            Autor autorEntidade = autor.mapearParaAutor();
            autorService.salvar(autorEntidade);

// http://localhost:8080/autores/4ac5faac-04b8-4761-9025-444be9e9f3ef
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();


            return ResponseEntity.created(location).build();

        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    //idempotente -> esquisar mais sobre
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {

        try {
            UUID idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            autorService.deletar(autorOptional.get());
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
        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> lista = resultado
                .stream()
                .map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade())
                ).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {

        try {
            UUID idAutor = UUID.fromString(id);

            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();

            autor.setNacionalidade(autorDTO.nacionalidade());
            autor.setNome(autorDTO.nome());
            autor.setDataNascimento(autorDTO.dataNascimento());

            autorService.atualizar(autor);

            return ResponseEntity.noContent().build();

        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }


}
