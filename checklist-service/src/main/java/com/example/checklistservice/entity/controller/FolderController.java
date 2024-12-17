package com.example.checklistservice.entity.controller;

import com.example.checklistservice.entity.dto.HomeFolderRes;
import com.example.checklistservice.entity.dto.RecordFolderRes;
import com.example.checklistservice.entity.dto.UpdateFolderReq;
import com.example.checklistservice.entity.service.FolderService;
import com.example.common.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<?>> getHomeFolders() {
        List<HomeFolderRes> homeFolders = folderService.getHomeFolders();
        return ResponseEntity.ok(ApiResponse.success(homeFolders));
    }

    @GetMapping("/records")
    public ResponseEntity<ApiResponse<?>> getRecordFolders() {
        List<RecordFolderRes> recordFolders = folderService.getRecordFolders();
        return ResponseEntity.ok(ApiResponse.success(recordFolders));
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<ApiResponse<?>> updateFolder(@PathVariable Long folderId,
                                                       @Valid UpdateFolderReq updateFolderReq) {
        folderService.updateFolder(folderId, updateFolderReq);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<ApiResponse<?>> updateFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
