package com.example.checklistservice.entity.service;

import com.example.checklistservice.entity.CheckList;
import com.example.checklistservice.entity.Folder;
import com.example.checklistservice.entity.dto.HomeFolderRes;
import com.example.checklistservice.entity.dto.RecordFolderRes;
import com.example.checklistservice.entity.dto.UpdateFolderReq;
import com.example.checklistservice.entity.repository.FolderRepository;
import com.example.common.global.exception.BaseException;
import com.example.common.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    /**
     * 홈 폴더 리스트 조회
     * @return 폴더 리스트 info
     */
    @Override
    public List<HomeFolderRes> getHomeFolders() {
        List<Folder> all = getFolders();

        return all.stream()
                .map(folder -> {
                    List<CheckList> checkLists = folder.getCheckLists();
                    int completionRate = Optional.ofNullable(checkLists)
                            .filter(c -> !c.isEmpty())
                            .map(c -> (int) (c.stream().filter(CheckList::isChecked).count() * 100.0 / c.size()))
                            .orElse(0);

                    return new HomeFolderRes(folder.getId(), folder.getTitle(), completionRate);
                })
                .toList();
    }

    /**
     * 기록 폴더 리스트 조회
     * @return 폴더 리스트 info
     */
    @Override
    public List<RecordFolderRes> getRecordFolders() {
        List<Folder> all = getFolders();

        return all.stream().map(folder -> {
            long checkListCount = Optional.ofNullable(folder.getCheckLists())
                    .stream()
                    .flatMap(List::stream)  // List를 Stream으로 변환
                    .count();

            Optional<CheckList> latestCheckList = Optional.ofNullable(folder.getCheckLists())
                    .stream()
                    .flatMap(List::stream)
                    .max(Comparator.comparing(CheckList::getModifiedDate));
            LocalDateTime latestUpdateTime = latestCheckList.map(CheckList::getModifiedDate).orElse(null);

            return new RecordFolderRes(
                    folder.getId(),
                    folder.getTitle(),
                    checkListCount,
                    latestUpdateTime
            );
        }).toList();
    }

    /**
     * 폴더 수정
     * @param updateFolderReq 수정할 폴더 info
     */
    @Override
    public void updateFolder(Long folderId, UpdateFolderReq updateFolderReq) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new BaseException(ErrorCode.FOLDER_NOT_FOUND));
        folder.setTitle(updateFolderReq.title());
    }

    /**
     * 폴더 삭제
     * @param deleteFolderReq 삭제할 폴더 info
     */
    @Override
    public void deleteFolder(Long folderId) {
        folderRepository.deleteById(folderId);
    }

    private List<Folder> getFolders() {
        List<Folder> all = folderRepository.findAll();
        return all;
    }
}
