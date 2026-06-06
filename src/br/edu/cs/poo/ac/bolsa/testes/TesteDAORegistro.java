package br.edu.cs.poo.ac.bolsa.testes;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.dao.DAOAtivo;
import br.edu.cs.poo.ac.bolsa.dao.DAORegistro;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.util.Registro;

public class TesteDAORegistro extends TesteGenerico {

    // ============================================================
    //  TESTES DA CLASSE ATIVO
    // ============================================================

    @Test
    public void testAtivoHerdaDeRegistro() {
        assertTrue(Registro.class.isAssignableFrom(Ativo.class));
    }

    @Test
    public void testRegistroEhAbstrata() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(Registro.class.getModifiers()));
    }

    @Test
    public void testGetIdentificadorEhAbstratoEmRegistro() throws Exception {
        var metodo = Registro.class.getDeclaredMethod("getIdentificador");
        assertTrue(java.lang.reflect.Modifier.isAbstract(metodo.getModifiers()));
    }

    @Test
    public void testGetIdentificadorDeAtivo() {
        Ativo ativo = new Ativo();
        ativo.setCodigo(123);
        assertEquals("123", ativo.getIdentificador());
    }

    // ============================================================
    //  TESTES DA CLASSE DAORegistro
    // ============================================================

    // Inner class arbitrária para testes
    public static class EntidadeFake extends Registro {
        private String id;
        public EntidadeFake(String id) { this.id = id; }
        @Override public String getIdentificador() { return id; }
    }

    public static class DAOFake extends DAORegistro {
        public DAOFake() { super(EntidadeFake.class); }
    }

    private DAOFake daoFake;

    @BeforeEach
    public void setup() {
        daoFake = new DAOFake();
        limparDiretorio("Ativo");
        limparDiretorio("EntidadeFake");
    }

    // ---------- buscar() ----------
    @Test
    public void testBuscarCaminhoFeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        daoFake.incluir(e);
        assertNotNull(daoFake.buscar("A1"));
    }

    @Test
    public void testBuscarCaminhoInfeliz() {
        assertNull(daoFake.buscar("X999"));
    }

    // ---------- incluir() ----------
    @Test
    public void testIncluirCaminhoFeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        assertTrue(daoFake.incluir(e));
    }

    @Test
    public void testIncluirCaminhoInfeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        daoFake.incluir(e);
        assertFalse(daoFake.incluir(e)); // já existe
    }

    // ---------- alterar() ----------
    @Test
    public void testAlterarCaminhoFeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        daoFake.incluir(e);
        assertTrue(daoFake.alterar(e));
    }

    @Test
    public void testAlterarCaminhoInfeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        assertFalse(daoFake.alterar(e)); // não existe
    }

    // ---------- excluir() ----------
    @Test
    public void testExcluirCaminhoFeliz() {
        EntidadeFake e = new EntidadeFake("A1");
        daoFake.incluir(e);
        assertTrue(daoFake.excluir("A1"));
    }

    @Test
    public void testExcluirCaminhoInfeliz() {
        assertFalse(daoFake.excluir("X999"));
    }

    // ============================================================
    //  TESTES DA CLASSE DAOAtivo
    // ============================================================

    private DAOAtivo daoAtivo;

    @BeforeEach
    public void setupAtivo() {
        daoAtivo = new DAOAtivo();
    }

    // ---------- buscar() ----------
    @Test
    public void testBuscarAtivoCaminhoFeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        daoAtivo.incluir(a);
        assertNotNull(daoAtivo.buscar(10));
    }

    @Test
    public void testBuscarAtivoCaminhoInfeliz() {
        assertNull(daoAtivo.buscar(999));
    }

    // ---------- incluir() ----------
    @Test
    public void testIncluirAtivoCaminhoFeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        assertTrue(daoAtivo.incluir(a));
    }

    @Test
    public void testIncluirAtivoCaminhoInfeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        daoAtivo.incluir(a);
        assertFalse(daoAtivo.incluir(a)); // duplicado
    }

    // ---------- alterar() ----------
    @Test
    public void testAlterarAtivoCaminhoFeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        daoAtivo.incluir(a);
        assertTrue(daoAtivo.alterar(a));
    }

    @Test
    public void testAlterarAtivoCaminhoInfeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        assertFalse(daoAtivo.alterar(a)); // não existe
    }

    // ---------- excluir() ----------
    @Test
    public void testExcluirAtivoCaminhoFeliz() {
        Ativo a = new Ativo();
        a.setCodigo(10);
        daoAtivo.incluir(a);
        assertTrue(daoAtivo.excluir(10));
    }

    @Test
    public void testExcluirAtivoCaminhoInfeliz() {
        assertFalse(daoAtivo.excluir(999));
    }
}