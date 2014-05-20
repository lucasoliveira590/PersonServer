PersonServer
============

Instruções como rodar o projeto.

1 - Executar 1.1 OU 1.2
	1.1 - Criar um banco no Postgres chamado: 'desafiodb', com usuário 'postgres' e senha 'postgres'.
	1.2 - Alterar o acesso a base de dados no persistence.xml.

2 - Baixar o projeto do GitHub
	$ git clone https://github.com/lucasoliveira590/PersonServer.git

3 - Fazer o deploy no TomCat 7;

4 - Testes
	4.1 Para rodar os testes aplicação deve estar rodando no seguinte endereço: http://localhost:8080/PersonServer
	4.2 Executar a classe de teste com JUnit

5 - Endereços do REST:
	5.1 Inserir: 
		curl -X POST -F "facebookId=XXXXXXXXXX" http://localhost:XXXX/PersonServer/person/
	5.2 Listagem:
		curl http://localhost:xxxx/PersonServer/person/{limitParam}
	5.3 Deletar
		curl -X DELETE http://localhost:xxxx/person/XXXXXXXXXX

