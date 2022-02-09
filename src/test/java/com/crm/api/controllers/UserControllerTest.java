package com.crm.api.controllers;

import com.crm.api.controller.UserController;
import com.crm.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends BaseController {

    @Test
   public void save() throws Exception {
        String url = "/api/v1/users";
        User user = new User("marcos", "marcos@marcos.com", "123456");
        String payload = om.writeValueAsString(user);
        when(userService.save(user)).thenReturn(user);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload)
            ).andExpect(status().isOk()
        ).andReturn();
    }
}
