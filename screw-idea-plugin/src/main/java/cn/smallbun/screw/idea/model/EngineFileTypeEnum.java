package main.java.cn.smallbun.screw.idea.model;

import cn.smallbun.screw.core.engine.EngineFileType;

public enum EngineFileTypeEnum {

    WORD("word",EngineFileType.WORD),
    HTML("html",EngineFileType.HTML),
    MD("md",EngineFileType.MD)
    ;

    private String name;

    private EngineFileType engineFileType;

    EngineFileTypeEnum(String name, EngineFileType engineFileType) {
        this.name = name;
        this.engineFileType = engineFileType;
    }

    public static EngineFileTypeEnum getByName(String name) {
        EngineFileTypeEnum[] values = EngineFileTypeEnum.values();
        for (EngineFileTypeEnum engineFileTypeEnum : values) {
            if (engineFileTypeEnum.getName().equalsIgnoreCase(name)) {
                return engineFileTypeEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EngineFileType getEngineFileType() {
        return engineFileType;
    }

    public void setEngineFileType(EngineFileType engineFileType) {
        this.engineFileType = engineFileType;
    }
}
