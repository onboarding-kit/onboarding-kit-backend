package com.example.checklistservice.entity.service;

import com.example.checklistservice.entity.CheckList;
import com.example.checklistservice.entity.Folder;
import com.example.checklistservice.entity.dto.*;
import com.example.checklistservice.entity.repository.CheckListRepository;
import com.example.checklistservice.entity.repository.FolderRepository;
import com.example.common.global.exception.BaseException;
import com.example.common.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CheckListServiceImpl implements CheckListService{
    private final FolderRepository folderRepository;
    private final CheckListRepository checkListRepository;

    /**
     * 폴더 & 체크리스트 생성
     * @param createCheckListReq 폴더 info & 체크리스트 info
     * @return ?
     */
    @Override
    public CreateCheckListRes createFolderWithCheckList(CreateCheckListReq createCheckListReq) {
        log.info("Start creating folder with checklist");

        Folder folder = createCheckListReq.folderInfoReq().toEntity();
        Folder savedFolder = folderRepository.save(folder);
        log.info("Folder created successfully: [ID: {}, Name: {}]", savedFolder.getId(), savedFolder.getTitle());

        List<CheckList> checkLists = createCheckListReq.checkListItemReqList().stream().map(checkList -> checkList.toEntity()).toList();
        List<CheckList> savedCheckLists = checkListRepository.saveAll(checkLists);
        log.info("Saved {} checklists for folder [ID: {}]", savedCheckLists.size(), savedFolder.getId());

        //연관관계 매핑
        savedFolder.setCheckLists(savedCheckLists);
        savedCheckLists.forEach(checkList -> checkList.setFolder(savedFolder));
        log.info("Successfully created folder and associated checklists");

        return new CreateCheckListRes(savedFolder.getId(), savedCheckLists.stream().map(CheckList::getId).toList());
    }

    /**
     * 체크리스트 삭제
     * @param checklistId 삭제할 체크리스트 id
     */
    @Override
    public void deleteCheckList(Long checklistId) {
        checkListRepository.deleteById(checklistId);
        log.info("Checklists deleted successfully: ID {}", checklistId);
    }

    /**
     * 체크리스트 업데이트
     * @param checklistId 체크리스트 id
     * @param updateCheckListReq 체크리스트 업데이트 info
     */
    @Override
    public void updateCheckLists(Long checklistId, UpdateCheckListReq updateCheckListReq) {
        log.info("Start updating checklist");


        CheckList checkList = checkListRepository.findById(checklistId).orElseThrow(() -> {
            log.error("Checklist not found: ID {}", checklistId);
            return new BaseException(ErrorCode.CHECKLIST_NOT_FOUND);
        });

        Optional.ofNullable(updateCheckListReq.content()).ifPresent(checkList::setContent);
        Optional.ofNullable(updateCheckListReq.isChecked()).ifPresent(checkList::setChecked);

        log.info("Checklist update process completed");
    }

    /**
     * 메인 체크리스트 Top3
     * @param folderId 폴더 id
     * @return 체크리스트 Top3
     */
    @Override
    public List<TopCheckListsRes> getTopCheckLists(Long folderId) {
        log.info("Fetching Top 3 checklists for folder [ID: {}]", folderId);

        Folder folder = findFolderById(folderId);
        List<CheckList> checkLists = folder.getCheckLists().stream()
                .filter(checkList -> checkList.isChecked() == Boolean.FALSE)
                .sorted(Comparator.comparing(CheckList::getCreateDate))
                .limit(3)
                .toList();
        log.info("Retrieved Top 3 checklists for folder [ID: {}]", folderId);

        return checkLists.stream()
                .map(checkList -> new TopCheckListsRes(checkList.getId(), checkList.getContent(), checkList.isChecked()))
                .toList();
    }

    /**
     * 체크리스트 조회
     * @param folderId 폴더 id
     * @return 체크리스트 info
     */
    @Override
    public List<CheckListsRes> getFolderCheckLists(Long folderId) {
        log.info("Fetching all checklists for folder [ID: {}]", folderId);
        Folder folder = findFolderById(folderId);
        List<CheckListsRes> checkLists = folder.getCheckLists().stream()
                .map(checkList -> new CheckListsRes(checkList.getId(), checkList.getContent(), checkList.isChecked()))
                .toList();
        log.info("Retrieved {} checklists for folder [ID: {}]", checkLists.size(), folderId);
        return checkLists;
    }

    private Folder findFolderById(Long folderId) {
        return folderRepository.findById(folderId).orElseThrow(() -> {
            log.error("Folder not found: ID {}", folderId);
            return new BaseException(ErrorCode.FOLDER_NOT_FOUND);
        });
    }
}
