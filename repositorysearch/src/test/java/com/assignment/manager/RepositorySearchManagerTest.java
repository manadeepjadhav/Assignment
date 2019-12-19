package com.assignment.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RepositorySearchManagerTest 
{
	@Autowired
    private RepositorySearchManager searchManager;
    
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setUp() throws Exception 
	{
		Mockito.reset(restTemplate);
	}

	@After
	public void tearDown() throws Exception 
	{
	}

	@Test
	public void testWhenAllProviderReturnRepositories() 
	{
		HttpHeaders authHeader = new HttpHeaders();
		authHeader.set("Authorization", "token  79b1c48c00eb4a24f013deeb38d79b2c5196ae2a");
		HttpEntity<Object> headerEntity = new HttpEntity<Object>(authHeader);
		String response1 = "[{id : 1, name : \"Repo1\", owner : {login : \"manadeepjadhav\"},html_url : \"http://example.com/repo1\", ssh_url : \"some ssh url\"}]";
		ResponseEntity<String> sampleReposne1 = new ResponseEntity<String>(response1, HttpStatus.OK);
		
		HttpHeaders authHeader2 = new HttpHeaders();
		authHeader2.set("Authorization", "Bearer gEm9nLwWctdmcyN77EQK");
		HttpEntity<Object> headerEntity2 = new HttpEntity<Object>(authHeader2);
		String response2 = "[{id : 112, name : \"Repo2\", owner : {name : \"Manadeep\"},http_url_to_repo : \"http://example.com/repo2\", ssh_url_to_repo : \"some other ssh url\"}]";
		ResponseEntity<String> sampleReposne2 = new ResponseEntity<String>(response2, HttpStatus.OK);
		
		
		when(restTemplate.exchange(eq("https://api.github.com/user/repos"),eq(
				HttpMethod.GET), eq(headerEntity), eq(String.class))).thenReturn(sampleReposne1);
		
		when(restTemplate.exchange(eq("https://gitlab.com/api/v4/users/manadeepjadhav/projects"),eq(
				HttpMethod.GET), eq(headerEntity2), eq(String.class))).thenReturn(sampleReposne2);
		
		assertTrue("Should return records from all providers", searchManager.getProviderRepositories(null).size() == 2); 
	}
	
	@Test
	public void testWhenOneProviderDoesNotReturnRepositories() 
	{
		HttpHeaders authHeader = new HttpHeaders();
		authHeader.set("Authorization", "token  79b1c48c00eb4a24f013deeb38d79b2c5196ae2a");
		HttpEntity<Object> headerEntity = new HttpEntity<Object>(authHeader);
		String response1 = "[{id : 1, name : \"Repo1\", owner : {login : \"manadeepjadhav\"},html_url : \"http://example.com/repo1\", ssh_url : \"some ssh url\"}]";
		ResponseEntity<String> sampleReposne1 = new ResponseEntity<String>(response1, HttpStatus.OK);
		
		HttpHeaders authHeader2 = new HttpHeaders();
		authHeader2.set("Authorization", "Bearer gEm9nLwWctdmcyN77EQK");
		HttpEntity<Object> headerEntity2 = new HttpEntity<Object>(authHeader2);
		ResponseEntity<String> sampleReposne2 = new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		
		
		when(restTemplate.exchange(eq("https://api.github.com/user/repos"),eq(
				HttpMethod.GET), eq(headerEntity), eq(String.class))).thenReturn(sampleReposne1);
		
		when(restTemplate.exchange(eq("https://gitlab.com/api/v4/users/manadeepjadhav/projects"),eq(
				HttpMethod.GET), eq(headerEntity2), eq(String.class))).thenReturn(sampleReposne2);
		
		assertTrue("Should return records from only working providers", searchManager.getProviderRepositories(null).size() == 1); 
	}
	
	@Test
	public void testWithOwnerFilter() 
	{
		HttpHeaders authHeader = new HttpHeaders();
		authHeader.set("Authorization", "token  79b1c48c00eb4a24f013deeb38d79b2c5196ae2a");
		HttpEntity<Object> headerEntity = new HttpEntity<Object>(authHeader);
		String response1 = "[{id : 1, name : \"Repo1\", owner : {login : \"manadeepjadhav\"},html_url : \"http://example.com/repo1\", ssh_url : \"some ssh url\"}]";
		ResponseEntity<String> sampleReposne1 = new ResponseEntity<String>(response1, HttpStatus.OK);
		
		HttpHeaders authHeader2 = new HttpHeaders();
		authHeader2.set("Authorization", "Bearer gEm9nLwWctdmcyN77EQK");
		HttpEntity<Object> headerEntity2 = new HttpEntity<Object>(authHeader2);
		String response2 = "[{id : 112, name : \"Repo2\", owner : {name : \"Manadeep\"},http_url_to_repo : \"http://example.com/repo2\", ssh_url_to_repo : \"some other ssh url\"}]";
		ResponseEntity<String> sampleReposne2 = new ResponseEntity<String>(response2, HttpStatus.OK);
		
		
		when(restTemplate.exchange(eq("https://api.github.com/user/repos"),eq(
				HttpMethod.GET), eq(headerEntity), eq(String.class))).thenReturn(sampleReposne1);
		
		when(restTemplate.exchange(eq("https://gitlab.com/api/v4/users/manadeepjadhav/projects"),eq(
				HttpMethod.GET), eq(headerEntity2), eq(String.class))).thenReturn(sampleReposne2);
		System.out.println(searchManager.getProviderRepositories("Manadeep"));
		assertTrue("Should return records matching owner filter", searchManager.getProviderRepositories("Manadeep").size() == 1); 
	}
	


}
