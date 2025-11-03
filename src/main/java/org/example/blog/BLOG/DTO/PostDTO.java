package org.example.blog.BLOG.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private String id;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 150, message = "Title must be at most 150 characters")
    private String title;

    @NotBlank(message = "Content must not be empty")
    private String content;

    @NotBlank(message = "User ID must not be empty")
    private String userId;

    private String authorName;

    private Instant createdAt;
}
