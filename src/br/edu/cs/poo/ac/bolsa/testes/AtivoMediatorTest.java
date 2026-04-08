package br.edu.cs.poo.ac.bolsa.testes;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.negocio.AtivoMediator;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;

public class AtivoMediatorTest extends TesteGenerico {

    private AtivoMediator mediator;
    private CadastroObjetos cadastro;

    @BeforeEach
    public void setup() {
        cadastro = new CadastroObjetos(Ativo.class);
        mediator = AtivoMediator.getInstancia();
        // apaga o diretório de ativos
        limparDiretorio("Ativo");
    }

    // -----------------------------
    // MÉTODO AUXILIAR PARA CRIAR ATIVOS VÁLIDOS
    // -----------------------------
    private Ativo criarAtivo(long codigo) {
        Ativo a = new Ativo();
        a.setCodigo(codigo);
        a.setDescricao("Ativo " + codigo);
        a.setValorMinimoAplicacao(100);
        a.setValorMaximoAplicacao(200);
        a.setTaxaMensalMinima(0.5);
        a.setTaxaMensalMaxima(1.0);
        a.setFaixaMinimaPermitida(FaixaRenda.REGULAR);
        a.setPrazoEmMeses(12);
        return a;
    }

    // -----------------------------
    // TESTES DE INCLUSÃO
    // -----------------------------
    @Test
    public void testIncluirComSucesso() {
        Ativo ativo = criarAtivo(1);

        MensagensValidacao msgs = mediator.incluir(ativo);

        assertTrue(msgs.estaVazio());
        assertNotNull(cadastro.buscar("1"));
    }

    @Test
    public void testIncluirAtivoJaExistente() {
        Ativo ativo = criarAtivo(1);
        cadastro.incluir(ativo, "1");

        MensagensValidacao msgs = mediator.incluir(ativo);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Ativo já existente."));
    }

    @Test
    public void testIncluirComErroDeValidacao() {
        Ativo ativo = criarAtivo(0); // código inválido

        MensagensValidacao msgs = mediator.incluir(ativo);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Código deve ser maior que zero."));
    }

    // -----------------------------
    // TESTES DE ALTERAÇÃO
    // -----------------------------
    @Test
    public void testAlterarComSucesso() {
        Ativo ativo = criarAtivo(1);
        cadastro.incluir(ativo, "1");

        ativo.setDescricao("Alterado");

        MensagensValidacao msgs = mediator.alterar(ativo);

        assertTrue(msgs.estaVazio());

        Ativo buscado = (Ativo) cadastro.buscar("1");
        assertEquals("Alterado", buscado.getDescricao());
    }

    @Test
    public void testAlterarAtivoNaoExistente() {
        Ativo ativo = criarAtivo(1);

        MensagensValidacao msgs = mediator.alterar(ativo);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Ativo não existente."));
    }

    @Test
    public void testAlterarComErroDeValidacao() {
        Ativo ativo = criarAtivo(1);
        ativo.setDescricao(""); // inválido

        MensagensValidacao msgs = mediator.alterar(ativo);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals(("Descrição é obrigatória.")));
    }

    // -----------------------------
    // TESTES DE EXCLUSÃO
    // -----------------------------
    @Test
    public void testExcluirComSucesso() {
        Ativo ativo = criarAtivo(1);
        cadastro.incluir(ativo, "1");

        MensagensValidacao msgs = mediator.excluir(1);

        assertTrue(msgs.estaVazio());
        assertNull(cadastro.buscar("1"));
    }

    @Test
    public void testExcluirCodigoInvalido() {
        MensagensValidacao msgs = mediator.excluir(0);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals(("Código deve ser maior que zero.")));
    }

    @Test
    public void testExcluirAtivoNaoExistente() {
        MensagensValidacao msgs = mediator.excluir(1);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Ativo não existente."));
    }

    // -----------------------------
    // TESTES DE BUSCA
    // -----------------------------
    @Test
    public void testBuscarComSucesso() {
        Ativo ativo = criarAtivo(1);
        cadastro.incluir(ativo, "1");

        Ativo buscado = mediator.buscar(1);

        assertNotNull(buscado);
        assertEquals(1, buscado.getCodigo());
    }

    @Test
    public void testBuscarCodigoInvalido() {
        assertNull(mediator.buscar(0));
    }

    @Test
    public void testBuscarNaoExistente() {
        assertNull(mediator.buscar(1));
    }
}