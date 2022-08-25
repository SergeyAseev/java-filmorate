package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

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

	private final User user1 = new User(
			1L,
			"user1",
			"user1@gmail.ru",
			"userName1",
			LocalDate.of(2022, 7, 1)
	);
	private final User user2 = new User(
			2L,
			"user2",
			"user2@gmail.ru",
			"userName2",
			LocalDate.of(2022, 6, 1)
	);

	private final User user3 = new User(
			3L,
			"user3",
			"user3@gmail.ru",
			"userName3",
			LocalDate.of(2022, 5, 1)
	);

	private final Film film1 = new Film(
			1L,
			"name1",
			"description1",
			LocalDate.of(2022, 8, 1),
			120
	);

	private final Film film2 = new Film(
			2L,
			"name2",
			"description2",
			LocalDate.of(2022, 7, 1),
			100
	);

	@Test
	public void addUserTest() {

		assertTrue(userService.retrieveAllUsers().isEmpty());
		userService.addUser(user1);
		userService.addUser(user2);
		assertEquals(2, userService.retrieveAllUsers().size());
	}

	@Test
	public void updateUserTest() {

		userService.addUser(user1);
		User user = userService.retrieveUserById(1L);
		user.setName("new name");
		user.setEmail("new@gmail.com");
		user.setBirthday(LocalDate.of(2022, 1, 1));
		userService.updateUser(user);
		assertEquals("new name", userService.retrieveUserById(1L).getName());
		assertEquals("new@gmail.com", userService.retrieveUserById(1L).getEmail());
		assertEquals(LocalDate.of(2022, 1, 1), userService.retrieveUserById(1L)
				.getBirthday());
	}

	@Test
	public void friendsUserTest() {

		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		userService.addFriend(user1.getId(), user2.getId());
		userService.addFriend(user1.getId(), user3.getId());
		userService.addFriend(user2.getId(), user3.getId());

		assertEquals(2, userService.retrieveFriends(user1.getId()).size());
		assertEquals(1, userService.retrieveCommonFriends(user1.getId(), user2.getId()).size());

		userService.removeFriend(user2.getId(), user3.getId());
		assertTrue(userService.retrieveFriends(user2.getId()).isEmpty());

	}

	@Test
	public void removeUserTest() {
		userService.addUser(user1);
		userService.addUser(user2);
		assertEquals(2, userService.retrieveAllUsers().size());
		userService.removeUserById(user2.getId());
		assertEquals(1, userService.retrieveAllUsers().size());
	}

	@Test
	public void addFilmTest() {

		assertTrue(filmService.retrieveAllFilms().isEmpty());
		film1.setMpa(new Mpa(1, "G"));
		filmService.addFilm(film1);
		film2.setMpa(new Mpa(2, "PG"));
		filmService.addFilm(film2);
		assertEquals(2, filmService.retrieveAllFilms().size());
	}

	@Test
	public void updateFilmTest() {

		film1.setMpa(new Mpa(1, "G"));
		filmService.addFilm(film1);
		Film film = filmService.retrieveFilmById(1L);
		film.setName("new name");
		film.setDescription("new description");
		film.setReleaseDate(LocalDate.of(2022, 1, 1));
		film.setDuration(60);
		filmService.updateFilm(film);
		assertEquals("new name", filmService.retrieveFilmById(1L).getName());
		assertEquals("new description", filmService.retrieveFilmById(1L).getDescription());
		assertEquals(LocalDate.of(2022, 1, 1), filmService.retrieveFilmById(1L)
				.getReleaseDate());
		assertEquals(60, filmService.retrieveFilmById(1L).getDuration());
	}

	@Test
	public void removeFilmTest() {

		film1.setMpa(new Mpa(1, "G"));
		filmService.addFilm(film1);
		film2.setMpa(new Mpa(2, "PG"));
		filmService.addFilm(film2);
		assertEquals(2, filmService.retrieveAllFilms().size());
		filmService.removeFilmById(film2.getId());
		assertEquals(1, filmService.retrieveAllFilms().size());
	}

	@Test
	public void genreTest() {
		assertEquals(6, filmService.retrieveAllGenres().size());
		assertEquals("Комедия", filmService.retrieveGenreById(1).getName());
	}

	@Test
	public void mpaTest() {
		assertEquals(5, filmService.retrieveAllMpaRatings().size());
		assertEquals("G", filmService.retrieveMpaRatingById(1).getName());
	}

	@Test
	public void likesTest() {

		film1.setMpa(new Mpa(1, "G"));
		filmService.addFilm(film1);
		film2.setMpa(new Mpa(2, "PG"));
		filmService.addFilm(film2);

		userService.addUser(user1);
		userService.addUser(user2);

		assertEquals(0, filmService.retrieveFilmById(film1.getId()).getLikes().size());

		filmService.addLike(1, 1);
		filmService.addLike(1, 2);
		filmService.addLike(2, 1);
		assertEquals(2, filmService.retrieveFilmById(film1.getId()).getLikes().size());

		//assertEquals(2,filmService.returnPopularFilms(2).size());

		filmService.removeLike(2, 1);
		assertEquals(0, filmService.retrieveFilmById(film2.getId()).getLikes().size());

	}
}