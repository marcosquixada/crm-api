package com.crm.api.controllers;

import com.crm.api.controller.AuthController;
import com.crm.api.model.ERole;
import com.crm.api.model.Role;
import com.crm.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends BaseController {

    @Test
   void save() throws Exception {
        String url = "/api/v1/auth/signup";
        User user = new User("marcos", "marcos@marcos.com", "123456");
        String payload = om.writeValueAsString(user);
        when(roleService.findByName(any())).thenReturn(Optional.of(new Role(ERole.ROLE_ADMIN)));
        when(userService.save(user)).thenReturn(user);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload)
            ).andExpect(status().isOk()
        ).andReturn();
    }
}
