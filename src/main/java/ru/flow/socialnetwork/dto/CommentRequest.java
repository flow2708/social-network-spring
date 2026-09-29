package ru.flow.socialnetwork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(max = 1000, message = "Комментарий не может быть длиннее 1000 символов")
    private String content;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}