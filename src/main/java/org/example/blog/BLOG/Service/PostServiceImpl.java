package org.example.blog.BLOG.Service;

import lombok.RequiredArgsConstructor;
import org.example.blog.BLOG.DTO.PostDTO;
import org.example.blog.BLOG.Model.Post;
import org.example.blog.BLOG.Repository.PostRepository;
import org.example.blog.BLOG.Repository.CommentRepository;
import org.example.blog.USER.Repository.UserRepository;
import org.example.blog.Exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = mapToEntity(postDTO);
        post.setCreatedAt(Instant.now());
        Post saved = postRepository.save(post);
        return mapToDTO(saved);
    }

    @Override
    public PostDTO getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostDTO> getPostsByUser(String userId, Pageable pageable) {
        List<PostDTO> posts = postRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), posts.size());
        int end = Math.min(start + pageable.getPageSize(), posts.size());
        return new org.springframework.data.domain.PageImpl<>(posts.subList(start, end), pageable, posts.size());
    }

    @Override
    public PostDTO updatePost(String id, PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // Ownership check
        if (!post.getUserId().equals(getLoggedInUserId())) {
            throw new RuntimeException("You are not authorized to update this post");
        }

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        Post updated = postRepository.save(post);
        return mapToDTO(updated);
    }

    @Override
    public void deletePost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        // Ownership check
        if (!post.getUserId().equals(getLoggedInUserId())) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        // Cascade delete comments
        commentRepository.findByPostId(post.getId())
                .forEach(commentRepository::delete);

        postRepository.delete(post);
    }

    private PostDTO mapToDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .authorName(post.getAuthorName())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Post mapToEntity(PostDTO dto) {
        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .userId(dto.getUserId())
                .authorName(dto.getAuthorName())
                .build();
    }

    private String getLoggedInUserId() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(principal.getUsername()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId()
                .toString();
    }
}
