package br.edu.cs.poo.ac.bolsa.testes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.negocio.InvestidorMediator;
import br.edu.cs.poo.ac.bolsa.util.MensagensValidacao;

import java.io.File;
import java.time.LocalDate;


public class InvestidorMediatorTest extends TesteGenerico {

    private InvestidorMediator mediator;

    private CadastroObjetos cadEmp;
    private CadastroObjetos cadPes;

    @BeforeEach
    void setup() {

        cadEmp = new CadastroObjetos(InvestidorEmpresa.class);
        cadPes = new CadastroObjetos(InvestidorPessoa.class);
        mediator = new InvestidorMediator();
        // apaga os diretórios de investidores
        limparDiretorio("InvestidorPessoa");
        limparDiretorio("InvestidorEmpresa");
    }

    // ---------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ---------------------------------------------------------

    private Endereco criarEndereco() {
        Endereco e = new Endereco(); 
        e.setLogradouro("Rua A");
        e.setNumero("100");
        e.setCidade("Recife");
        e.setEstado("PE");
        e.setPais("Brasil");
        e.setCep("50000-000");
        return e;
    }

    private Contatos criarContatosPJ() {
        Contatos c = new Contatos();
        c.setEmail("empresa@teste.com");
        c.setTelefoneFixo("8133334444");
        c.setNomeParaContato("João");
        return c;
    }

    private Contatos criarContatosPF() {
        Contatos c = new Contatos();
        c.setEmail("pessoa@teste.com");
        c.setTelefoneCelular("8199998888");
        return c;
    }

    private InvestidorEmpresa criarEmpresa(String cnpj) {
        InvestidorEmpresa ie = new InvestidorEmpresa();
        ie.setCnpj(cnpj);
        ie.setNome("Empresa X");
        ie.setEndereco(criarEndereco());
        ie.setDataAbertura(LocalDate.now());        
        ie.setContatos(criarContatosPJ());
        ie.setFaturamento(200000.0);
        return ie;
    }

    private InvestidorPessoa criarPessoa(String cpf) {
        InvestidorPessoa ip = new InvestidorPessoa();
        ip.setCpf(cpf);
        ip.setNome("Fulano");
        ip.setEndereco(criarEndereco());
        ip.setDataNascimento(LocalDate.now());        
        ip.setContatos(criarContatosPF());
        ip.setRenda(20000.0);
        return ip;
    }

    // ---------------------------------------------------------
    // TESTES — INCLUIR / ALTERAR / EXCLUIR / BUSCAR — EMPRESA
    // ---------------------------------------------------------

