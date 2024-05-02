package project.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.domain.party.redis.PartyLink;
import project.domain.party.repository.PartyLinkRedisRepository;
import project.domain.party.service.PartyService;
import project.domain.sse.service.SseService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class Scheduler {


    private final PartyService partyService;
    private final SseService sseService;

    @Scheduled(fixedRate = 1000 * 3600 * 2) // 2H
    public void redisLinkDelete() {
        partyService.linkDelete();
    }

    @Scheduled(fixedRate = 1000 * 60 * 10) // 10M
    public void checkEmitter(){
        sseService.checkEmitterLive();
    }
}
