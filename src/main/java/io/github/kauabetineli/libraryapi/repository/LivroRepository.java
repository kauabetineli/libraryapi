package io.github.kauabetineli.libraryapi.repository;

import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    // Query Method

    List<Livro> findByAutor(Autor autor);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByIsbn(String isbn);


}
