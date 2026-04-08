package br.edu.cs.poo.ac.bolsa.testes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.entidade.Ativo;
import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.StatusTitulo;
import br.edu.cs.poo.ac.bolsa.entidade.Titulo;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TituloTest {

    private InvestidorPessoa pessoa; 
    private InvestidorEmpresa empresa;
    private Ativo ativo = new Ativo(1010,"Ativo 1", 1000.0, 10000000.0, 0.1, 4.0, FaixaRenda.REGULAR, 120);

    @BeforeEach
    void setup() {
        pessoa = new InvestidorPessoa(
        		"MARCOS", null, null, BigDecimal.ZERO, null,
                        "12345678901", 30000.00, FaixaRenda.REGULAR);        		        		
        empresa = new InvestidorEmpresa(
        		"ACME", null, null,BigDecimal.ZERO, null,
                "12345678901234", 3000000.00);
        ativo = new Ativo(
        		123456, "C-BONDS",10000.00, 1000000000.00,
                0.10, 1.00,FaixaRenda.REGULAR, 24);
    }

    private Titulo criarTituloPadrao() {
        return new Titulo(
                pessoa,
                empresa,
                ativo,
                new BigDecimal("1000.00"),     // valorInvestido
                new BigDecimal("1000.00"),     // valorAtual
                new BigDecimal("1.0"),         // taxaDiaria (1% ao dia)
                LocalDate.now().minusDays(10), // dataAplicacao
                LocalDate.now().plusDays(10),  // dataVencimento
                null,                          // dataUltimoRendimento
                StatusTitulo.ATIVO             // status
        );
    }

    // ---------------------------------------------------------
    // TESTES DE BLOQUEIO
    // ---------------------------------------------------------

    @Test
    void deveRetornarFalseQuandoStatusNaoAtivo() {
        Titulo t = criarTituloPadrao();
        t.setStatus(StatusTitulo.CANCELADO);

        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoHojeNaoEhAntesDoVencimento() {
        Titulo t = criarTituloPadrao();
        t.setDataVencimento(LocalDate.now()); // hoje não é antes do vencimento

        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoHojeNaoEhDepoisDaAplicacao() {
        Titulo t = criarTituloPadrao();
        t.setDataAplicacao(LocalDate.now()); // hoje não é depois da aplicação

        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoUltimoRendimentoEhHoje() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now()); // hoje não é depois do último rendimento

        assertFalse(t.render());
    }

    @Test
    void deveRetornarFalseQuandoDiasCalculadosSaoZero() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now().minusDays(0)); // dias = 0

        assertFalse(t.render());
    }

    // ---------------------------------------------------------
    // TESTES DE CÁLCULO
    // ---------------------------------------------------------

    @Test
    void deveCalcularRendimentoCorretamenteQuandoPrimeiroRendimento() {
        Titulo t = criarTituloPadrao();

        BigDecimal valorAntes = t.getValorAtual();

        boolean resultado = t.render();

        assertTrue(resultado);
        assertTrue(t.getValorAtual().compareTo(valorAntes) > 0);
    }

    @Test
    void deveAtualizarDataUltimoRendimento() {
        Titulo t = criarTituloPadrao();

        t.render();

        assertEquals(LocalDate.now(), t.getDataUltimoRendimento());
    }

    @Test
    void deveCalcularRendimentoComUltimoRendimentoDefinido() {
        Titulo t = criarTituloPadrao();
        t.setDataUltimoRendimento(LocalDate.now().minusDays(5));

        BigDecimal valorAntes = t.getValorAtual();

        boolean resultado = t.render();

        assertTrue(resultado);
        assertTrue(t.getValorAtual().compareTo(valorAntes) > 0);
    }

    @Test
    void deveAplicarFormulaCorretamenteParaDiasEspecificos() {
        Titulo t = criarTituloPadrao();
        t.setValorAtual(new BigDecimal("1000.00"));
        t.setTaxaDiaria(new BigDecimal("1.0")); // 1% ao dia
        t.setDataUltimoRendimento(LocalDate.now().minusDays(3));

        t.render();

        // Fórmula esperada: 1000 * (1.01)^3
        BigDecimal esperado = new BigDecimal("1000")
                .multiply(new BigDecimal("1.01").pow(3));

        assertEquals(0, t.getValorAtual().compareTo(esperado));
    }

    @Test
    void testGetNumeroParaInvestidorPessoa() {

        // Mocks simples
        InvestidorPessoa pessoa = new InvestidorPessoa("MARCUS", null, null,
                BigDecimal.ZERO, null, null, 100000.0, FaixaRenda.DIFERENCIADA);
        pessoa.setCpf("12345678901");

        

        LocalDate dataAplicacao = LocalDate.of(2024, 3, 10);

        Titulo titulo = new Titulo(
                pessoa,
                null,
                ativo,
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.ONE,
                dataAplicacao,
                dataAplicacao.plusDays(30),
                null,
                StatusTitulo.ATIVO
        );

        String esperado = "000" +
                pessoa.getCpf() +
                ativo.getCodigo() +
                "202403100000";

        assertEquals(esperado, titulo.getNumero());
    }

    @Test
    void testGetNumeroParaInvestidorEmpresa() {

        // Mocks simples
        InvestidorEmpresa empresa = new InvestidorEmpresa("ACME", null, null,
                BigDecimal.ZERO, null, null, 100000.0);
        empresa.setCnpj("11222333444455");

        LocalDate dataAplicacao = LocalDate.of(2024, 5, 20);

        Titulo titulo = new Titulo(
                null,
                empresa,
                ativo,
                BigDecimal.TEN,
                BigDecimal.TEN,
                BigDecimal.ONE,
                dataAplicacao,
                dataAplicacao.plusDays(60),
                null,
                StatusTitulo.ATIVO
        );

        String esperado = empresa.getCnpj() +
                ativo.getCodigo() +
                "202405200000";

        assertEquals(esperado, titulo.getNumero());
    }
}