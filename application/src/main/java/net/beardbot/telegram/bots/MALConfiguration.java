package net.beardbot.telegram.bots;

import net.beardbot.myanimelist.MALClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.NotAuthorizedException;

@Configuration
public class MALConfiguration {
    @Value("${mal.username}")
    private String malUsername;
    @Value("${mal.password}")
    private String malPassword;

    @Bean
    @ConditionalOnMissingBean(MALClient.class)
    public MALClient malClient(){
        MALClient malClient = new MALClient(malUsername,malPassword);
        try {
            malClient.verifyCredentials();
        } catch (NotAuthorizedException e) {
            e.printStackTrace();
        }
        return malClient;
    }
}
