package com.example.checklistservice.entity.service;

import com.example.checklistservice.entity.dto.HomeFolderRes;
import com.example.checklistservice.entity.dto.RecordFolderRes;
import com.example.checklistservice.entity.dto.UpdateFolderReq;

import java.util.List;

public interface FolderService {
    List<HomeFolderRes> getHomeFolders();

    List<RecordFolderRes> getRecordFolders();

    void updateFolder(Long folderId, UpdateFolderReq updateFolderReq);

    void deleteFolder(Long folderId);
}
