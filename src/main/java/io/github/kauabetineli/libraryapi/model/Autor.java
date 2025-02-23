package io.github.kauabetineli.libraryapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autor", schema = "public")
@Getter
@Setter
@ToString(exclude = "livros") //evitar loop infinito
@EntityListeners(AuditingEntityListener.class) //verifica quando tem alteracao na entidade
public class Autor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length = 100 /*varchar(100)*/, nullable = false /* not null */)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade", length = 50, nullable = false)
    private String nacionalidade;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //como esta mapeado na entidade Livro
    private List<Livro> livros;

    @CreatedDate // coloca a data hora atual no banco
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate // sempre que fazer um update, ele atualiza
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;


}
