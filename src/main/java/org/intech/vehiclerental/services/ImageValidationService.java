package org.intech.vehiclerental.services;

import lombok.RequiredArgsConstructor;
import org.intech.vehiclerental.exceptions.InvalidImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageValidationService {

    @DataSizeUnit(DataUnit.MEGABYTES)
    @Value("${app.vehicle.max-image-size-mb}")
    private DataSize maxImageSize;

    /**
     * Alternative way to inject value in mb using Spring Expression Language (SpEL)
     * Keeping it commented for learning purpose
     */
//    @Value("#{${app.vehicle.max-image-size-mb} * 1024 * 1024}")
//    private long maxImageSize;

    public void validateImages(List<MultipartFile> images, Integer primaryImageIndex) {

        if (images == null || images.isEmpty()) {
            throw new InvalidImageException("At least one vehicle image is required");
        }

        if (primaryImageIndex < 0 || primaryImageIndex >= images.size()) {
            throw new InvalidImageException("Invalid primary image index");
        }

        for (MultipartFile image : images) {

            if (image.isEmpty()) {
                throw new InvalidImageException("Empty image file detected");
            }

            if (image.getSize() > maxImageSize.toBytes()) {
                throw new InvalidImageException(
                        "Image size must not exceed " + maxImageSize.toMegabytes() + "MB: "
                                + image.getOriginalFilename()
                );
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new InvalidImageException(
                        "Invalid file type: " + image.getOriginalFilename()
                );
            }
        }
    }
}