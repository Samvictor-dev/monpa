package com.myvamsnet.monpa.controller.admin;

import com.myvamsnet.monpa.dto.admin.AdminUserFilter;
import com.myvamsnet.monpa.dto.admin.AdminUserResponse;
import com.myvamsnet.monpa.dto.common.PagedResponse;
import com.myvamsnet.monpa.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public PagedResponse<AdminUserResponse> getUsers(

            @ModelAttribute
            AdminUserFilter filter,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size

    ) {

        return adminUserService.getUsers(
                filter,
                page,
                size
        );

    }

}
