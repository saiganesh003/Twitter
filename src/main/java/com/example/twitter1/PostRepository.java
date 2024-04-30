package com.example.twitter1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
//    boolean deleteById(int id);
    default void newDelete(Integer postID){
        for (Post post : findAll()) {
            if (post.getPostID().equals(postID)) {
                delete(post);
            }
        }
    }
}
