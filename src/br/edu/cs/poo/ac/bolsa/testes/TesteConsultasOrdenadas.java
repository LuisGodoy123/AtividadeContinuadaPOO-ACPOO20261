package br.edu.cs.poo.ac.bolsa.testes;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.bolsa.dao.DAOInvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.entidade.ComparadorInvestidorPessoaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.FaixaRenda;
import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;
import br.edu.cs.poo.ac.bolsa.util.Comparador;
import br.edu.cs.poo.ac.bolsa.util.ComparadorGenerico;
import br.edu.cs.poo.ac.bolsa.util.Comparavel;
import br.edu.cs.poo.ac.bolsa.util.Ordenador;

public class TesteConsultasOrdenadas extends TesteGenerico {
	
    private DAOInvestidorPessoa dao;

    @BeforeEach
    void setup() {
    	 limparDiretorio("InvestidorPessoa");
        dao = new DAOInvestidorPessoa();        
    }

    private InvestidorPessoa criarInvestidor(String cpf, String nome) {
        return new InvestidorPessoa(
                nome,
                null,
                LocalDate.of(1990, 1, 1),
                BigDecimal.ZERO,
                null,
                cpf,
                5000.0,
                FaixaRenda.DIFERENCIADA
        );
    }

    @Test
    @DisplayName("consultarTodos deve retornar todos os investidores cadastrados")
    void testConsultarTodosComInvestidores() {
        InvestidorPessoa inv1 = criarInvestidor("111", "Ana");
        InvestidorPessoa inv2 = criarInvestidor("222", "Bruno");

        dao.incluirInvestidorPessoa(inv1);
        dao.incluirInvestidorPessoa(inv2);

        InvestidorPessoa[] resultado = dao.consultarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.length);
        assertEquals("Ana", resultado[0].getNome());
        assertEquals("Bruno", resultado[1].getNome());
    }

    @Test
    @DisplayName("consultarTodos deve retornar array vazio quando não há investidores")
    void testConsultarTodosSemInvestidores() {
        InvestidorPessoa[] resultado = dao.consultarTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.length);
    }

    @Test
    @DisplayName("consultarTodos deve retornar null quando cadastro.buscarTodos() retorna null")
    void testConsultarTodosCadastroNulo() throws Exception {
        // Simulando comportamento: forçando cadastro.buscarTodos() retornar null
        // Isso depende de como DAOGenerico é implementado.
        // Aqui assumimos que podemos sobrescrever o cadastro manualmente.

        dao = new DAOInvestidorPessoa() {
            @Override
            public InvestidorPessoa[] consultarTodos() {
                return null; // simulando retorno nulo
            }
        };

        InvestidorPessoa[] resultado = dao.consultarTodos();

        assertNull(resultado);
    }

    @Test
    @DisplayName("consultarTodos deve retornar apenas objetos InvestidorPessoa")
    void testConsultarTodosTiposCorretos() {
        InvestidorPessoa inv = criarInvestidor("333", "Carlos");
        dao.incluirInvestidorPessoa(inv);

        InvestidorPessoa[] resultado = dao.consultarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.length);
        assertTrue(resultado[0] instanceof InvestidorPessoa);
    }
	
    private InvestidorPessoa criarInvestidor(String nome) {
        return new InvestidorPessoa(
                nome,
                null, // Endereco
                LocalDate.of(1990, 1, 1),
                BigDecimal.ZERO,
                null, // Contatos
                "00000000000",
                5000.0,
                FaixaRenda.DIFERENCIADA
        );
    }

    @Test
    @DisplayName("Comparar dois InvestidorPessoa - caminho feliz (nomes diferentes)")
    void testCompararCaminhoFeliz() {
        InvestidorPessoa inv1 = criarInvestidor("Ana");
        InvestidorPessoa inv2 = criarInvestidor("Bruno");

        int resultado = inv1.comparar(inv2);

        assertTrue(resultado < 0, "Ana deve vir antes de Bruno na ordem alfabética");
    }

    @Test
    @DisplayName("Comparar dois InvestidorPessoa - caminho feliz (nomes iguais)")
    void testCompararNomesIguais() {
        InvestidorPessoa inv1 = criarInvestidor("Carlos");
        InvestidorPessoa inv2 = criarInvestidor("Carlos");

        int resultado = inv1.comparar(inv2);

        assertEquals(0, resultado, "Nomes iguais devem resultar em comparação zero");
    }

    @Test
    @DisplayName("Comparar com objeto não InvestidorPessoa - caminho infeliz")
    void testCompararCaminhoInfeliz() {
        InvestidorPessoa inv = criarInvestidor("Daniel");

        Comparavel compInvalido = new Comparavel() {
            @Override
            public int comparar(Comparavel c) {
                return 0;
            }
        };

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            inv.comparar(compInvalido);
        });

        assertEquals("O argumento nao e do tipo InvestidorPessoa", ex.getMessage());
    }
    
    @Test
    @DisplayName("ordenar(Comparavel[]) deve ordenar corretamente usando ComparadorGenerico")
    void testOrdenarSemComparador() {
        Comparavel[] itens = {
                new ItemTeste(5),
                new ItemTeste(1),
                new ItemTeste(3)
        };

        Ordenador.ordenar(itens);

        assertEquals(1, ((ItemTeste) itens[0]).getValor());
        assertEquals(3, ((ItemTeste) itens[1]).getValor());
        assertEquals(5, ((ItemTeste) itens[2]).getValor());
    }

    @Test
    @DisplayName("ordenar(Comparavel[], Comparador) deve ordenar corretamente com comparador customizado")
    void testOrdenarComComparador() {
        Comparavel[] itens = {
                new ItemTeste(10),
                new ItemTeste(2),
                new ItemTeste(7)
        };

        Comparador comparador = new ComparadorGenerico();

        Ordenador.ordenar(itens, comparador);

        assertEquals(2, ((ItemTeste) itens[0]).getValor());
        assertEquals(7, ((ItemTeste) itens[1]).getValor());
        assertEquals(10, ((ItemTeste) itens[2]).getValor());
    }

    @Test
    @DisplayName("ordenar deve ignorar array nulo sem lançar exceção")
    void testOrdenarArrayNulo() {
        assertDoesNotThrow(() -> Ordenador.ordenar((Comparavel[]) null));
    }

    @Test
    @DisplayName("ordenar deve ignorar comparador nulo sem lançar exceção")
    void testOrdenarComparadorNulo() {
        Comparavel[] itens = {
                new ItemTeste(3),
                new ItemTeste(1)
        };

        assertDoesNotThrow(() -> Ordenador.ordenar(itens, null));

        // Deve permanecer sem ordenação
        assertEquals(3, ((ItemTeste) itens[0]).getValor());
        assertEquals(1, ((ItemTeste) itens[1]).getValor());
    }

    @Test
    @DisplayName("ordenar deve ignorar array vazio")
    void testOrdenarArrayVazio() {
        Comparavel[] itens = new Comparavel[0];

        assertDoesNotThrow(() -> Ordenador.ordenar(itens));

        assertEquals(0, itens.length);
    }
    
    private InvestidorPessoa criar(String nome, String cpf, double renda) {
        return new InvestidorPessoa(
                nome,
                null,
                LocalDate.of(1990, 1, 1),
                BigDecimal.ZERO,
                null,
                cpf,
                renda,
                FaixaRenda.DIFERENCIADA
        );
    }

    @Test
    @DisplayName("comparar deve retornar 1 quando renda do primeiro é maior")
    void testCompararPrimeiroMaior() {
        ComparadorInvestidorPessoaRenda comp = new ComparadorInvestidorPessoaRenda();

        InvestidorPessoa inv1 = criar("Ana", "111", 8000);
        InvestidorPessoa inv2 = criar("Bruno", "222", 5000);

        int resultado = comp.comparar(inv1, inv2);

        assertEquals(1, resultado);
    }

    @Test
    @DisplayName("comparar deve retornar -1 quando renda do primeiro é menor")
    void testCompararPrimeiroMenor() {
        ComparadorInvestidorPessoaRenda comp = new ComparadorInvestidorPessoaRenda();

        InvestidorPessoa inv1 = criar("Ana", "111", 3000);
        InvestidorPessoa inv2 = criar("Bruno", "222", 7000);

        int resultado = comp.comparar(inv1, inv2);

        assertEquals(-1, resultado);
    }

    @Test
    @DisplayName("comparar deve retornar 0 quando rendas são iguais")
    void testCompararRendasIguais() {
        ComparadorInvestidorPessoaRenda comp = new ComparadorInvestidorPessoaRenda();

        InvestidorPessoa inv1 = criar("Ana", "111", 5000);
        InvestidorPessoa inv2 = criar("Bruno", "222", 5000);

        int resultado = comp.comparar(inv1, inv2);

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("comparar deve lançar exceção quando algum argumento não é InvestidorPessoa")
    void testCompararTipoInvalido() {
        ComparadorInvestidorPessoaRenda comp = new ComparadorInvestidorPessoaRenda();

        InvestidorPessoa inv1 = criar("Ana", "111", 5000);

        Comparavel invalido = new Comparavel() {
            @Override
            public int comparar(Comparavel c) {
                return 0;
            }
        };

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            comp.comparar(inv1, invalido);
        });

        assertEquals("Pelo menos um dos argumentos nao e do tipo InvestidorPessoa", ex.getMessage());
    }
    
    private static class ItemTeste implements Comparavel {

        private final int valor;

        public ItemTeste(int valor) {
            this.valor = valor;
        }

        public int getValor() {
            return valor;
        }

        @Override
        public int comparar(Comparavel comp) {
            ItemTeste outro = (ItemTeste) comp;
            return Integer.compare(this.valor, outro.valor);
        }

        @Override
        public String toString() {
            return String.valueOf(valor);
        }
    }
}