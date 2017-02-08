package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.SpringDataTests;
import com.nixmash.blog.jpa.dto.UserDTO;
import com.nixmash.blog.jpa.model.User;
import com.nixmash.blog.jpa.repository.UserDataRepository;
import com.nixmash.blog.jpa.utils.UserTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by daveburke on 12/20/16.
 */
@RunWith(SpringRunner.class)
@Transactional
public class UserRegistrationTests extends SpringDataTests {

    private final static Boolean NOT_ENABLED = false;
    @Autowired
    UserService userService;

    @Autowired
    UserDataRepository userDataRepository;

    @Test
    public void userDataIsNotNull() throws Exception {
        // H2Data Tommy loginAttempts = 2
        User tommy = userService.getUserByIdWithDetail(7L).get();
        assertEquals(userDataRepository.findByUserId(7L).getLoginAttempts(), 2);
    }

    @Test
    public void newUser_NotYetEmailApproved_IsNotEnabled() throws Exception {
        UserDTO userDTO = UserTestUtils.newUserDTO(46, NOT_ENABLED);
        User user = userService.create(userDTO);
        assertFalse(user.isEnabled());
        assertNull(user.getUserData().getApprovedDatetime());
    }

    @Test
    public void newUser_AfterEmailVerification_IsEnabled() throws Exception {
        UserDTO userDTO = UserTestUtils.newUserDTO(56, NOT_ENABLED);
        User user = userService.enableAndApproveUser(userService.create(userDTO));
        assertTrue(user.isEnabled());
        assertNotNull(user.getUserData().getApprovedDatetime());
    }




}
