package com.example.gsrelationaldataaccess;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
	@SpringBootApplication é uma anotação de conveniência que adiciona todos os itens a seguir:

	@Configuration: marca a classe como uma fonte de definições de bean para o contexto do aplicativo.

	@EnableAutoConfiguration: diz ao Spring Boot para começar a adicionar beans,
	com base nas configurações do caminho de classe, outros beans e várias configurações de propriedade.

	@ComponentScan: diz ao Spring para procurar outros componentes, configurações e serviços no
	com.example.relationaldataaccesspacote. Neste caso, não há.
 */

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {
	/*
	Esta Applicationclasse implementa o Spring Boot's CommandLineRunner,
	o que significa que ele executará o run()método depois que o contexto
	do aplicativo for carregado.
 */

	private static final Logger log =
			LoggerFactory.getLogger(RelationalDataAccessApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	/*
		O Spring Boot suporta H2 (um mecanismo de banco de dados relacional na memória) e cria automaticamente uma conexão.
		Como usamos spring-jdbc, o Spring Boot cria automaticamente um arquivo JdbcTemplate. O campo @Autowired JdbcTemplate
		o carrega automaticamente e o disponibiliza.
	 */
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {

		log.info("Creating tables");

		jdbcTemplate.execute("DROP TABLE customer IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customer(" +
				"id SERIAL, firs_name VARCHAR(255)," +
				"last_name VARCHAR(255))");

		//Dividindo a matriz de nomes inteiros em uma matriz de nomes/sobrenomes
		List<Object[]> splitUpNames = Arrays.asList("John Woo",
				"Jeff Dean", "Josh Long").stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		//Usando um fluxo Java 8 para imprimir cada tupla da lista
		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s"
				, name[0], name[1])));

		//Usando a operação batchUpdate do JdbcTemplate para carregar dados em massa
		jdbcTemplate.batchUpdate(
				"INSERT INTO customers(first_name," +
						" last_name) VALUES (?,?)", splitUpNames);

		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
		).forEach(customer -> log.info(customer.toString()));
	}
}
