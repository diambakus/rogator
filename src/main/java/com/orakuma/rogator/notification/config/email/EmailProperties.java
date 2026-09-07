package com.orakuma.rogator.notification.config.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail-config")
public record EmailProperties(String host, Integer port, String encrypt, User user) {
  record User(String email, String username, char[] password) {}
}
