package org.example.cosmocats.category.dto;

public class CategoryDto {
    private Long id;
    private String code;
    private String title;

    public CategoryDto() {
    }

    public CategoryDto(Long id, String code, String title) {
        this.id = id;
        this.code = code;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
