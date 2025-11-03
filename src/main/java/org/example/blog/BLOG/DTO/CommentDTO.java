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
public class CommentDTO {

    private String id;

    @NotBlank(message = "Post ID must not be empty")
    private String postId;

    @NotBlank(message = "User ID must not be empty")
    private String userId;

    private String authorName;

    @NotBlank(message = "Content must not be empty")
    @Size(max = 2000, message = "Comment must be at most 2000 characters")
    private String content;

    private Instant createdAt;
}

