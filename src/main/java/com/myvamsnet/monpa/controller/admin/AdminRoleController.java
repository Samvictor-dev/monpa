package com.myvamsnet.monpa.controller.admin;

import com.myvamsnet.monpa.service.AdminRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @PatchMapping("/{id}/promote")
    public ResponseEntity<Void> promote(
            @PathVariable Long id
    ) {

        adminRoleService.promoteToAdmin(id);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}/demote")
    public ResponseEntity<Void> demote(
            @PathVariable Long id
    ) {

        adminRoleService.demoteAdmin(id);

        return ResponseEntity.noContent().build();

    }

}
