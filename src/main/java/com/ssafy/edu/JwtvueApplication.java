package com.ssafy.edu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ssafy.edu.image.model.service.BoardFileService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class JwtvueApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(JwtvueApplication.class, args);
	}
	
	@Resource
	private  BoardFileService storageService;
	
    @Override
    public void run(String... arg) throws Exception {
    	storageService.init();
    }
	
}
