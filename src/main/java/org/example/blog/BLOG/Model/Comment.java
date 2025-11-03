package org.example.blog.BLOG.Model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Document(collection = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;


    @NotBlank(message = "postId must not be empty")
    private String postId;


    @NotBlank(message = "userId must not be empty")
    private String userId;


    private String authorName;

    @NotBlank(message = "Comment content must not be empty")
    @Size(max = 2000, message = "Comment must be at most 2000 characters")
    private String content;

    @CreatedDate
    private Instant createdAt;
}

