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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    AutorRepository autorRepository;
    @Autowired
    LivroRepository livroRepository;

    @Test
    void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("12313-123");
        livro.setPreco(BigDecimal.valueOf(100.22));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setTitulo("Ciencias");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = autorRepository
                .findById(UUID.fromString("dca0d3c3-f4ad-45fa-bac1-1a583a4de9a8"))
                .orElse(null);

        livro.setAutor(autor);

        livroRepository.save(livro);
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

        livroRepository.save(livro);
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

        livroRepository.save(livro);
    }

    @Test
    void atualizarAutorDoLivro(){

        UUID id = UUID.fromString("a8e23bb9-aef8-4409-98c8-946487a697d0");
        var livroParaAtualizar = livroRepository
                .findById(id)
                .orElse(null);

        UUID idAutor = UUID.fromString("7ecc6266-7af2-4797-b783-12cfdf0c98c8");
        Autor maria = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(maria);

        livroRepository.save(livroParaAtualizar);

    }

    // SE FOR FAZER ESTE TESTE, ATIVE O CASCADE.ALL NA ENTIDADE LIVRO!
    @Test
    void deletarCascade(){
        UUID id = UUID.fromString("e2b7bb45-990f-4274-a09c-63f2c1321833");
        livroRepository.deleteById(id);
    }

    @Test
    @Transactional // janela de transacao (Fetch Lazy) -> permite buscar um livro sem fazer um outro select para o autor
    //e caso precise do autor, ele faz um select quando tiver essa necessidade
    void buscarLivroTest(){
        UUID id = UUID.fromString("3974873e-2bb7-4273-81d7-4fbacc265da1");
        Livro livro = livroRepository.findById(id).orElse(null);

        System.out.println("Livro:");
        System.out.println(livro.getTitulo());
        System.out.println("Autor:");
        System.out.println(livro.getAutor().getNome());

    }

    @Test
    void pesquisaPorTituloTest(){
        List<Livro> lista = livroRepository.findByTitulo("Roubo a casa assombrada");
        lista.forEach(System.out::println);
    }

    @Test
    void pesquisaPorIsbnTest(){
        Optional<Livro> livro = livroRepository.findByIsbn("9999-99");
        livro.ifPresent(System.out::println);
    }

    @Test
    void pesquisaPorTituloAndPrecoTest(){
        var preco = BigDecimal.valueOf(204.23);
        var tituloPesquisa = "Roubo a casa assombrada";
        List<Livro> lista = livroRepository.findByTituloAndPreco(tituloPesquisa, preco);
    }

    @Test
    void listarLivrosComQueryJPQLTest(){
        var resultado = livroRepository.listarTodosOrdenadoPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivrosTest(){
        var resultado = livroRepository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarTitulosNaoRepetidosDosLivrosTest(){
        var resultado = livroRepository.listarNomesDiferentesLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosDeLivrosAutoresBrasileirosTest(){
        var resultado = livroRepository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParamTest(){
        var resultado = livroRepository.findByGenero(GeneroLivro.MISTERIO, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest(){
        livroRepository.deleteByGenero(GeneroLivro.CIENCIA);
    }

    @Test
    void updateDataPublicacaoTest(){
        livroRepository.updateDataPublicacao(LocalDate.of(2000, 1, 1));
    }

}