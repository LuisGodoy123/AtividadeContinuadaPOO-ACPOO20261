package br.edu.cs.poo.ac.bolsa.testes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.entidade.Investidor;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class InvestidorTest {

    private Investidor investidor;

    @BeforeEach
    void setup() {
        investidor = new Investidor(
                "João Silva",
                null,                // mocks simples
                LocalDate.now().minusYears(5),
                new BigDecimal("100.00"),      // bônus inicial
                null
        );
    }

    // ---------------------------------------------------------
    // TESTES DO MÉTODO creditarBonus
    // ---------------------------------------------------------

    @Test
    void deveCreditarBonusQuandoValorValido() {
        investidor.creditarBonus(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("150.00"), investidor.getBonus());
    }

    @Test
    void deveCreditarZeroSemAlterarBonus() {
        investidor.creditarBonus(BigDecimal.ZERO);
        assertEquals(new BigDecimal("100.00"), investidor.getBonus());
    }

    @Test
    void naoDeveCreditarQuandoValorNulo() {
        investidor.creditarBonus(null);
        assertEquals(new BigDecimal("100.00"), investidor.getBonus());
    }

    // ---------------------------------------------------------
    // TESTES DO MÉTODO debitarBonus
    // ---------------------------------------------------------

    @Test
    void deveDebitarBonusQuandoValorValido() {
        investidor.debitarBonus(new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.00"), investidor.getBonus());
    }

    @Test
    void deveDebitarZeroSemAlterarBonus() {
        investidor.debitarBonus(BigDecimal.ZERO);
        assertEquals(new BigDecimal("100.00"), investidor.getBonus());
    }

    @Test
    void naoDeveDebitarQuandoValorNulo() {
        investidor.debitarBonus(null);
        assertEquals(new BigDecimal("100.00"), investidor.getBonus());
    }

    // ---------------------------------------------------------
    // TESTE EXTRA: Garantir que não quebra com valores grandes
    // ---------------------------------------------------------

    @Test
    void deveCreditarValorGrande() {
        investidor.creditarBonus(new BigDecimal("1000000.00"));
        assertEquals(new BigDecimal("1000100.00"), investidor.getBonus());
    }
}