package project.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;
import project.domain.photo.entity.Photo;

public interface GptService {

    public void processGptApiAsync(Photo photo, MultipartFile file);

}
