package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Feed {

    private final long eventId;
    private final long userId;
    private final long entityId;
    private final long timeTs;
    private final EventEnum eventEnum;
    private final OperationEnum operationEnum;

}
