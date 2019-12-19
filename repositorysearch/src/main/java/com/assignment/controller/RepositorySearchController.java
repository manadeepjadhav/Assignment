package com.assignment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.manager.RepositorySearchManager;
import com.assignment.model.Repository;



@RestController
public class RepositorySearchController {
	
	@Autowired
	private RepositorySearchManager repoManager;
	
	@RequestMapping("/repositories")
    public List<Repository> getEmployees(@RequestParam (value = "owner", required = false) String searchOwner) 
    {
		List<Repository> repositoryList = new ArrayList<Repository>();
		repositoryList.addAll(repoManager.getProviderRepositories(searchOwner));
		return repositoryList;
    }

}
