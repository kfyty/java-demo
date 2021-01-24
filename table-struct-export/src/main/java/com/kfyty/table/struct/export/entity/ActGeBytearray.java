package com.kfyty.table.struct.export.entity;

import lombok.Data;

@Data
public class ActGeBytearray {
    private String id;
    private Long rev;
    private String name;
    private String deploymentId;
    private byte[] bytes;
    private Long generated;
}
