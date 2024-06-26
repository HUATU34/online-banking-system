package com.zjuse.bankingsystem.controller.loan;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjuse.bankingsystem.service.loan.OfficerLoginService;
import com.zjuse.bankingsystem.service.loan.OfficerService;

import lombok.extern.slf4j.Slf4j;

import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.security.service.UserCacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@PreAuthorize("@roleCheck.isRole('OFFICER')")
public class OfficerController {

    @Autowired
    private OfficerService officerService;
    @Autowired
    private OfficerLoginService loginService;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private UserCacheManager userCacheManager; 

    @PutMapping("/officer-main/update-officer-password-by-officer")
    public String updateOfficerPasswordByOfficer(@RequestParam String currentPassword, @RequestParam String newPassword) {
        String officerUsername;
        officerUsername = (String)currentUserService.getCurrentUsername().payload;
        Officer officer = loginService.findOfficerByUsername(officerUsername);
        int officer_id = officer.getOfficerId();
        userCacheManager.cleanUserCache("OFFICER-" + officerUsername);


        int result = officerService.updatePassword(officer_id,currentPassword,newPassword);
        if (result > 0) {
            return "Password updated successfully!";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Current password is not correct");
        }
    }


}

