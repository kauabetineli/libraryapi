package io.github.kauabetineli.libraryapi.repository;

import io.github.kauabetineli.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
