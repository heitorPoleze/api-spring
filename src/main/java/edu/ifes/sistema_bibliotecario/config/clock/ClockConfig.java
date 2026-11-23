package edu.ifes.sistema_bibliotecario.config.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class ClockConfig {
    @Bean
    public Clock clock(){
        return Clock.systemDefaultZone();
    }

    public LocalDate getDataAtual(){
        return LocalDate.now(clock());
    }

    public static Clock clockFixo() {
        return clockFixo("2026-10-01T10:00:00Z");
    }

    public static Clock clockFixo(String dataHoraISO) {
        return Clock.fixed(
                Instant.parse(dataHoraISO),
                ZoneId.systemDefault()
        );
    }
}
