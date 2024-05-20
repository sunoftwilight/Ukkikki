package project.global.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class ImageUtil {

    public BufferedImage resizeImage(MultipartFile file, int thumbnailNumber) {
        //todo : 썸네일 생성
        int width = 0;
        int height = 0;
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(thumbnailNumber == 1){
            width = inputImage.getWidth() / 2;
            height = inputImage.getHeight() / 2;
        }
        if(thumbnailNumber == 2){
            width = inputImage.getWidth() / 4;
            height = inputImage.getHeight() / 4;
        }

        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
    }
}
