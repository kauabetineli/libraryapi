package io.github.kauabetineli.libraryapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "livro", schema = "public")
@Entity()
@Data //Getter, Getter, toString, equalsAndHashCode, requiredArgsConstructor
@ToString(exclude = "autor") //evitar loop infinito -> excluir a propriedade, colocar { } para array
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate // coloca a data hora atual no banco
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate // sempre que fazer um update, ele atualiza
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;

}
