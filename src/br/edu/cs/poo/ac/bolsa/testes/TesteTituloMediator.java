package br.edu.cs.poo.ac.bolsa.testes;

import org.junit.jupiter.api.*;

import br.edu.cs.poo.ac.bolsa.dao.DAO;
import br.edu.cs.poo.ac.bolsa.dao.DAOAtivo;
import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.StatusTitulo;
import br.edu.cs.poo.ac.bolsa.entidade.Titulo;
import br.edu.cs.poo.ac.bolsa.negocio.AtivoMediator;
import br.edu.cs.poo.ac.bolsa.negocio.DadosTitulo;
import br.edu.cs.poo.ac.bolsa.negocio.InvestidorMediator;
import br.edu.cs.poo.ac.bolsa.negocio.TituloMediator;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoNegocio;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TesteTituloMediator extends TesteGenerico {

    private TituloMediator mediator;
    private DAO<Titulo> dao;
    private DAOAtivo daoAtivo = new DAOAtivo();
    private DAOInvestidorPessoa daoInvPes = new DAOInvestidorPessoa();
    private InvestidorMediator investidorMediator;
    private AtivoMediator ativoMediator;
    private Endereco endereco;
    private Contatos contatos;

    @BeforeEach
    void setup() throws Exception {
    	limparDiretorio("Ativo");
    	limparDiretorio("InvestidorEmpresa");
    	limparDiretorio("InvestidorPessoa");
    	limparDiretorio("Titulo");
        dao = new DAO<Titulo>(Titulo.class);
        ativoMediator = new AtivoMediator();
        investidorMediator = new InvestidorMediator();

        mediator = TituloMediator.getInstancia();

        setField(mediator, "daoTitulo", dao);
        setField(mediator, "ativoMediator", ativoMediator);
        setField(mediator, "investidorMediator", investidorMediator);

        endereco = new Endereco("XXX", "YYY", "ZZZ", "WWW", "AAA", "BBB", "VCC");
        contatos = new Contatos("81999999999", "81988888888", "81977777777", "email@teste.com", "Contato");
    }

    private void setField(Object obj, String field, Object value) throws Exception {
        var f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, value);
    }

    // -------------------------------------------------------------
    // MÉTODO INCLUIR — CAMINHO FELIZ
    // -------------------------------------------------------------

    @Test
    void incluir_caminhoFeliz() {
        Ativo ativo = new Ativo( 
                10, "Ativo XPTO",
                500, 5000,
                0, 100,
                FaixaRenda.REGULAR,
                12
        );
        daoAtivo.incluir(ativo);

        InvestidorPessoa inv = new InvestidorPessoa(
                "João", endereco, LocalDate.now().minusYears(30),
                BigDecimal.ZERO, contatos,
                "80052380610", 30000, FaixaRenda.REGULAR
        );
        daoInvPes.incluirInvestidorPessoa(inv);

        DadosTitulo dados = new DadosTitulo(
                "80052380610",
                10,
                new BigDecimal("1000"),
                new BigDecimal("0.5")
        );

        assertDoesNotThrow(() -> mediator.incluir(dados));

        assertEquals(1, dao.buscarTodos().length);
    }

    // -------------------------------------------------------------
    // MÉTODO INCLUIR — VALIDAÇÕES
    // -------------------------------------------------------------

    @Test
    void incluir_cpfInvalido() {
        DadosTitulo dados = new DadosTitulo(" ", 10,
                new BigDecimal("1000"), new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("CPF/CNPJ inválido"));
    }

    @Test
    void incluir_codigoAtivoInvalido() {
        DadosTitulo dados = new DadosTitulo("80052380610", 0,
                new BigDecimal("1000"), new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Código do ativo inválido"));
    }

    @Test
    void incluir_valorInvestidoNulo() {
        DadosTitulo dados = new DadosTitulo("80052380610", 10,
                null, new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Valor investido não pode ser nulo"));
    }

    @Test
    void incluir_taxaDiariaNula() {
        DadosTitulo dados = new DadosTitulo("80052380610", 10,
                new BigDecimal("1000"), null);

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Taxa diária não pode ser nula"));
    }

    @Test
    void incluir_ativoNaoEncontrado() {
        DadosTitulo dados = new DadosTitulo("80052380610", 10,
                new BigDecimal("1000"), new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Ativo não encontrado"));
    }

    @Test
    void incluir_investidorNaoEncontrado() {
        daoAtivo.incluir(new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        ));

        DadosTitulo dados = new DadosTitulo("80052380610", 10,
                new BigDecimal("1000"), new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Investidor não encontrado"));
    }

    @Test
    void incluir_valorInvestidoForaFaixa() {
        daoAtivo.incluir(new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        ));

        daoInvPes.incluirInvestidorPessoa(new InvestidorPessoa(
                "João", endereco, LocalDate.now().minusYears(30),
                BigDecimal.ZERO, contatos,
                "80052380610", 30000, FaixaRenda.REGULAR
        ));

        DadosTitulo dados = new DadosTitulo("80052380610", 10,
                new BigDecimal("100"), new BigDecimal("0.5"));

        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.incluir(dados));

        assertTrue(ex.getMensagens().contem("Valor investido fora da faixa permitida"));
    }

    // -------------------------------------------------------------
    // MÉTODO CANCELAR TÍTULO
    // -------------------------------------------------------------

    @Test
    void cancelarTitulo_caminhoFeliz() {
        Ativo ativo = new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        );
        daoAtivo.incluir(ativo);

        InvestidorPessoa inv = new InvestidorPessoa(
                "João", endereco, LocalDate.now().minusYears(30),
                new BigDecimal("100.00"), criarContatosPF(),
                "80052380610", 30000, FaixaRenda.REGULAR
        );
        daoInvPes.incluirInvestidorPessoa(inv);

        Titulo titulo = new Titulo(inv, ativo,
                new BigDecimal("1000.00"), new BigDecimal("1000.00"),
                new BigDecimal("0.50"),
                LocalDate.now(), LocalDate.now().plusMonths(12),
                null, StatusTitulo.ATIVO);
        try {
        	dao.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
        	fail("Titulo ja existente");
        }

        assertDoesNotThrow(() -> mediator.cancelarTitulo(titulo.getIdentificador()));

        assertEquals(StatusTitulo.CANCELADO,
                dao.buscar(titulo.getIdentificador()).getStatus());
        inv = daoInvPes.buscarInvestidorPessoa(inv.getCpf());
        BigDecimal b1 = new BigDecimal("30.00");
        BigDecimal b2 = inv.getBonus();
        assertEquals(b1.setScale(2), b2.setScale(2));
    }

    @Test
    void cancelarTitulo_tituloNaoEncontrado() {
        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.cancelarTitulo("999"));

        assertTrue(ex.getMensagens().contem("Título não encontrado"));
    }

    @Test
    void cancelarTitulo_tituloNaoPodeSerCancelado() {
        InvestidorPessoa inv = new InvestidorPessoa(
                "João", endereco, LocalDate.now().minusYears(30),
                BigDecimal.ZERO, contatos,
                "80052380610", 30000, FaixaRenda.REGULAR
        );
        daoInvPes.incluirInvestidorPessoa(inv);

        Ativo ativo = new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        );
        daoAtivo.incluir(ativo);

        Titulo titulo = new Titulo(inv, ativo,
                new BigDecimal("1000"), new BigDecimal("1000"),
                new BigDecimal("0.5"),
                LocalDate.now(), LocalDate.now().plusMonths(12),
                null, StatusTitulo.VENCIDO);

        try {
        	dao.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
        	fail("Titulo ja existente");
        }


        ExcecaoNegocio ex = assertThrows(ExcecaoNegocio.class,
                () -> mediator.cancelarTitulo(titulo.getIdentificador()));

        assertTrue(ex.getMensagens().contem("Título não pode ser cancelado"));
    }

    // -------------------------------------------------------------
    // MÉTODO PROCESSAR RENDIMENTOS
    // -------------------------------------------------------------

    @Test
    void processarRendimentos_semTitulos() {
        assertDoesNotThrow(() -> mediator.processarRendimentos());
    }

    @Test
    void processarRendimentos_tituloNaoRende() {
        Ativo ativo = new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        );
        daoAtivo.incluir(ativo);

        InvestidorPessoa inv = new InvestidorPessoa(
                "João", endereco, LocalDate.now().minusYears(30),
                BigDecimal.ZERO, contatos,
                "80052380610", 30000, FaixaRenda.REGULAR
        );
        daoInvPes.incluirInvestidorPessoa(inv);

        Titulo titulo = new Titulo(inv, ativo,
                new BigDecimal("1000"), new BigDecimal("1000"),
                new BigDecimal("0.5"),
                LocalDate.now(), LocalDate.now().plusMonths(12),
                null, StatusTitulo.ATIVO);

        try {
        	dao.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
        	fail("Titulo ja existente");
        }


        mediator.processarRendimentos();

        assertEquals(new BigDecimal("0"), inv.getBonus());
    }

    @Test
    void processarRendimentos_tituloRendeEBonus() {
        Ativo ativo = new Ativo(
                10, "Ativo", 500, 5000,
                0, 100, FaixaRenda.REGULAR, 12
        );
        daoAtivo.incluir(ativo);

//        InvestidorPessoa inv = new InvestidorPessoa(
//                "João", endereco, LocalDate.now().minusYears(30),
//                BigDecimal.ZERO, contatos,
//                "80052380610", 30000, FaixaRenda.REGULAR
//        );
        InvestidorPessoa inv = new InvestidorPessoa();
        inv.setCpf("80052380610");
        inv.setNome("Fulano");
        inv.setEndereco(endereco);
        inv.setDataNascimento(LocalDate.now());        
        inv.setContatos(criarContatosPF());
        inv.setRenda(1000000.0);
        daoInvPes.incluirInvestidorPessoa(inv);

        Titulo titulo = new Titulo(inv, ativo,
                new BigDecimal("10000000"), new BigDecimal("10000000"),
                new BigDecimal("1.0"),
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(2),
                null, StatusTitulo.ATIVO);
        try {
        	dao.incluir(titulo);
        } catch (ExcecaoObjetoJaExistente e) {
        	fail("Titulo ja existente");
        }

        mediator.processarRendimentos();
        titulo = dao.buscar(titulo.getNumero());
        inv = daoInvPes.buscarInvestidorPessoa(inv.getCpf());
        assertEquals(StatusTitulo.ATIVO, titulo.getStatus());
        assertTrue(inv.getBonus().compareTo(BigDecimal.ZERO) > 0);
    }

    private Contatos criarContatosPF() {
        Contatos c = new Contatos();
        c.setEmail("pessoa@teste.com");
        c.setTelefoneCelular("8199998888");
        return c;
    }
}