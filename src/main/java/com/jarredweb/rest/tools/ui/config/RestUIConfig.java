package com.jarredweb.rest.tools.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jarredweb.plugins.rest.tools.config.RestToolsConfig;
import com.jarredweb.plugins.users.config.UsersServiceConfig;

@Configuration
@Import({RestToolsConfig.class, UsersServiceConfig.class})
public class RestUIConfig {}
