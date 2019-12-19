package com.assignment.manager;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("test")
@Configuration
public class TestConfiguration 
{
   @Bean
   public RestTemplate getMockRestTemplate()
   {
	   return Mockito.mock(RestTemplate.class);
   }
   
   @Bean
   public RepositorySearchManager getSearchManager()
   {
	   return new RepositorySearchManager();
   }
}
