package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

@Data
public class UI_FileBrowserDto {
    private String name;
    private String type;
    private String path;
    private Long size;
}
