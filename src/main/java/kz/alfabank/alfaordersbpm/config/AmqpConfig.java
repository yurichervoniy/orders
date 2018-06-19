package kz.alfabank.alfaordersbpm.config;

import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    ConnectionNameStrategy connectionNameStrategy(){
        return connectionFactory -> new StringBuilder().append("ORDERSBPM-").append(connectionFactory.getClass().getSimpleName()).append("#").append(connectionFactory.hashCode()).toString();
    }

}
