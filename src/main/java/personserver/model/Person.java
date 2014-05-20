package main.java.personserver.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Lucas
 *
 * Classe que representa o modelo de "Person" para a aplicação
 */
@Entity
@XmlRootElement
public class Person {
	
	@Id
	@Column(name="person_id")
	private BigInteger facebookId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="genero")
	private String genero;

	public BigInteger getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(BigInteger facebookId) {
		this.facebookId = facebookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
	
}

