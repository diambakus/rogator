package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.application.ApplicationEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

class FileAccessPolicyTest {

  private final ApplicationEntity app = new ApplicationEntity();

  FileAccessPolicyTest() {
    app.setRequestorId("req-1");
    app.setAssigneeId("emp-1");
  }

  @AfterEach
  void clear() {
    SecurityContextHolder.clearContext();
  }

  private FileAccessPolicy policy(String staffAuthority, String... profiles) {
    MockEnvironment env = new MockEnvironment();
    env.setActiveProfiles(profiles);
    return new FileAccessPolicy(env, new FileAccessProperties(staffAuthority));
  }

  private void login(String sub, String... authorities) {
    SecurityContextHolder.getContext()
        .setAuthentication(new TestingAuthenticationToken(sub, "n/a", authorities));
  }

  @Test
  void requestorCanReadAndUpload() {
    login("req-1");
    assertTrue(policy(null).canRead(app));
    assertTrue(policy(null).canUpload(app));
  }

  @Test
  void assigneeCanReadButNotUpload() {
    login("emp-1");
    assertTrue(policy(null).canRead(app));
    assertFalse(policy(null).canUpload(app));
  }

  @Test
  void strangerAndAnonymousAreDenied() {
    login("someone-else");
    assertFalse(policy(null).canRead(app));
    SecurityContextHolder.clearContext();
    assertFalse(policy(null).canRead(app));
  }

  @Test
  void nullOwnersNeverMatch() {
    app.setRequestorId(null);
    app.setAssigneeId(null);
    login("anything");
    assertFalse(policy(null).canRead(app));
  }

  @Test
  void staffAuthorityGrantsAccessOnlyWhenConfigured() {
    login("clerk", "ROLE_staff");
    assertTrue(policy("ROLE_staff").canRead(app));
    assertFalse(policy(null).canRead(app));
    assertFalse(policy("").canRead(app));
  }

  @Test
  void noauthProfileBypasses() {
    assertTrue(policy(null, "noauth").canUpload(app));
  }
}
