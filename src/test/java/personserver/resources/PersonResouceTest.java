package test.java.personserver.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import main.java.personserver.controle.PersonBean;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class PersonResouceTest {
	private final String URL_WS = "http://localhost:8080/PersonServer/person/";
	private final String FACEBOOK_ID = "100002008148617";
	
	
	// /////////////////////////TESTES UNITARIOS//////////////////////////////
	/**
	 * Verifica se o sistema consegue carregar as informações do facebook
	 */
	 @Test
	public void testShouldLoadPersonInformation() {
		PersonBean pb = new PersonBean(new BigInteger(FACEBOOK_ID));
		assertTrue(pb.loadInfo());
	}

	// /////////////////////////TESTES INTEGRAÇÃO//////////////////////////////

	/*
	 * Teste para inclusão feita por requisição
	 */
	@Test
	public void testAddNewPersonByRequest() throws IOException {
		// Insere usuário
		assertEquals(201, addNewPerson(FACEBOOK_ID));
		// Remover pós inserção
		removeAPerson(FACEBOOK_ID);

	}

	/*
	 * Teste para exclusão por requisição
	 */
	@Test
	public void testRemovePersonByRequest() throws ClientProtocolException,
			IOException {
		assertEquals(204, removeAPerson(FACEBOOK_ID));
	}

	/*
	 * Teste Lista 1 pessoa por requisição
	 */
	@Test
	public void testListOnePersonByRequest() {
		// Adiciona para ser listado
		addNewPerson(FACEBOOK_ID);

		// Faz a requisição da listagem propriamente dita
		int resultadoListagem = listaPessoas(1);

		// Remover o que foi inserido.
		removeAPerson(FACEBOOK_ID);
		
		assertEquals(200, resultadoListagem);
	}

	/*
	 * Teste para listar todas as pessoas por requisição
	 */
	@Test
	public void testListAllPersonByRequest() {
		// Adiciona para ser listado
		addNewPerson(FACEBOOK_ID);

		// Faz a requisição da listagem propriamente dita
		int resultadoListagem = listaPessoas(-1);

		// Remover o que foi inserido.
		removeAPerson(FACEBOOK_ID);
		
		assertEquals(200, resultadoListagem);
	}
	
	private int addNewPerson(String facebookId){
		try{
			System.out.println("Adicionando usuario: "+facebookId);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL_WS);
			StringBody faceId = new StringBody(facebookId);
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("facebookId", faceId);
			httpPost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httpPost);
			return response.getStatusLine().getStatusCode();
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	private int removeAPerson(String facebookId){
		try{
			System.out.println("Removendo usuário: "+facebookId);
			HttpClient httpclient = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(URL_WS + facebookId);
			HttpResponse response = httpclient.execute(httpDelete);
			return response.getStatusLine().getStatusCode();
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	private int listaPessoas(int i) {
		URL url;
		HttpURLConnection conn = null;
		try {
			if (i > 0){
				url = new URL(URL_WS + i);
			}else{
				url = new URL(URL_WS);
			}
			conn = (HttpURLConnection) url.openConnection();
			return conn.getResponseCode();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return -1;
	}

}
