package main.java.personserver.resources;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import main.java.personserver.controle.PersonBean;
import main.java.personserver.model.Person;
import main.java.personserver.repository.PersonRepository;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.json.JSONArray;

import com.sun.jersey.multipart.FormDataParam;

/**
 * Esta classe é responsável por fornecer os serviços relacionado ao objeto
 * Person
 * 
 * @author lucas
 * 
 */
@Path("/person")
public class PersonResource {
	
	static final String LOG_PATH = "fileLog.txt";
	static Logger logger = Logger.getLogger(PersonResource.class);

	/**
	 * Este método lista Persons O numero de Person listados é determinado por
	 * parametro
	 * 
	 * @param limit
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/{limit}")
	@Produces("application/json")
	public Response getPerson(@PathParam("limit") int limit) throws IOException {
		this.abreLogger();
		logger.info("Inicializando busca de " + limit + " pessoas");
		PersonRepository personRepository = new PersonRepository();
		ArrayList<Person> lista = (ArrayList<Person>) personRepository.getLista(limit);
		if (lista.isEmpty()) {
			logger.info("Não há pessoas para serem buscadas na base de dados.");
			return Response.status(404).build();
		}
		JSONArray jarray = new JSONArray(lista);
		logger.info("Listagem de " + limit + " pessoas realizada com sucesso.");
		return Response.status(200).entity(jarray.toString()).build();

	}

	/**
	 * Este método lista todos os Persons armazenados
	 * 
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/")
	@Produces("application/json")
	public Response getPerson() throws IOException {
		this.abreLogger();
		logger.info("Inicializando listagem de todas as pessoas da base de dados...");
		PersonRepository personRepository = new PersonRepository();
		ArrayList<Person> lista = (ArrayList<Person>) personRepository.getLista(0);
		if (lista.isEmpty()) {
			logger.info("Não há pessoas para serem listadas na base de dados.");
			return Response.status(404).build();
		}
		JSONArray jarray = new JSONArray(lista);
		logger.info("Busca das pessoas da base de dados realizada com sucesso.");
		return Response.status(200).entity(jarray.toString()).build();
	}

	/**
	 * Este método insere uma nova pessoa no banco baseado no facebookID
	 * 
	 * @param facebookId
	 * @return Retorna o status da operação
	 * @throws IOException
	 */
	@POST
	@Produces("text/plain")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response insertPerson(@FormDataParam("facebookId") String facebookId)
			throws IOException {
		this.abreLogger();
		logger.info("Inicializando inserção de nova pessoa...");
		int status = 0;
		try {
			BigInteger id = new BigInteger(facebookId);
			PersonBean pb = new PersonBean(id);
			logger.info("Carregando outras informações da pessoa...");
			boolean achou = pb.loadInfo();
			if (achou) {
				logger.info("Informações carregadas...");
				int add = pb.add();
				if (add == 0){
					logger.info("Pessoa já existente na base de dados.");
				}else{
					logger.info("Pessoa cadastrada com sucessso.");
				}
				status = 201;
			} else {
				logger.error("Não foi possível carregar as informações da pessoa.");
			}
		} catch (Exception e) {
			logger.error("Houve uma falha ao tentar carregar a pessoa.");
			e.printStackTrace();
			status = 404;
		}
		return Response.status(status).build();
	}

	/**
	 * Este método apaga uma pessoa do banco baseado no facebookId
	 * 
	 * @param facebookId
	 * @return Retorna o status da operação
	 * @throws IOException
	 */
	@DELETE
	@Path("/{facebookId}")
	@Produces("text/plain")
	public Response deletePerson(@PathParam("facebookId") BigInteger facebookId)
			throws IOException {
		this.abreLogger();
		logger.info("Inicializando remoção de pessoa...");
		int status = 0;
		try {
			PersonBean pb = new PersonBean(facebookId);
			pb.remove();
			status = 204;
			logger.info("Pessoa removida com sucesso.");
		} catch (Exception e) {
			logger.error("Falha ao remover pessoa.");
			e.printStackTrace();
			status = 404;
		}
		return Response.status(status).build();
	}
	
	private void abreLogger() throws IOException {
		BasicConfigurator.configure();
		String logPath = getClass().getClassLoader().getResource(".").getPath();
		Appender fileAppender = new FileAppender(new PatternLayout(
				PatternLayout.TTCC_CONVERSION_PATTERN), logPath+"/log/logFile.log");
		
		logger.setLevel(Level.INFO);
		logger.addAppender(fileAppender);
	}

}
