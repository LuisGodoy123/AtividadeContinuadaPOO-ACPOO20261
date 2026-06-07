package br.edu.cs.poo.ac.bolsa.testes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import br.edu.cs.poo.ac.bolsa.dao.DAO;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoOobjetoNaoExistente;
import br.edu.cs.poo.ac.bolsa.util.Registro;

public class TesteDAO extends TesteGenerico {

    // -----------------------------
    // Inner class usada nos testes
    // -----------------------------
    private static class RegistroFake extends Registro {
        private String id;
        private String valor;

        public RegistroFake(String id, String valor) {
            this.id = id;
            this.valor = valor;
        }

        @Override
        public String getIdentificador() {
            return id;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String novoValor) {
            this.valor = novoValor;
        }
    }

    private DAO<RegistroFake> dao;

    @BeforeEach
    void setup() {
    	limparDiretorio("RegistroFake");
        // Cada teste usa uma classe diferente para isolar os arquivos gerados
        dao = new DAO<RegistroFake>(RegistroFake.class);
        
    }

    // -----------------------------
    // Testes do método incluir
    // -----------------------------
    @Test
    void incluir_deveIncluirQuandoNaoExiste() throws Exception {
        RegistroFake r = new RegistroFake("1", "A");

        assertDoesNotThrow(() -> dao.incluir(r));
        assertEquals("A", dao.buscar("1").getValor());
    }

    @Test
    void incluir_deveLancarExcecaoQuandoJaExiste() throws Exception {
        RegistroFake r1 = new RegistroFake("1", "A");
        RegistroFake r2 = new RegistroFake("1", "B");

        dao.incluir(r1);

        assertThrows(ExcecaoObjetoJaExistente.class, () -> dao.incluir(r2));
    }

    // -----------------------------
    // Testes do método alterar
    // -----------------------------
    @Test
    void alterar_deveAlterarQuandoExiste() throws Exception {
        RegistroFake r = new RegistroFake("1", "A");
        dao.incluir(r);

        r.setValor("B");
        dao.alterar(r);

        assertEquals("B", dao.buscar("1").getValor());
    }

    @Test
    void alterar_deveLancarExcecaoQuandoNaoExiste() {
        RegistroFake r = new RegistroFake("1", "A");

        assertThrows(ExcecaoOobjetoNaoExistente.class, () -> dao.alterar(r));
    }

    // -----------------------------
    // Testes do método excluir
    // -----------------------------
    @Test
    void excluir_deveExcluirQuandoExiste() throws Exception {
        RegistroFake r = new RegistroFake("1", "A");
        dao.incluir(r);

        dao.excluir("1");

        assertNull(dao.buscar("1"));
    }

    @Test
    void excluir_deveLancarExcecaoQuandoNaoExiste() {
        assertThrows(ExcecaoOobjetoNaoExistente.class, () -> dao.excluir("1"));
    }

    // -----------------------------
    // Testes do método buscar
    // -----------------------------
    @Test
    void buscar_deveRetornarObjetoQuandoExiste() throws Exception {
        RegistroFake r = new RegistroFake("1", "A");
        dao.incluir(r);

        RegistroFake encontrado = dao.buscar("1");

        assertNotNull(encontrado);
        assertEquals("A", encontrado.getValor());
    }

    @Test
    void buscar_deveRetornarNullQuandoNaoExiste() {
        assertNull(dao.buscar("1"));
    }

    // -----------------------------
    // Testes do método buscarTodos
    // -----------------------------
    @Test
    void buscarTodos_deveRetornarTodosOsRegistros() throws Exception {
        dao.incluir(new RegistroFake("1", "A"));
        dao.incluir(new RegistroFake("2", "B"));

        RegistroFake[] todos = dao.buscarTodos();

        assertEquals(2, todos.length);
    }
}