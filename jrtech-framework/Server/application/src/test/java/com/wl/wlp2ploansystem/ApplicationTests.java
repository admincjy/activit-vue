package com.wl.wlp2ploansystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;
	@Test
	public void contextLoads() throws JsonProcessingException {

		RestObject  user = new RestObject();
		user.setTimestamp(LocalDateTime.now());

		String json = objectMapper.writeValueAsString(user);


	}

}
