package com.nilotpal.api.userapi.repository;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nilotpal.api.userapi.entity.UserEntity;
import com.nilotpal.api.userapi.model.UpdateUserRequest;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {

	@Autowired
	TestEntityManager testEntityManager;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
		List<UserEntity> dummyUsers = new ArrayList<>();
		dummyUsers.add(new UserEntity("test1@testing.com", "TestUser1", "password", "testusername1"));
		dummyUsers.add(new UserEntity("test2@testing.com", "TestUser2", "password", "testusername2"));
		dummyUsers.add(new UserEntity("test3@testing.com", "TestUser3", "password", "testusername3"));
		dummyUsers.add(new UserEntity("test4@testing.com", "TestUser4", "password", "testusername4"));
		dummyUsers.add(new UserEntity("test5@testing.com", "TestUser5", "password", "testusername5"));
		dummyUsers.add(new UserEntity("test6@testing.com", "TestUser6", "password", "testusername6"));
		dummyUsers.add(new UserEntity("test7@testing.com", "TestUser7", "password", "testusername7"));
		dummyUsers.add(new UserEntity("test8@testing.com", "TestUser8", "password", "testusername8"));
		dummyUsers.stream().forEach(testEntityManager::persist);
		testEntityManager.flush();
	}

	@Test
	public void testFindAll() {
		List<UserEntity> users = userRepository.findAll();
		assertThat(users, hasSize(16));
	}

	@Test
	public void testFindAllPaged() {
		PageRequest request = PageRequest.of(0, 3, Sort.by("id"));
		List<UserEntity> users = userRepository.findAll(request).getContent();
		List<Long> userIds = users.stream().map(e -> e.getId()).collect(Collectors.toList());
		assertThat(userIds, hasSize(3));
		assertThat(userIds, hasItems(1L, 2L, 3L));

		request = (PageRequest) request.next();
		users = userRepository.findAll(request).getContent();
		userIds = users.stream().map(e -> e.getId()).collect(Collectors.toList());
		assertThat(userIds, hasSize(3));
		assertThat(userIds, hasItems(4L, 5L, 6L));
	}

	@Test
	public void testFindById() {
		Optional<UserEntity> userOptional = userRepository.findById(1L);
		UserEntity user = userOptional.get();
		assertNotNull(user);
		assertEquals("test1@testing.com", user.getEmail());
		assertEquals("TestUser1", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("testusername1", user.getUsername());
	}

	@Test
	public void testFindById_UserNonExistent() {
		Optional<UserEntity> userOptional = userRepository.findById(15L);
		assertEquals(Optional.empty(), userOptional);
	}

	@Test
	public void testCreateUser() {
		UserEntity user = userRepository
			.save(new UserEntity("createduser@testing.com", "Created User", "password", "createduser"));
		assertNotNull(user);
		assertNotNull(user.getId());
		assertEquals("createduser@testing.com", user.getEmail());
		assertEquals("Created User", user.getName());
		assertEquals("password", user.getPassword());
		assertEquals("createduser", user.getUsername());
	}

	@Test
	@Transactional
	public void testUpdateUser() {
		Optional<UserEntity> optionalUser = userRepository.findById(1L);
		UserEntity userToBeUpdated = optionalUser.get();
		userToBeUpdated = userToBeUpdated
			.merge(new UpdateUserRequest("updateduser@testing.com", "Updated User", 
			"password", "updateduser"));

		optionalUser = userRepository.findById(1L);
		UserEntity userUpdated = optionalUser.get();

		assertNotNull(userUpdated);
		assertThat(userUpdated.getId(), is(1L));
		assertEquals("updateduser@testing.com", userUpdated.getEmail());
		assertEquals("Updated User", userUpdated.getName());
		assertEquals("password", userUpdated.getPassword());
		assertEquals("updateduser", userUpdated.getUsername());
	}

	@Test
	@Transactional
	public void testDeleteUser() {
		Optional<UserEntity> optionalUser = userRepository.findById(1L);
		UserEntity userToBeDeleted = optionalUser.get();
		userRepository.delete(userToBeDeleted);
		List<UserEntity> users = userRepository.findAll();
		assertEquals(15, users.stream().count());
		List<Long> userIds = users.stream().map(e -> e.getId()).collect(Collectors.toList());
		assertThat(userIds, everyItem(greaterThan(1L)));
	}
}
