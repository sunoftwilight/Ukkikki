package project.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.domain.directory.service.TrashService;
import project.domain.party.service.PartyService;
import project.domain.alarm.service.AlarmService;

@RequiredArgsConstructor
@Component
@Slf4j
public class Scheduler {


    private final PartyService partyService;
    private final AlarmService alarmService;
    private final TrashService trashService;

    @Scheduled(fixedRate = 1000 * 3600 * 2) // 2H
    public void redisLinkDelete() {
        partyService.linkDelete();
    }

    @Scheduled(fixedRate = 1000 * 30) // 30S
    public void checkEmitter(){
        alarmService.checkEmitterLive();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void dailyTrashBinCleanUp() {trashService.dailyTrashCleanUp();}
}
