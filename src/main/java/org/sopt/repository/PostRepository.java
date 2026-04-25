package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public Post save(Post post);
    public List<Post> findAll();
    public Optional<Post> findById(Long id);
    public boolean deleteById(Long id);
    public Long generateId();
}
