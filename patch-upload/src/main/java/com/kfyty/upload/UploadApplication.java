package com.kfyty.upload;

import com.kfyty.upload.mapper.FileMapper;
import com.kfyty.upload.pojo.FilePojo;
import com.kfyty.upload.utils.UploadUtil;
import com.kfyty.upload.vo.FileExists;
import com.kfyty.upload.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SpringBootApplication
public class UploadApplication {
    @Autowired
    private FileMapper fileMapper;

    @PostMapping("/file/new")
    public FilePojo uploadFile(@RequestBody FilePojo file) {
        fileMapper.insert(file);
        return file;
    }

    @GetMapping("/file/exists")
    public FileExists fileExists(String md5, Long size) {
        FilePojo file = fileMapper.findByMd5(md5);
        if(file == null) {
            return FileExists.nonExistent();
        }
        if(file.getSize().equals(size)) {
            return FileExists.exists(file.getId());
        }
        return FileExists.partExistent(file.getId(), fileMapper.findPatchIndexByParent(file.getId()));
    }

    @PostMapping("/file/patch/upload")
    public Result filePatchExists(String name, Integer index, Integer parent, String md5, Long size, MultipartFile patch) throws IOException {
        FilePojo file = fileMapper.findByParentAndMd5(parent, md5);
        if(file == null || !file.getSize().equals(size)) {
            Optional.ofNullable(file).ifPresent(e -> fileMapper.deleteByPk(e.getId()));
            fileMapper.insert(new FilePojo(index, parent, name, UploadUtil.saveFile(patch, size), md5, size));
            return Result.OK();
        }
        return file.getSize().equals(size) ? Result.OK() : Result.FAIL();
    }

    @Transactional
    @PostMapping("/file/patch/merge")
    public Result filePatchMerge(Integer parent, Long size) throws IOException {
        FilePojo fileInfo = fileMapper.findByPk(parent);
        List<FilePojo> patchs = fileMapper.findByParentOrderByPatchIndexAsc(parent);
        Long total = patchs.stream().mapToLong(FilePojo::getSize).sum();
        if(fileInfo == null || CollectionUtils.isEmpty(patchs) || !total.equals(size)) {
            fileMapper.deleteByParent(parent);
            log.warn("total: {}, require size: {}, and delete file to retry !", total, size);
            return Result.FAIL();
        }
        String fileType = UploadUtil.parseFileType(fileInfo.getName());
        String path = UploadUtil.mergeFile(fileType, patchs.stream().map(FilePojo::getPath).collect(Collectors.toList()));
        fileMapper.updateByIdSetPathAndSize(parent, path, total);
        fileMapper.deleteByParent(parent);
        return Result.OK();
    }

    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
