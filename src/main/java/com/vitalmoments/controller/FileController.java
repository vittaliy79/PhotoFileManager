package com.vitalmoments.controller;

import com.vitalmoments.service.FileComparisonResult;
import com.vitalmoments.service.FileComparisonService;
import com.vitalmoments.service.FileManagementService;
import com.vitalmoments.service.FileOperationResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FileController {

    private final FileComparisonService fileComparisonService;
    private final FileManagementService fileManagementService;

    public FileController(final FileComparisonService fileComparisonService,
                          final FileManagementService fileManagementService
    ) {
        this.fileComparisonService = fileComparisonService;
        this.fileManagementService = fileManagementService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/compare")
    public String compareFiles(String mainFolderPath, String subFolderName, Model model) {
        // Assemble the full path for the subfolder using Apache Commons IO
        String subFolderPath = FilenameUtils.concat(mainFolderPath, subFolderName);

        List<FileComparisonResult> comparisonResults = fileComparisonService.compareFiles(mainFolderPath, subFolderPath);
        model.addAttribute("comparisonResults", comparisonResults);
        model.addAttribute("mainFolderPath", mainFolderPath);
        model.addAttribute("subFolderPath", subFolderPath);
        return "result";
    }

    @PostMapping("/deleteNonMatchingFiles")
    @ResponseBody
    public ResponseEntity<FileOperationResponse> deleteSelectedFiles(@RequestBody Map<String, Object> payload) {
        String mainFolderPath = (String) payload.get("mainFolderPath");
        List<String> filesToDelete = (List<String>) payload.get("filesToDelete");

        FileOperationResponse response = FileManagementService.deleteSelectedFiles(mainFolderPath, filesToDelete);
/*        Map<String, Object> response = new HashMap<>();
        response.put("deletedCount", deletedCount);*/
        return ResponseEntity.ok(response);
    }

    @PostMapping("/moveFiles")
    @ResponseBody
    public ResponseEntity<FileOperationResponse> moveFiles(@RequestBody Map<String, Object> payload) {
        final String mainFolderPath = (String) payload.get("mainFolderPath");
        final String destinationPath = (String) payload.get("destinationPath");
        List<String> filesToMove = (List<String>) payload.get("filesToMove");

        FileOperationResponse response =
                fileManagementService.moveSelectedFiles(mainFolderPath, filesToMove, destinationPath);
        return ResponseEntity.ok(response);
    }
}
