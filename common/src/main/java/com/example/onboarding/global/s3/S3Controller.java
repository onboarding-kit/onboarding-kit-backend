package com.example.onboarding.global.s3;

import com.example.onboarding.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    private final S3Service s3Service;

    /**
     * S3 이미지 업로드
     * @param multipartFile upload file
     * @return 이미지 S3 url
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> upload(@RequestPart(value = "image") MultipartFile multipartFile) {
        Map<String, String> url = s3Service.upload(multipartFile);
        return ResponseEntity.ok(new ApiResponse("200", "Success", url));
    }
}
