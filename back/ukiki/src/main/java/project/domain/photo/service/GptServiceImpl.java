package project.domain.photo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.entity.Meta;
import project.domain.photo.entity.MetaCode;
import project.domain.photo.entity.Photo;
import project.domain.photo.repository.MetaRepository;
import project.global.util.gptutil.GptUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptServiceImpl implements GptService{

    private final GptUtil gptUtil;
    private final MetaRepository metaRepository;

    @Async
    public void processGptApiAsync(Photo photo, MultipartFile file) {
        log.info("Starting async GPT processing");
        Thread.currentThread().getName();
        long start = System.nanoTime();
        try {
            for (Integer code : gptUtil.postChat(file)) {
                metaRepository.save(
                    Meta.builder()
                        .photo(photo)
                        .metaCode(MetaCode.getEnumByCode(code))
                        .build()
                );
            }
        } catch (Exception e) {
            log.error("Error during async GPT processing", e);
        }

        long end = System.nanoTime();
        log.info("duration = {}", end - start);
        log.info("Completed async GPT processing");
    }
}
