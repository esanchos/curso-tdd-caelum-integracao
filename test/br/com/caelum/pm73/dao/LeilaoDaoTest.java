package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;

public class LeilaoDaoTest {

	private Session session;
	private LeilaoDao leilaoDao;
	private UsuarioDao usuarioDao;
	
	@Before
	public void setup() {
		session =new CriadorDeSessao().getSession();
		session.beginTransaction();
		
		session = new CriadorDeSessao().getSession();
		leilaoDao = new LeilaoDao(session);
		usuarioDao = new UsuarioDao(session);
	}
	
	@After
	public void tearDown(){
		session.beginTransaction().rollback();
		session.close();
	}
	
	@Test
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

}
