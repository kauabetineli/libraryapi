package io.github.kauabetineli.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "livro", schema = "public")
@Entity()
@Data //Getter, Getter, toString, equalsAndHashCode, requiredArgsConstructor
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2 /* 18,2 (18 posicoes, 2 decimais) */)
//    private Double preco;
    private BigDecimal preco;

    //tipo de relacionamento (varios livros um autor) (um autor tem varios livros)
    @ManyToOne(
//            cascade =  CascadeType.ALL,
                fetch = FetchType.LAZY
    )
    @JoinColumn(name = "id_autor") // maneira de referenciar chave estrangeira
    private Autor autor;
}
