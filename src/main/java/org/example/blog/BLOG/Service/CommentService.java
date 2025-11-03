package org.example.blog.BLOG.Service;

import org.example.blog.BLOG.DTO.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO);

    CommentDTO getCommentById(String id);

    List<CommentDTO> getCommentsByPost(String postId);

    List<CommentDTO> getCommentsByUser(String userId);

    CommentDTO updateComment(String id, CommentDTO commentDTO);

    void deleteComment(String id);
}

