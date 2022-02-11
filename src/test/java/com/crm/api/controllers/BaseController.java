package com.crm.api.controllers;

import com.crm.api.security.jwt.AuthEntryPointJwt;
import com.crm.api.security.jwt.JwtUtils;
import com.crm.api.security.services.RefreshTokenService;
import com.crm.api.security.services.UserDetailsServiceImpl;
import com.crm.api.service.RoleService;
import com.crm.api.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

class BaseController {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @MockBean
    AuthService userService;

    @MockBean
    RoleService roleService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    JwtUtils jwtUtils;


}
