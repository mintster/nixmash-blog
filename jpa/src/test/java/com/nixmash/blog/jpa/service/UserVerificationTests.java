package com.nixmash.blog.jpa.service;

import com.nixmash.blog.jpa.SpringDataTests;
import com.nixmash.blog.jpa.TestUtil;
import com.nixmash.blog.jpa.dto.UserDTO;
import com.nixmash.blog.jpa.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@Transactional
public class UserVerificationTests extends SpringDataTests {


    @Autowired
    private UserService userService;

    @Test
    public void userSavedWithEnabledFalse() {
        UserDTO userDTO = TestUtil.createTestUserDTO("user121528", "bumb", "bammer", "user121528@aol.com");
        userDTO.setEnabled(false);
        User user = userService.create(userDTO);
        assertFalse(user.isEnabled());
        assertNotNull(user.getUserData().getCreatedDatetime());
        assertNull(user.getUserData().getApprovedDatetime());
    }

    @Test
    public void canRetrieveUserByUserKey() throws Exception {
        //  Scott Schoenberg retrieved for UserKey 'Fx05XbWjPFECJZQP'
        Optional<User> user = userService.getByUserKey("Fx05XbWjPFECJZQP");
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void nonExistentUserKeyReturnsEmptyUser() throws Exception {
        //  No one retrieved for UserKey '12345'
        Optional<User> user = userService.getByUserKey("12345");
        assertThat(user.isPresent(), is(false));
    }
}
