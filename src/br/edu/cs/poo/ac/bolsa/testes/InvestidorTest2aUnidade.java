package br.edu.cs.poo.ac.bolsa.testes;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.entidade.Contatos;
import br.edu.cs.poo.ac.bolsa.entidade.Endereco;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.Investidor;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class InvestidorTest2aUnidade {

    // Objetos dummy para permitir instanciar InvestidorEmpresa
    static class EnderecoDummy extends Endereco{}
    static class ContatosDummy extends Contatos{}

    @Test
    void getIdentificador_deveRetornarCpf() {
        InvestidorPessoa pessoa = new InvestidorPessoa(
                "João Silva",
                new EnderecoDummy(),
                LocalDate.of(1990, 5, 10),
                BigDecimal.ZERO,
                new ContatosDummy(),
                "123.456.789-00",
                4500.0,
                FaixaRenda.PREMIUM
        );

        assertEquals("123.456.789-00", pessoa.getIdentificador());
    }    

    @Test
    void getEntradaFinanceira_deveRetornarBigDecimalComRenda() {
        InvestidorPessoa pessoa = new InvestidorPessoa(
                "Maria Souza",
                new EnderecoDummy(),
                LocalDate.of(1985, 3, 20),
                BigDecimal.ZERO,
                new ContatosDummy(),
                "987.654.321-00",
                8200.0,
                FaixaRenda.DIFERENCIADA
        );

        BigDecimal entrada = pessoa.getEntradaFinanceira();

        assertEquals(new BigDecimal("8200.0").setScale(1), entrada.setScale(1));
    }    
    
    @Test
    void getIdentificador_deveRetornarCnpj() {
        InvestidorEmpresa emp = new InvestidorEmpresa(
                "Empresa X",
                new EnderecoDummy(),
                LocalDate.now(),
                BigDecimal.ZERO,
                new ContatosDummy(),
                "12.345.678/0001-99",
                500000.0
        );

        assertEquals("12.345.678/0001-99", emp.getIdentificador());
    }

    @Test
    void getEntradaFinanceira_deveRetornarBigDecimalComFaturamento() {
        InvestidorEmpresa emp = new InvestidorEmpresa(
                "Empresa Y",
                new EnderecoDummy(),
                LocalDate.now(),
                BigDecimal.ZERO,
                new ContatosDummy(),
                "98.765.432/0001-11",
                750000.0
        );

        BigDecimal entrada = emp.getEntradaFinanceira();

        assertEquals(new BigDecimal("750000.0").setScale(1), entrada.setScale(1));
    }
	
    @Test
    void deveSerClasseAbstrata() {
        int modificadores = Investidor.class.getModifiers();
        assertTrue(Modifier.isAbstract(modificadores),
                "A classe Investidor deve ser abstrata");
    }

    @Test
    void deveConterMetodoAbstratoGetEntradaFinanceira() throws Exception {
        Method metodo = Investidor.class.getDeclaredMethod("getEntradaFinanceira");

        int modificadores = metodo.getModifiers();

        assertTrue(Modifier.isAbstract(modificadores),
                "O método getEntradaFinanceira() deve ser abstrato");

        assertEquals(BigDecimal.class, metodo.getReturnType(),
                "O método getEntradaFinanceira() deve retornar BigDecimal");
    }
}