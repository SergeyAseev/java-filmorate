package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {

	private final UserDbService userService;
	private final FilmDbService filmService;

	private User user1 = new User(
			1L,
			"user1",
			"user1@gmail.ru",
			"userName1",
			LocalDate.of(2022, 7, 31)
	);
	private User user2 = new User(
			2L,
			"user2",
			"user2@gmail.ru",
			"userName2",
			LocalDate.of(1998, 8, 1)
	);

	@Test
	public void test1_addUser() {
		assertTrue(userService.retrieveAllUsers().isEmpty());
		userService.addUser(user1);
		userService.addUser(user2);
		assertEquals(2, userService.retrieveAllUsers().size());
	}
}