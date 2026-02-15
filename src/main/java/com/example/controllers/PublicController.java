package com.example.controllers;

import com.example.models.Permission;
import com.example.models.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", new Date().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles")
    public ResponseEntity<Map<String, Object>> getRolesInfo() {
        Map<String, Object> rolesInfo = new HashMap<>();

        for (Role role : Role.values()) {
            Map<String, Object> roleData = new HashMap<>();
            roleData.put("name", role.name());
            roleData.put("permissions", role.getPermissions().stream()
                    .map(Permission::getPermission)
                    .collect(Collectors.toList()));
            roleData.put("authorities", role.getAuthorities());
            rolesInfo.put(role.name(), roleData);
        }

        return ResponseEntity.ok(rolesInfo);
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Map<String, String>>> getPermissionsInfo() {
        List<Map<String, String>> permissions = new ArrayList<>();

        for (Permission permission : Permission.values()) {
            Map<String, String> permData = new HashMap<>();
            permData.put("name", permission.name());
            permData.put("permission", permission.getPermission());
            permissions.add(permData);
        }

        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("authenticated", true);
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(userInfo);
    }
}

