package com.assignment.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import com.assignment.model.ProviderConfig;
import com.assignment.model.ProviderDetails;
import com.assignment.model.Repository;
import com.assignment.util.JsonUtils;

@Component
@SuppressWarnings(value = { "unchecked" })
public class RepositorySearchManager {
	private static final String PROVIDERCONFIG_YML = "/config/providerconfig.yml";

	@Autowired
	private RestTemplate restTemplate;

	private ProviderConfig providerConfig;
	
	private Logger loggger = LoggerFactory.getLogger(RepositorySearchManager.class);

	@PostConstruct
	public void initializeProviderConfig() {
		Yaml configYaml = new Yaml();
		try (InputStream in = RepositorySearchManager.class.getResourceAsStream(PROVIDERCONFIG_YML)) {
			providerConfig = configYaml.loadAs(in, ProviderConfig.class);
		} catch (IOException e) {
			loggger.error("Failed to load provider config {}", e.getMessage());
		}
	}

	public List<Repository> getProviderRepositories(String searchOwner) {
		List<Repository> updateRepositories = new ArrayList<>();
		for(ProviderDetails eachProvider : providerConfig.getProviders())
		{
			updateRepositories.addAll(getReposirotiesProvider(eachProvider,searchOwner));
		}
		
		return updateRepositories;

	}

	
	private List<Repository> getReposirotiesProvider(ProviderDetails eachProvider, String searchOwner) {
		HttpHeaders authHeader = new HttpHeaders();
		authHeader.set(eachProvider.getAuthHeaderName(), eachProvider.getAuthHaederValue());
		ResponseEntity<String> repositoryResponse = restTemplate.exchange(eachProvider.getUrl(), HttpMethod.GET,
				new HttpEntity<Object>(authHeader), String.class);
		List<Repository> updateRepositories = new ArrayList<>();
		if(repositoryResponse.getStatusCode() != HttpStatus.OK)
		{
			loggger.warn("Faiiled to connect to provider {} hence skipping same", eachProvider.getProvider());
			return updateRepositories;
		}
		
		try {
			JSONArray repositoriesArray = new JSONArray(repositoryResponse.getBody());
			List<Object> repositories = JsonUtils.toList(repositoriesArray);

			for (Object eachRepository : repositories) {
				Map<String, Object> eachRepositoryRow = (Map<String, Object>) eachRepository;
				Repository updateRepository = new Repository();
				Map<String, Object> ownerDetails = (Map<String, Object>) eachRepositoryRow
						.get(eachProvider.getOwnerKey());
				String ownerName = ownerDetails.get(eachProvider.getUseidKey()).toString();
				if(searchOwner != null && !searchOwner.equalsIgnoreCase(ownerName))
				{
					continue;
				}	
				updateRepository.setProvider(eachProvider.getProvider());
				updateRepository.setId(eachRepositoryRow.get(eachProvider.getIdKey()).toString());
				updateRepository.setName(eachRepositoryRow.get(eachProvider.getNameKey()).toString());
				
				updateRepository.setOwner(ownerName);
				updateRepository.setHttpsUrl(eachRepositoryRow.get(eachProvider.getHtmlUrlKey()).toString());
				updateRepository.setSshUrl(eachRepositoryRow.get(eachProvider.getSshUrlKey()).toString());
				updateRepositories.add(updateRepository);
			}

		} catch (JSONException e) {
			loggger.warn("Incompatible response format {}", e.getMessage());
		}
		return updateRepositories;
	}
}
