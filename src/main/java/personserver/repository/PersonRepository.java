package main.java.personserver.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import main.java.personserver.filters.JPAFilter;
import main.java.personserver.model.Person;

/**
 * @author Lucas
 * 
 *         Esta classe é responsável pelas operações relacionadas ao banco.
 */
public class PersonRepository {
	private EntityManager manager;
	
	public PersonRepository() {
		this.manager = JPAFilter.getManager();
	}

	/**
	 * Este metodo adiciona uma pessoa no repositório.
	 * 
	 * @param person
	 */
	public int add(Person person) {
		Person personAux = this.procura(person.getFacebookId());
		if (personAux != null){
			return 0;
		}
		try{
			this.manager.persist(person);
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

	/**
	 * Este método remove uma pessoa determianda do banco.
	 * 
	 * @param facebookId
	 */
	public boolean remove(BigInteger facebookId) {
		Person person = this.procura(facebookId);
		if (person == null){
			return false;
		}
		try{
			this.manager.remove(person);
		}catch(Exception e){
			System.err.println("Não removeu pessoa com facebookId: " + facebookId);
			return false;
		}
		return true;
	}

	/**
	 * Este método busca e retorna uma pessoa com determinado id, se a pessoa
	 * não existir no banco, o retorno é null
	 * 
	 * @param facebookId
	 * @return
	 */
	public Person procura(BigInteger facebookId) {
		return this.manager.find(Person.class, facebookId);
	}

	/**
	 * Este metodo retorna a lista de pessoas do banco. Caso seja passado
	 * parâmetro e a lista for maior que o número passado, só serão retornadas o
	 * numero de pessoas passado por parâmetro.
	 * 
	 * @param limit
	 * @return
	 */
	public List<Person> getLista(int limit) {
		Query query;
		if (limit > 0){
			query = this.manager.createQuery("select x from Person x").setMaxResults(limit);
		}else{
			query = this.manager.createQuery("select x from Person x"); 
		}
		return query.getResultList();
	}
}
