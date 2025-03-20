package io.github.kauabetineli.libraryapi.controller.mappers;

import io.github.kauabetineli.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.kauabetineli.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.kauabetineli.libraryapi.model.Livro;
import io.github.kauabetineli.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class) // com o USES, mapeia outras entidades se possivel, no caso do livro ele possui um autor, entao é chamado o AutorMapper para fazer o mapeamento de Autor para AutorDTO
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