    @Test
    void incluirEmpresaComSucesso() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertTrue(msgs.estaVazio());
        assertNotNull(cadEmp.buscar("51121160000120"));
    }

    @Test
    void incluirEmpresaJaExistente() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        cadEmp.incluir(ie, "51121160000120");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Empresa já existente."));
    }

    @Test
    void alterarEmpresaComSucesso() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        cadEmp.incluir(ie, "51121160000120");

        ie.setNome("Empresa Alterada");

        MensagensValidacao msgs = mediator.alterarInvestidorEmpresa(ie);

        assertTrue(msgs.estaVazio());
        assertEquals("Empresa Alterada",
                ((InvestidorEmpresa) cadEmp.buscar("51121160000120")).getNome());
    }

    @Test
    void alterarEmpresaNaoExistente() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");

        MensagensValidacao msgs = mediator.alterarInvestidorEmpresa(ie);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Empresa não existente."));
    }

    @Test
    void excluirEmpresaComSucesso() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        cadEmp.incluir(ie, "51121160000120");

        MensagensValidacao msgs = mediator.excluirInvestidorEmpresa("51121160000120");

        assertTrue(msgs.estaVazio());
        assertNull(cadEmp.buscar("51121160000120"));
    }

    @Test
    void excluirEmpresaNaoExistente() {
        MensagensValidacao msgs = mediator.excluirInvestidorEmpresa("51121160000120");

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Empresa não existente."));
    }

    @Test
    void buscarEmpresaComSucesso() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        cadEmp.incluir(ie, "51121160000120");

        assertNotNull(mediator.buscarInvestidorEmpresa("51121160000120"));
    }

    @Test
    void buscarEmpresaInvalida() {
        assertNull(mediator.buscarInvestidorEmpresa("111"));
    }

    // ---------------------------------------------------------
    // TESTES — INCLUIR / ALTERAR / EXCLUIR / BUSCAR — PESSOA
    // ---------------------------------------------------------

    @Test
    void incluirPessoaComSucesso() {
        InvestidorPessoa ip = criarPessoa("80052380610");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.estaVazio());
        assertNotNull(cadPes.buscar("80052380610"));
    }

    @Test
    void incluirPessoaJaExistente() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        cadPes.incluir(ip, "80052380610");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Pessoa já existente."));
    }

    @Test
    void alterarPessoaComSucesso() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        cadPes.incluir(ip, "80052380610");

        ip.setNome("Fulano Alterado");

        MensagensValidacao msgs = mediator.alterarInvestidorPessoa(ip);

        assertTrue(msgs.estaVazio());
        assertEquals("Fulano Alterado",
                ((InvestidorPessoa) cadPes.buscar("80052380610")).getNome());
    }

    @Test
    void alterarPessoaNaoExistente() {
        InvestidorPessoa ip = criarPessoa("80052380610");

        MensagensValidacao msgs = mediator.alterarInvestidorPessoa(ip);

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Pessoa não existente."));
    }

    @Test
    void excluirPessoaComSucesso() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        cadPes.incluir(ip, "80052380610");

        MensagensValidacao msgs = mediator.excluirInvestidorPessoa("80052380610");

        assertTrue(msgs.estaVazio());
        assertNull(cadPes.buscar("80052380610"));
    }

    @Test
    void excluirPessoaNaoExistente() {
        MensagensValidacao msgs = mediator.excluirInvestidorPessoa("80052380610");

        assertFalse(msgs.estaVazio());
        assertTrue(msgs.getMensagens()[0].equals("Investidor Pessoa não existente."));
    }

    @Test
    void buscarPessoaComSucesso() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        cadPes.incluir(ip, "80052380610");

        assertNotNull(mediator.buscarInvestidorPessoa("80052380610"));
    }

    @Test
    void buscarPessoaInvalida() {
        assertNull(mediator.buscarInvestidorPessoa("111"));
    }

    // ---------------------------------------------------------
    // TESTES DE VALIDAÇÃO — ENDEREÇO
    // ---------------------------------------------------------

    @Test
    void enderecoSemLogradouro() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        ip.getEndereco().setLogradouro("");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.getMensagens()[0].equals("Logradouro é obrigatório."));
    }

    @Test
    void enderecoSemNumero() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        ie.getEndereco().setNumero("");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertTrue(msgs.getMensagens()[0].equals("Número é obrigatório."));
    }

    @Test
    void enderecoSemCidade() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        ip.getEndereco().setCidade("");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.getMensagens()[0].equals("Cidade é obrigatório."));
    }

    @Test
    void enderecoSemEstado() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        ie.getEndereco().setEstado("");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertTrue(msgs.getMensagens()[0].equals("Estado é obrigatório."));
    }

    @Test
    void enderecoSemPais() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        ip.getEndereco().setPais("");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.getMensagens()[0].equals("País é obrigatório."));
    }

    // ---------------------------------------------------------
    // TESTES DE VALIDAÇÃO — CONTATOS
    // ---------------------------------------------------------

    @Test
    void contatosEmailInvalido() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        ip.getContatos().setEmail("email-invalido");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.getMensagens()[0].equals("E-mail inválido."));
    }

    @Test
    void contatosSemTelefone() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        ie.getContatos().setTelefoneFixo("");
        ie.getContatos().setTelefoneCelular("");
        ie.getContatos().setNumeroWhatsApp("");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertTrue(msgs.getMensagens()[0].equals("Pelo menos um telefone deve ser informado."));
    }

    @Test
    void contatosTelefoneComLetras() {
        InvestidorPessoa ip = criarPessoa("80052380610");
        ip.getContatos().setTelefoneCelular("81ABC123");

        MensagensValidacao msgs = mediator.incluirInvestidorPessoa(ip);

        assertTrue(msgs.getMensagens()[0].equals("Telefone celular deve conter apenas números."));
    }

    @Test
    void contatosNomeParaContatoObrigatorioPJ() {
        InvestidorEmpresa ie = criarEmpresa("51121160000120");
        ie.getContatos().setNomeParaContato("");

        MensagensValidacao msgs = mediator.incluirInvestidorEmpresa(ie);

        assertTrue(msgs.getMensagens()[0].equals("Nome para contato é obrigatório para pessoa jurídica."));
    }
}