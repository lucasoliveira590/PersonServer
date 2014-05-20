package main.java.personserver.filters;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * @author Lucas
 * 
 * Toda requisição passa por essa classe
 */
@WebFilter(servletNames = { "Person REST Service" })
public class JPAFilter implements Filter {

	private static EntityManagerFactory factory;
	private static EntityManager manager; 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.factory = getFactory();
	}

	@Override
	public void destroy() {
		this.factory.close();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// CHEGADA
		manager = factory.createEntityManager();
		request.setAttribute("EntityManager", manager);
		manager.getTransaction().begin();
		// CHEGADA

		// FACES SERVLET
		chain.doFilter(request, response);
		// FACES SERVLET

		// SAÍDA
		try {
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			manager.getTransaction().rollback();
		} finally {
			manager.close();
		}
		// SAÍDA
	}
	
	public static EntityManagerFactory getFactory(){
		if(factory!=null && factory.isOpen()) {
			return factory;
		}
		else {
			return Persistence.createEntityManagerFactory("PersonPU");
		}
	}
	public static EntityManager getManager(){
		if(manager == null || !manager.isOpen()){
			manager = getFactory().createEntityManager();
		}
		if(!manager.getTransaction().isActive()){
			manager.getTransaction().begin();
		}
		return manager;
	}
	
}
