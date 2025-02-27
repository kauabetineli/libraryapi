package io.github.kauabetineli.libraryapi.service;

import io.github.kauabetineli.libraryapi.model.Autor;
import io.github.kauabetineli.libraryapi.model.GeneroLivro;
import io.github.kauabetineli.libraryapi.model.Livro;
import io.github.kauabetineli.libraryapi.repository.AutorRepository;
import io.github.kauabetineli.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void salvarLivroComFoto(){
        // salva o livro
        // repository.save(livro)

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucket na nuvem
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome arquivo que foi salvo
        // livro.setNomeArquivo(id + ".png");

    }

    @Transactional
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository
                .findById(UUID.fromString("3974873e-2bb7-4273-81d7-4fbacc265da1"))
                .orElse(null);
        livro.setDataPublicacao(LocalDate.of(2024,6,1));

        livroRepository.save(livro);
    }

    @Transactional
    public void executar(){

        // Salva o autor
        Autor autor = new Autor();
        autor.setNome("Teste Francisco");
        autor.setNacionalidade("brasileira");
        autor.setDataNascimento(LocalDate.of(1951, 1, 31));

        autorRepository.saveAndFlush(autor);

        // Salva o livro
        Livro livro = new Livro();
        livro.setIsbn("33333");
        livro.setPreco(BigDecimal.valueOf(100.22));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Teste Livro do Francisco");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        livro.setAutor(autor);

        livroRepository.saveAndFlush(livro); // envolve a questão de quando está em modo managed!

        if(autor.getNome().equals("Teste Francisco")){
            throw new RuntimeException("Rollback!");
        }

    }

}
