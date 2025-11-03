package org.example.blog.BLOG.Service;

import org.example.blog.BLOG.DTO.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO);

    PostDTO getPostById(String id);

    List<PostDTO> getAllPosts();

    Page<PostDTO> getPostsByUser(String userId, Pageable pageable);

    PostDTO updatePost(String id, PostDTO postDTO);

    void deletePost(String id);
}

