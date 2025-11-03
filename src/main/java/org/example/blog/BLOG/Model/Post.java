package org.example.blog.BLOG.Model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {


    @Id
    private String id;

    @NotBlank(message = "Title must not be empty")
    @Size(max = 150, message = "Title must be at most 150 characters")
    private String title;

    @NotBlank(message = "Content must not be empty")
    private String content;


    @NotBlank(message = "userId must not be empty")
    private String userId;


    private String authorName;


    @CreatedDate
    private Instant createdAt;
}

