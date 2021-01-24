package com.kfyty.upload.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileExists {
    /**
     * 文件 id
     */
    private Integer id;

    /**
     * 文件状态
     *      -1: 不存在
     *       1: 已存在
     *       0: 部分存在
     */
    private Integer status;

    /**
     * 已上传的文件分片索引
     */
    private List<Integer> patchIndex;

    public static FileExists nonExistent() {
        return new FileExists(null, -1, null);
    }

    public static FileExists exists(Integer id) {
        return new FileExists(id, 1, null);
    }

    public static FileExists partExistent(Integer id, List<Integer> patchIndex) {
        return new FileExists(id, 0, patchIndex);
    }
}
