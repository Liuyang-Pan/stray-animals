package cn.basicPLY.animals.enumerate;

/**
 * purpose:文件模块类型枚举类
 *
 * @author Pan Liuyang
 * 2022/4/23 20:56
 */
public enum FileEnum {
    /**
     * 领养模块文件类型
     */
    STRAY_ANIMALS_ADOPTION("领养", "STRAY_ANIMALS_ADOPTION", "adoption");

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型代码
     */
    private final String typeCode;

    /**
     * 相对路径
     */
    private final String relativePath;


    FileEnum(String typeName, String typeCode, String relativePath) {
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.relativePath = relativePath;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getRelativePath() {
        return relativePath;
    }

    /**
     * 通过类型CODE获取绝对存储路径
     *
     * @param typeCode 类型CODE
     * @return 绝对存储路径
     */
    public static String getPathByTypeCode(String typeCode) {
        FileEnum[] fileEnums = values();
        for (FileEnum fileEnum : fileEnums) {
            if (fileEnum.getTypeCode().equals(typeCode)) {
                return fileEnum.getRelativePath();
            }
        }
        return null;
//        Arrays.stream(FileEnum.values()).filter(fileEnum -> fileEnum.typeCode.equals(typeCode));
    }
}
