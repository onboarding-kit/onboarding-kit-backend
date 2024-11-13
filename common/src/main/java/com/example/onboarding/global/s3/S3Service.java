package com.example.onboarding.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.onboarding.global.exception.BaseException;
import com.example.onboarding.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Map<String, String> upload(MultipartFile multipartFile) {
        try {
            File uploadFile = convertToFile(multipartFile)
                    .orElseThrow(() -> new BaseException(ErrorCode.FAILED_CONVERT_FILE));

            String imgUrl = uploadToS3(uploadFile);

            removeLocalFile(uploadFile);  // 로컬에 생성된 파일 삭제

            return Map.of("imgUrl", imgUrl);  // 업로드된 이미지의 URL 반환
        } catch (IOException e) {
            throw new BaseException(ErrorCode.FAILED_CONVERT_FILE);
        }
    }
    //S3 file upload
    private String uploadToS3(File uploadFile) {
        String fileName = UUID.randomUUID().toString();  // 고유한 파일 이름 생성
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)  // PublicRead 권한으로 업로드
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();  // S3 URL 반환
    }

    //local file remove
    private void removeLocalFile(File file) {
        if (file.delete()) {
            log.info("local file remove success: {}", file.getName());
        } else {
            log.error("local file remove fail: {}", file.getName());
        }
    }

    //MultipartFile -> File
    private Optional<File> convertToFile(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
