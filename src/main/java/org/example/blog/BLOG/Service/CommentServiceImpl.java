package org.example.blog.BLOG.Service;

import lombok.RequiredArgsConstructor;
import org.example.blog.BLOG.DTO.CommentDTO;
import org.example.blog.BLOG.Model.Comment;
import org.example.blog.BLOG.Repository.CommentRepository;
import org.example.blog.USER.Repository.UserRepository;
import org.example.blog.Exceptions.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);
        comment.setCreatedAt(Instant.now());
        Comment saved = commentRepository.save(comment);
        return mapToDTO(saved);
    }

    @Override
    public CommentDTO getCommentById(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return mapToDTO(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPost(String postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUser(String userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO updateComment(String id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        // Ownership check
        if (!comment.getUserId().equals(getLoggedInUserId())) {
            throw new RuntimeException("You are not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        Comment updated = commentRepository.save(comment);
        return mapToDTO(updated);
    }

    @Override
    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        // Ownership check
        if (!comment.getUserId().equals(getLoggedInUserId())) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentDTO mapToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private Comment mapToEntity(CommentDTO dto) {
        return Comment.builder()
                .id(dto.getId())
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .authorName(dto.getAuthorName())
                .content(dto.getContent())
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
