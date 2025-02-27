package io.github.kauabetineli.libraryapi.controller;

import io.github.kauabetineli.libraryapi.controller.dto.AutorDTO;
import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.service.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
// http://localhost:8080/autores/
public class AutorController {


    private final AutorService autorService;

    public AutorController(AutorService autorService){
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO autor){
        Autor autorEntidade = autor.mapearParaAutor();
        autorService.salvar(autorEntidade);

// http://localhost:8080/autores/4ac5faac-04b8-4761-9025-444be9e9f3ef
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorEntidade.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if(autorOptional.isPresent()){
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    //idempotente -> esquisar mais sobre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id){
        UUID idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if(autorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        autorService.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

}
