package com.vshare.vshare.feature.voice.boundary.dto.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class AudioFileValidator implements ConstraintValidator<IsAudio, MultipartFile> {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "audio/mpeg",
            "audio/wav",
            "audio/x-wav",
            "audio/ogg",
            "audio/mp4"
    );
    @Override
    public boolean isValid(MultipartFile audio, ConstraintValidatorContext constraintValidatorContext) {
        return ALLOWED_TYPES.contains(audio.getContentType());
    }
}
