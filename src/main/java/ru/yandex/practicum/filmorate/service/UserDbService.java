package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("UserDbService")
public class UserDbService implements UserService{

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public User addUser(User user) {
        validate(user);
        userStorage.createUser(user);
        log.info("Добавлен пользователь с ID = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        retrieveUserById(user.getId());
        log.info("Обновлен пользователь с ID = {}", user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        retrieveUserById(friendId);
        userStorage.addFriends(userId, friendId);
        log.info("Добавлен друг с ID = {} пользователю с ID = {}", friendId, userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userStorage.removeFromFriends(userId, friendId);
        log.info("Удален друг с ID = {} у пользователя с ID = {}", friendId, userId);
    }

    @Override
    public void removeUserById(long userId) {
        userStorage.removeUserById(userId);
        log.info("Удален пользователь с ID = {}", userId);
    }


    @Override
    public User retrieveUserById(long userId) {
        return userStorage.retrieveUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден пользователь с ID %s", userId)));
    }

    @Override
    public List<User> retrieveAllUsers() {
        log.info("Возвращаем всех пользователей");
        return userStorage.retrieveAllUsers();
    }

    @Override
    public List<User> retrieveFriends(long userId) {
        log.info("Возвращаем друзей пользователя с ID = {}", userId);
        return userStorage.getFriends(userId);
    }

    @Override
    public List<User> retrieveCommonFriends(long userId, long friendId) {
        log.info("Возвращаем общих друзей пользователей с ID = {} и с ID = {}", userId, friendId);
        return userStorage.getCommonFriends(userId, friendId);
    }


    private Set<Long> getTargetFilms (long id) {
        ArrayList<Long> usersIds = new ArrayList<>();
        List<User> allUsers = retrieveAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            usersIds.add(allUsers.get(i).getId());
        }
        Map<Long, List<Long>> usersLikes = new HashMap<>();
        Map<Long, Integer> intersectionLikes = new HashMap<>();
        for (int i = 0; i < usersIds.size(); i++) {
            List<Long> commonFilmsIds = new ArrayList<>();
            commonFilmsIds = userStorage.getLikesByUser(usersIds.get(i));
            usersLikes.put(usersIds.get(i), commonFilmsIds);
            List<Long> user = usersLikes.get(id);
            List<Long> otherUser = new ArrayList<>();
            otherUser = usersLikes.get(i);
                Set<Long> intersection = new HashSet<>(user);
                if (intersection.size() == 0) {
                    throw new NotFoundException("Пересечений не найдено");
                }
            if (otherUser != null) {
                intersection.retainAll(otherUser); // нашли пересечения
                intersectionLikes.put(usersIds.get(i), intersection.size());
            }
        }
        if (intersectionLikes.size() != 0) {
            intersectionLikes.values().stream().sorted();
        }
        Set<Long> targetFilms = new HashSet<>();

               List<Long> targetUserFilms = new ArrayList<>();
               targetUserFilms = userStorage.getLikesByUser(intersectionLikes.get(0));
               if (targetUserFilms != null) {
                   List<Long> userFilms = userStorage.getLikesByUser(id);
                   Set<Long> targetFilms2 = new HashSet<>(targetUserFilms);
                   targetFilms = targetFilms2;
                   targetFilms.removeAll(userFilms);
               }
            return targetFilms;
    }
        /*
        Comparator<Set<Long>> lengthComparator = new Comparator<Set<Long>>() {
            public int compare(Set<Long> a, Set<Long> b) {
                return a.size() - b.size();
            }
        };
        Collections.sort(intersectionLikes, lengthComparator);
        return intersectionLikes;
    }



        for (Long userId : usersLikes.get(id)) {
        Set<Long> user2 = usersLikes.get(user2);
        Set<Long> likeCount = new HashSet<>();
        if (user2.contains(userId)) {
            likeCount.add(user2.stream().count());
        }
                HashMap<Long, Set<Long>> diff = new HashMap<>();
                HashMap<Long,  Set<Long>> freq = new HashMap<>();
                    diff.put(, likeCount);
                    freq.put((Long) e.getKey(), new HashMap<List<Long>;

                    for (Map.Entry<List<Long>, Integer> e2 : user.entrySet()) {
                        int oldCount = 0;
                        if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                            oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                        }
                        for (Long j : diff.keySet()) {
                            for (List<Long> i : diff.get(j).keySet()) {
                                int oldValue = (int) diff.get(j).get(i).doubleValue();
                                int count = freq.get(j).get(i).intValue();
                                diff.get(j).put(i, oldValue / count);

                            }
                        double oldDiff = 0.0;
                        if (diff.get(e.getKey()).containsKey(e2.getKey())){
                            oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                        }
                        System.out.println(e.getValue());
                        System.out.println(e2.getValue());
                        double observedDiff = e.getValue() - e2.getValue();
                        freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                        diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                        }
                    }
                }

        for (Map.Entry<Long, Set<List<Long>>> e2 : usersLikes.entrySet()) {
            for (List<Long> j : e2.getValue().keySet()) {
                for (List<Long> k : diff.keySet()) {
                    long predictedValue =
                            (long) (diff.get(k).get(j).doubleValue() + e2.getValue().get(j).doubleValue());
                    long finalValue = predictedValue * freq.get(k).get(j).intValue();
                    HashMap<List<Long> , Long> uPred = new HashMap<>();
                    HashMap<List<Long> , Long> uFreq = new HashMap<>();
                    uPred.put(k, uPred.get(k) + finalValue);
                    uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
            HashMap<K, V> clean = new HashMap<Object, Object>();
            for (List<Long> l : uPred.keySet()) {
                if (uFreq.get(l) > 0) {
                    clean.put(l, uPred.get(l).doubleValue() / uFreq.get(l).intValue());
                }
            }
            for (K j : InputData.items) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                    }
            }
        }

        return 3;
    }

 */


    @Override
    public List<Optional<Film>> getRecommendations(long id) {
        Set<Long> targetFilms = getTargetFilms(id);
        List<Optional<Film>> recommendations = new ArrayList<>();
        for (int i = 0; i < targetFilms.size(); i++) {
            for (long filmId : targetFilms) {
                recommendations.add(filmStorage.retrieveFilmById(filmId));
            }
        }
        return recommendations;
    }

    protected void validate(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения пользователя %s не может быть позже текущей даты");
        }
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email пользователя %s некорретный");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("login пользователя %s не может быть пустым");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}

