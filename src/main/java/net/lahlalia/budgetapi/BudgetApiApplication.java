package net.lahlalia.budgetapi;

import net.lahlalia.budgetapi.entities.Role;
import net.lahlalia.budgetapi.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BudgetApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetApiApplication.class, args);
    }
    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository){
        return args -> {
            if(roleRepository.findByName("USER").isEmpty()){
                roleRepository.save(
                        Role.builder().name("USER")
                                .build()
                );
            }
        };
    }
}
