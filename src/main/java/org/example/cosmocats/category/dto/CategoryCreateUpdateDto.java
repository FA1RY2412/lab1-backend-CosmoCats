package org.example.cosmocats.category.dto;

public class CategoryCreateUpdateDto {
    private String code;
    private String title;

    public CategoryCreateUpdateDto() {
    }

    public CategoryCreateUpdateDto(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
