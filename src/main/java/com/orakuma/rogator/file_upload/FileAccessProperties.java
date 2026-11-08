package com.orakuma.rogator.file_upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Who may touch an application's files besides the application's requestor/assignee.
 *
 * @param staffAuthority Spring Security authority (e.g. {@code ROLE_staff}) granting access to
 *     every application's files. Blank disables the staff override. Note the default JWT converter
 *     only exposes {@code SCOPE_*} authorities; Keycloak realm/client roles need a custom
 *     {@code JwtAuthenticationConverter} to appear here.
 */
@ConfigurationProperties(prefix = "app.files.access")
public record FileAccessProperties(String staffAuthority) {}
