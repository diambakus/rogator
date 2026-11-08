package com.orakuma.rogator.file_upload;

import com.orakuma.rogator.application.ApplicationEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Ownership rules for application files. The caller is identified by the JWT subject
 * ({@link Authentication#getName()}), compared with {@link ApplicationEntity#getRequestorId()} and
 * {@link ApplicationEntity#getAssigneeId()}.
 *
 * <ul>
 *   <li>read: requestor, assignee or staff
 *   <li>upload: requestor or staff
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class FileAccessPolicy {

  private final Environment environment;
  private final FileAccessProperties properties;

  public boolean canRead(ApplicationEntity application) {
    return bypass()
        || isStaff()
        || isSubject(application.getRequestorId())
        || isSubject(application.getAssigneeId());
  }

  public boolean canUpload(ApplicationEntity application) {
    return bypass() || isStaff() || isSubject(application.getRequestorId());
  }

  /** The "noauth" profile disables security entirely (local development only). */
  private boolean bypass() {
    return environment.acceptsProfiles(Profiles.of("noauth"));
  }

  private boolean isStaff() {
    Authentication auth = current();
    String staff = properties.staffAuthority();
    return auth != null
        && StringUtils.isNotBlank(staff)
        && auth.getAuthorities().stream().anyMatch(a -> staff.equals(a.getAuthority()));
  }

  private boolean isSubject(String ownerId) {
    Authentication auth = current();
    return auth != null && StringUtils.isNotBlank(ownerId) && ownerId.equals(auth.getName());
  }

  private static Authentication current() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
      return null;
    }
    return auth;
  }
}
