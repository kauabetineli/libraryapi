package io.github.kauabetineli.libraryapi.repository;

import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.model.GeneroLivro;
import io.github.kauabetineli.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("12313-123");
        livro.setPreco(BigDecimal.valueOf(100.22));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro novo livro");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = autorRepository
                .findById(UUID.fromString("7ecc6266-7af2-4797-b783-12cfdf0c98c8"))
                .orElse(null);

        livro.setAutor(autor);

        repository.save(livro);
    }


    // SE FOR FAZER ESTE TESTE, DESATIVE O CASCADE.ALL NA ENTIDADE LIVRO!
    @Test
    void salvarAutorLivroTest(){
        Livro livro = new Livro();
        livro.setIsbn("33333");
        livro.setPreco(BigDecimal.valueOf(100.22));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("AutorLivroTest");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("José");
        autor.setNacionalidade("brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.save(autor);

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void salvarCascadeTest(){
        Livro livro = new Livro();
        livro.setIsbn("33333");
        livro.setPreco(BigDecimal.valueOf(100.22));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Outro livro xDxD");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("João");
        autor.setNacionalidade("brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro(){

        UUID id = UUID.fromString("a8e23bb9-aef8-4409-98c8-946487a697d0");
        var livroParaAtualizar = repository
                .findById(id)
                .orElse(null);

        UUID idAutor = UUID.fromString("7ecc6266-7af2-4797-b783-12cfdf0c98c8");
        Autor maria = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(maria);

        repository.save(livroParaAtualizar);

    }

    // SE FOR FAZER ESTE TESTE, ATIVE O CASCADE.ALL NA ENTIDADE LIVRO!
    @Test
    void deletarCascade(){
        UUID id = UUID.fromString("e2b7bb45-990f-4274-a09c-63f2c1321833");
        repository.deleteById(id);
    }

    @Test
    @Transactional // janela de transacao (Fetch Lazy) -> permite buscar um livro sem fazer um outro select para o autor
    //e caso precise do autor, ele faz um select quando tiver essa necessidade
    void buscarLivroTest(){
        UUID id = UUID.fromString("3974873e-2bb7-4273-81d7-4fbacc265da1");
        Livro livro = repository.findById(id).orElse(null);

        System.out.println("Livro:");
        System.out.println(livro.getTitulo());
        System.out.println("Autor:");
        System.out.println(livro.getAutor().getNome());


    }



}