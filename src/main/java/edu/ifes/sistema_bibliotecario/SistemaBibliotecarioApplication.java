package edu.ifes.sistema_bibliotecario;

import edu.ifes.sistema_bibliotecario.config.carga.CargaInicialDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaBibliotecarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaBibliotecarioApplication.class, args);
	}

	@Bean
	CommandLineRunner cargaInicial(CargaInicialDados cargaInicialDados) {
		return args -> cargaInicialDados.carregar();
	}

}
