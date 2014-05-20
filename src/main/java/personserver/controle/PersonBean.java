package main.java.personserver.controle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.faces.bean.ManagedBean;

import main.java.personserver.model.Person;
import main.java.personserver.repository.PersonRepository;

import org.json.JSONObject;

/**
 * 
 * @author lucas
 * 
 *         Esta Classe é responsável pelos operações feitas em cima do modelo
 *         Person
 */
@ManagedBean
public class PersonBean {



	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	private Person person;
	private List<Person> persons;

	public PersonBean() {
	}

	/**
	 * 
	 * @param facebookId
	 * 
	 *            Construtor já cria um Person com "facebookId"
	 */
	public PersonBean(BigInteger facebookId) {
		person = new Person();
		person.setFacebookId(facebookId);
	}

	/**
	 * Este método adiciona a Pessoa corrente no banco
	 */
	public int add() {
		PersonRepository personRepository = new PersonRepository();
		int adicionou = personRepository.add(this.person);
		if (adicionou >= 0) {
			this.person = new Person();
		}
		return adicionou;
	}

	/**
	 * Este método remove a atual pessoa do banco
	 */
	public boolean remove() {
		PersonRepository personRepository = new PersonRepository();
		boolean removed = personRepository.remove(this.person.getFacebookId());
		this.person = new Person();
		return removed;
	}

	/**
	 * Este método lista as pessoas, com o limite passado por parametro.
	 * 
	 * @param limit
	 * @return
	 */
	public List<Person> getPersons(int limit) {
		PersonRepository personRepository = new PersonRepository();
		this.persons = personRepository.getLista(limit);
		System.out.println("Tipo do dado: " + persons.getClass());
		return this.persons;
	}

	/**
	 * Este método acessa ao serviço do facebook e pega as informações
	 * necessárias de Person
	 * 
	 * @return
	 */
	public boolean loadInfo() {
		HttpURLConnection connection = null;
		boolean achou = false;
		try {
			URL url = new URL("http://graph.facebook.com/"
					+ this.person.getFacebookId());
			connection = (HttpURLConnection) url.openConnection();
			if (connection.getResponseCode() == 201
					|| connection.getResponseCode() == 200) {
				InputStream response = connection.getInputStream();
				String texto = convertStreamToString(response);
				JSONObject obj = new JSONObject(texto);
				this.person.setGenero(obj.get("gender").toString());
				this.person.setName(obj.get("first_name").toString()
						+ obj.get("last_name"));
				this.person.setUserName(obj.get("username").toString());
				achou = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return achou;
	}

	/**
	 * Este método recebe um imputStream e o converte para string
	 * @param response
	 * @return
	 */
	private static String convertStreamToString(InputStream response) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Person> getPersons() {
		return persons;
	}
}
