package io.github.kauabetineli.libraryapi.service;

import io.github.kauabetineli.libraryapi.controller.dto.AutorDTO;
import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository){
        this.autorRepository = autorRepository;
    }

    public Autor salvar(Autor autor){
        return autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id){
        return autorRepository.findById(id);
    }

}
