package io.github.kauabetineli.libraryapi.controller.dto;

import io.github.kauabetineli.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "campo obrigatorio") //nao vir nula e tambem nao vir vazia, pois uma string pode ser = ' '
        @Size(min = 2, max = 100, message = "campo fora de tamanho padrao")
        String nome,
        @NotNull(message = "campo obrigatorio")
        @Past(message = "nao pode ser uma data futura")
        LocalDate dataNascimento,
        @NotBlank(message = "campo obrigatorio")
        @Size(max = 50, min = 2, message = "campo fora de tamanho padrao")
        String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }

}
