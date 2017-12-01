package br.com.caelum.pm73.dao;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Lance;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {

	private Session session;
	private LeilaoDao leilaoDao;
	private UsuarioDao usuarioDao;
	
	private Calendar hoje;
	private Calendar ontem;
	private Calendar c5DiasAtras;
	private Calendar c10DiasAtras;
	private Calendar c11DiasAtras;
	
	private Usuario jose;
	private Usuario joao;
	
	@Before
	public void setup() {
		session =new CriadorDeSessao().getSession();
		session.beginTransaction();
		
		session = new CriadorDeSessao().getSession();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
		
		hoje = new GregorianCalendar(2017, Calendar.NOVEMBER, 30);
		ontem = new GregorianCalendar(2017, Calendar.NOVEMBER, 29);
		c5DiasAtras = new GregorianCalendar(2017, Calendar.NOVEMBER, 25);
		c10DiasAtras = new GregorianCalendar(2017, Calendar.NOVEMBER, 20);
		c11DiasAtras = new GregorianCalendar(2017, Calendar.NOVEMBER, 19);
		
		jose = new Usuario("Jose","jose@gmail.com");
		joao = new Usuario("Joao","joao@gmail.com");
		
	}

	@After
	public void tearDown(){
		session.getTransaction().rollback();
		session.close();
	}
	
	/*@Test
	public void deveContarLeiloesNaoEncerrados() {
		
		Usuario mauricio = new Usuario("Mauricio","mauricio@gmail.com");
		
		Leilao ativo = new Leilao("TV", 1500.0, mauricio, false);
		
		Leilao encerrado = new Leilao("Gel", 700.0, mauricio, false);
		encerrado.encerra();
		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(ativo);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(1L, total);
	}
	
	@Test
	public void deveDevolver0SeNaoTiverLeiloesAtivos() {
		
		Usuario mauricio = new Usuario("Mauricio","mauricio@gmail.com");
				
		Leilao encerrado = new Leilao("Gel", 700.0, mauricio, false);
		encerrado.encerra();
		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(encerrado);
		
		long total = leilaoDao.total();
		
		assertEquals(0L, total);
	}
	
	@Test
	public void deveRetornarLeiloesNovos() {
		
		Usuario mauricio = new Usuario("Mauricio","mauricio@gmail.com");
				
		Leilao ativo = new Leilao("TV", 1500.0, mauricio, false);

		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(ativo);
		
		List<Leilao> novos = leilaoDao.novos();
		
		assertEquals(1L, novos.size());
	}
	
	@Test
	public void deveRetornarLeiloesAntigos() {
		
		Usuario mauricio = new Usuario("Mauricio","mauricio@gmail.com");
				
		Leilao ativo = new Leilao("TV", 1500.0, mauricio, false);
		ativo.setDataAbertura(new GregorianCalendar(2017, Calendar.NOVEMBER, 1));

		usuarioDao.salvar(mauricio);
		leilaoDao.salvar(ativo);
		
		List<Leilao> novos = leilaoDao.antigos();
		
		assertEquals(1L, novos.size());
	}
	
	@Test
	public void deveTrazerLeiloesAtivosNoPeriodo() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c5DiasAtras)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(1, leiloes.size());
		assertThat(leiloes, hasItem(leilao));
	}
	
	
	@Test
	public void naoDeveTrazerLeiloesAtivosForaDoPeriodo() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c10DiasAtras)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c5DiasAtras, hoje);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerradosDentroDoPeriodo() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c5DiasAtras)
				.comDono(jose)
				.encerrado()
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerradosForaDoPeriodo() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c10DiasAtras)
				.comDono(jose)
				.encerrado()
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c5DiasAtras, hoje);
		
		assertEquals(0, leiloes.size());
	}

	@Test
	public void deveTrazerLeiloesAtivosNoPeriodoLimiteInicial() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c10DiasAtras)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(1, leiloes.size());
		assertThat(leiloes, hasItem(leilao));
	}
	
	@Test
	public void deveTrazerLeiloesAtivosNoPeriodoLimiteFinal() {
		Leilao leilao = new LeilaoBuilder()
				.naData(hoje)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(1, leiloes.size());
		assertThat(leiloes, hasItem(leilao));
	}
	
	@Test
	public void naoDeveTrazerLeiloesAtivosForaDoPeriodoLimiteInicial() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c11DiasAtras)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void naoDeveTrazerLeiloesAtivosForaDoPeriodoLimiteFinal() {
		Leilao leilao = new LeilaoBuilder()
				.naData(hoje)
				.comDono(jose)
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, ontem);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void deveTrazerApenasLeiloesAtivosNoPeriodo() {
		Leilao leilao = new LeilaoBuilder()
				.naData(c5DiasAtras)
				.comDono(jose)
				.controi();
		
		Leilao leilao2 = new LeilaoBuilder()
				.naData(c5DiasAtras)
				.comDono(jose)
				.encerrado()
				.controi();
		
		usuarioDao.salvar(jose);
		leilaoDao.salvar(leilao);
		leilaoDao.salvar(leilao2);
		
		List<Leilao> leiloes = leilaoDao.porPeriodo(c10DiasAtras, hoje);
		
		assertEquals(1, leiloes.size());
		assertThat(leiloes, hasItem(leilao));
	}*/
	
	@Test
	public void deveTrazerLeiloesAtivosDentroDoValorDoIntervalo() {
		
		Lance lance1 = new LanceBuilder().comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comValor(1300.0).controi();
		Lance lance4 = new LanceBuilder().comValor(1400.0).controi();
		
		Leilao leilao = new LeilaoBuilder()
				.comValor(300)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.comLance(lance4)
				.controi();
		
		usuarioDao.salvar(jose);
		usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.disputadosEntre(000.0, 500.0);
		
		assertEquals(1, leiloes.size());
		assertThat(leiloes, hasItem(leilao));
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerradosDentroDoValorNoIntervalo() {
		Lance lance1 = new LanceBuilder().comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comValor(1300.0).controi();
		Lance lance4 = new LanceBuilder().comValor(1400.0).controi();
		
		Leilao leilao = new LeilaoBuilder()
				.comValor(300)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.comLance(lance4)
				.controi();
		
		leilao.encerra();
		
		usuarioDao.salvar(jose);
		usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.disputadosEntre(000.0, 500.0);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void naoDeveTrazerLeiloesEncerradosForaDoValorNoIntervalo() {
		Lance lance1 = new LanceBuilder().comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comValor(1300.0).controi();
		Lance lance4 = new LanceBuilder().comValor(1400.0).controi();
		
		Leilao leilao = new LeilaoBuilder()
				.comValor(1000)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.comLance(lance4)
				.controi();
		
		leilao.encerra();
		
		usuarioDao.salvar(jose);
		usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.disputadosEntre(000.0, 500.0);
		
		assertEquals(0, leiloes.size());
	}
	
	
	@Test
	public void naoDeveTrazerLeiloesAtivosForaDoValorNoIntervalo() {
		
		Lance lance1 = new LanceBuilder().comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comValor(1300.0).controi();
		Lance lance4 = new LanceBuilder().comValor(1400.0).controi();
		
		Leilao leilao = new LeilaoBuilder()
				.comValor(1000)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.comLance(lance4)
				.controi();
		
		//usuarioDao.salvar(jose);
		//usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.disputadosEntre(000.0, 500.0);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void naoDeveTrazerLeiloesAtivosComMenosDe4Lances() {
		
		Lance lance1 = new LanceBuilder().comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comValor(1300.0).controi();
		
		Leilao leilao = new LeilaoBuilder()
				.comValor(300)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.controi();
		
		leilaoDao.salvar(leilao);
		
		List<Leilao> leiloes = leilaoDao.disputadosEntre(000.0, 500.0);
		
		assertEquals(0, leiloes.size());
	}
	
	@Test
	public void deveTrazerLeiloesDoUsuarioSemRepeticao() {
		
		Lance lance1 = new LanceBuilder().comUsuario(jose).comValor(1100.0).controi();
		Lance lance2 = new LanceBuilder().comUsuario(joao).comValor(1200.0).controi();
		Lance lance3 = new LanceBuilder().comUsuario(jose).comValor(1300.0).controi();

		Lance lance4 = new LanceBuilder().comUsuario(jose).comValor(1100.0).controi();
		Lance lance5 = new LanceBuilder().comUsuario(joao).comValor(1200.0).controi();
		Lance lance6 = new LanceBuilder().comUsuario(jose).comValor(1300.0).controi();
		
		Leilao leilao1 = new LeilaoBuilder()
				.comValor(300)
				.comLance(lance1)
				.comLance(lance2)
				.comLance(lance3)
				.controi();
		
		Leilao leilao2 = new LeilaoBuilder()
				.comValor(300)
				.comLance(lance4)
				.comLance(lance5)
				.comLance(lance6)
				.controi();
		
		usuarioDao.salvar(jose);
		usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);
		
		List<Leilao> leiloes = leilaoDao.listaLeiloesDoUsuario(jose);
		
		assertEquals(2, leiloes.size());
	}
	
	/*@Test
	public void deveTrazerValorInicialMedio() {
		
		Lance lance1 = new LanceBuilder().comUsuario(jose).comValor(1100.0).controi();
		Lance lance3 = new LanceBuilder().comUsuario(jose).comValor(1300.0).controi();

		Lance lance4 = new LanceBuilder().comUsuario(jose).comValor(1100.0).controi();
		
		Leilao leilao1 = new LeilaoBuilder()
				.comValor(50)
				.comLance(lance1)
				.comLance(lance3)
				.controi();
		
		Leilao leilao2 = new LeilaoBuilder()
				.comValor(150)
				.comLance(lance4)
				.controi();
		
		usuarioDao.salvar(jose);
		usuarioDao.salvar(joao);
		leilaoDao.salvar(leilao1);
		leilaoDao.salvar(leilao2);
		
		double valorMedio = leilaoDao.getValorInicialMedioDoUsuario(jose);
		
		assertEquals(100.0, valorMedio, 0.1);
	}*/

	/*@Test
	public void deveDeletarUsuario() {
		Usuario jack = new Usuario("jack","jack@gmail.com");
		
		usuarioDao.salvar(jack);

		usuarioDao.deletar(jack);
		
		session.flush();
		
		Usuario resultado = usuarioDao.porNomeEEmail("jack", "jack@gmail.com");
		
		assertNull(resultado);
		
	}*/
}
