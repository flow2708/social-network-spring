package ru.flow.socialnetwork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostRequest {

    @NotBlank(message = "Текст поста не может быть пустым")
    @Size(max = 5000, message = "Пост не может быть длиннее 5000 символов")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}