package br.com.caelum.pm73.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pm73.dominio.Usuario;

public class UsuarioDaoTest {
	
	private Session session;
	
	@Before
	public void setup() {
		session =new CriadorDeSessao().getSession();
		session.beginTransaction();
	}
	
	@After
	public void tearDown(){
		session.getTransaction().rollback();
		session.close();
	}

	@Test
	public void verificaSeRetornaUsuarioCorretoAoFazerPesquisa() {
		UsuarioDao usuarioDao = new UsuarioDao(session);
		
		usuarioDao.salvar(new Usuario("Eduardo","earaujo@gmail.com"));
		
		Usuario usuario = usuarioDao.porNomeEEmail("Eduardo", "earaujo@gmail.com");
		
		assertEquals("Eduardo", usuario.getNome());
		assertEquals("earaujo@gmail.com", usuario.getEmail());
	}
	
	@Test
	public void verificaSeNaoRetornaUsuarioSeNaoExistir() {
		UsuarioDao usuarioDao = new UsuarioDao(session);
				
		Usuario usuario = usuarioDao.porNomeEEmail("Eduardo", "earaujo@gmail.com");
		
		assertNull(usuario);
	}
	
	


}
