package com.example.twitter1;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    @Autowired
    UserRepository userRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    CommentRepository commentRepo;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String,Object> payloaddedd) {    //email , name , password
        String email=payloaddedd.get("email").toString();
        Optional<User> prestOrNot=userRepo.findByEmail(email);
        if(prestOrNot.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResp("Forbidden, Account already exists"));
        }
        User newUser=new User(payloaddedd.get("name").toString(),email,payloaddedd.get("password").toString());
//        newUser.setEmail(email);
//        newUser.setPassword();
//        newUser.setName();
        userRepo.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account Creation Successful");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam("userID") Integer userIDD) {
        Optional<User> presentOrNot=userRepo.findById(userIDD);
        if(presentOrNot.isPresent()) {
            forAllUsers userr=new forAllUsers();
            userr.setEmail(presentOrNot.get().getEmail());
            userr.setName(presentOrNot.get().getName());
            userr.setUserID(presentOrNot.get().getUserID());
            return ResponseEntity.ok(userr);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("User does not exist"));      //  *** NEED TO CHANGE TO A RETURN TYPE OF STRING
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,Object> payloadedd) {
        String email=payloadedd.get("email").toString();
        String password=payloadedd.get("password").toString();
        Optional<User> checking=userRepo.findByEmail(email);
        if(checking.isPresent()) {
            if(password.equals(checking.get().getPassword())) {
                return ResponseEntity.ok("Login Successful");
            }
            else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResp("Username/Password Incorrect"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("User does not exist"));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getPosts() {
        Iterable<Post> allPosts=postRepo.findAll();
        List<Post> postss=new ArrayList<>();
        allPosts.forEach(postss::add);
//        return ResponseEntity.ok(posts);
        Collections.sort(postss,new Comparator<Post>() {
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return ResponseEntity.ok(postss);
    }

    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestBody Map<String,Object> payloadded) {
        Integer userID=Integer.parseInt(payloadded.get("userID").toString());
        String postBody=payloadded.get("postBody").toString();
        Optional<User> user=userRepo.findById(userID);
        Post newPostPosting=new Post();
        newPostPosting.setComments(new ArrayList<>());
        newPostPosting.setPostBody(postBody);
        newPostPosting.setDate(new Date());
        if(user.isPresent()) {
            postRepo.save(newPostPosting);
            List<Post> posts=user.get().getPosts();
            posts.add(newPostPosting);
            user.get().setPosts(posts);

            userRepo.save(user.get());

            return ResponseEntity.ok("Post created successfully");
        }
        else{
//            return "User does not exist"; //"User does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("User does not exist"));
        }
    }

    @GetMapping("/post")
    public ResponseEntity<?> gettingAPost(@RequestParam("postID") Integer postID) {
        Optional<Post> post=postRepo.findById(postID);
        if(post.isPresent()) {
            return ResponseEntity.ok(post.get());
        }
        else{       //"Post does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Post does not exist"));
        }
    }

    @PatchMapping("/post")
    public ResponseEntity<?> patchingAPost(@RequestBody Map<String,Object> payloadingg) {
        Integer postID=Integer.parseInt(payloadingg.get("postID").toString());
        Optional<Post> post=postRepo.findById(postID);
        if(post.isPresent()) {
            post.get().setPostBody(payloadingg.get("postBody").toString());

            postRepo.save(post.get());

            return ResponseEntity.ok("Post edited successfully");
        }
        else{   //"Post does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Post does not exist"));
        }
    }


    @DeleteMapping("/post")
    public ResponseEntity<?> deletingAPost(@RequestParam("postID") Integer postIDDing) {
        Optional<Post> postt=postRepo.findById(postIDDing);
        if(postt.isPresent()) {
            commentRepo.deleteAll(postt.get().getComments());
            for(User u:userRepo.findAll()) {
                u.getPosts().removeIf(p -> p.getPostID().equals(postIDDing));
            }
            postRepo.delete(postt.get());
            return ResponseEntity.ok("Post deleted");
        }
        else{       //"Post does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Post does not exist"));
        }
    }
//    @DeleteMapping("/post")
//    public ResponseEntity<?> deletingAPost(@RequestBody forDeleting payload) {
////        Integer postID = Integer.parseInt(payload.get("postID").toString());
//        Integer postID= payload.getId();
//
//        try{
////            int postID=payload.getId();
//            if(!postRepo.existsById(postID)) {
//                throw new PostNotFoundException("post does not exist");
//            }
//            postRepo.deleteById(postID);
//            return ResponseEntity.ok("Post deleted successfully");
//        }catch (PostNotFoundException e) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//            return new ResponseEntity<>(postID, HttpStatus.NOT_FOUND);
////            return new ResponseEntity<>(postID, HttpStatus.NOT_FOUND);
//        }
////        Optional<Post> post1 = postRepo.findById(postID);
//////        for(Post post)
////        if (post1.isPresent()) {
////            postRepo.delete(post1.get());
////            return ResponseEntity.ok("Post deleted successfully");
////        } else {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
////        }
////        Integer id=Integer.parseInt(payload.get("postID").toString());
////        Optional<Post> post1=postRepo.findById(postID);
////        if(post1.isPresent()) {
////            postRepo.delete(post1.get());
////            return ResponseEntity.ok("Post deleted successfully");
////        }
////        else{
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
////        }
////        try {
////            postRepo.deleteById(id);
////            return new ResponseEntity<>(HttpStatus.OK);
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post does not exist");
////        }
////        Optional<Post> postOptional = postRepo.findById(id);
////        if (postOptional.isPresent()) {
////            postRepo.deleteById(id);
////            return ResponseEntity.ok("Post deleted successfully");
////        } else {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
////        }
//    }


    @PostMapping("/comment")
    public ResponseEntity<?> addingComment(@RequestBody Map<String,Object> payloadedPayLoadd) {
        String commentBody=(String) payloadedPayLoadd.get("commentBody");
        Integer postId=Integer.valueOf(payloadedPayLoadd.get("postID").toString());
        Integer userId=Integer.valueOf(payloadedPayLoadd.get("userID").toString());

        Optional<Post> postt=postRepo.findById(postId);
        Optional<User> userr=userRepo.findById(userId);

        boolean flag=false;
        if(userr.isPresent()&&postt.isPresent()) {
//            for(Post p:user.get().getPosts()) {
//                if(p.getPostID()==postId){
//                    post=postRepo.findById(postId);
//                    flag=true;
//                    break;
//                }
//            }
//            if(flag){
                Comment commentingg=new Comment();
                commentingg.setCommentBody(commentBody);
                commentingg.setInformation(userr.get().name,userId);
                commentRepo.save(commentingg);
                List<Comment> comments=postt.get().getComments();
                comments.add(commentingg);

                postt.get().setComments(comments);
                postRepo.save(postt.get());
//            }
//            else{
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Post does not exist");
//            }

            return ResponseEntity.ok("Comment created successfully");
        }
        else{
            if(!userr.isPresent()) {     //"User does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("User does not exist"));
            }
            else{       //"Post does not exist"
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResp("Post does not exist"));
            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment does not exist");
        }

    }

    @GetMapping("/comment")
    public ResponseEntity<?> getAComment(@RequestParam("commentID") Integer commentID) {
       Optional<Comment> commenting=commentRepo.findById(commentID);
       if(commenting.isPresent()) {
           return ResponseEntity.ok(commenting.get());
       }
       else{        //"Comment does not exist"
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Comment does not exist"));
       }
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> updateAComment(@RequestBody Map<String,Object> payload) {
        String bodyOFTHEComment=(String) payload.get("commentBody");
        Integer commentId=Integer.valueOf(payload.get("commentID").toString());
        Optional<Comment> commenting=commentRepo.findById(commentId);
        if(commenting.isPresent()) {
            commenting.get().setCommentBody(bodyOFTHEComment);
            commentRepo.save(commenting.get());

            return ResponseEntity.ok("Comment edited successfully");
        }
        else{           //"Comment does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Comment does not exist"));
        }
    }


    @DeleteMapping("/comment")
    public ResponseEntity<?> deletingAComment(@RequestParam("commentID") Integer commentId) {
//        Integer commentId=Integer.valueOf(payload.get("commentID").toString());
        Optional<Comment> commenting=commentRepo.findById(commentId);
//        if(comment.isPresent()) {
//            commentRepo.delete(comment.get());
//            return ResponseEntity.ok("Comment deleted successfully");
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment does not exist");
//        }
        if(commenting.isPresent()) {
//            comment.get().commentCreator=null;
            List<Post> postss=postRepo.findAll();
            for(Post postingg:postss) {
//                return ResponseEntity.ok("Comment deleted successfully "+post.getComments());
                if(postingg.getComments().contains(commenting.get())) {
                    postingg.getComments().remove(commenting.get());
                }
//                for(Comment c:post.getComments()) {
//                    if(c.getCommentID()==commentId) {
//                        post.getComments().remove(c);
//                    }
//                }
                postRepo.save(postingg);
            }
            commentRepo.delete(commenting.get());
            return ResponseEntity.ok("Comment deleted successfully ");

        }
        else{       //"Comment does not exist"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResp("Comment does not exist"));
        }


//        return null;
    }

    @GetMapping("/users")
    public @ResponseBody List<forAllUsers> getUsers() {
        Iterable<User> allUsers=userRepo.findAll();
        List<forAllUsers> users=new ArrayList<>();
        for(User u:allUsers) {
            forAllUsers user=new forAllUsers(u.getName(),u.getUserID(),u.getEmail());
            users.add(user);
        }
        return users;
    }



//    @Entity
//    public static class forDeleting{
//        @Id
//        Integer postID;
//
//
//        public void setId(Integer postID) {
//            this.postID = postID;
//        }
//
//        public Integer getId() {
//            return postID;
//        }
//    }




//    @Entity
//    public static class forPosting{
//        String postBody;
//        Integer userID;
//        @Id
//        @GeneratedValue(strategy = GenerationType.AUTO)
//        private Long id;
//
//        public void setPostBody(String postBody) {
//            this.postBody = postBody;
//        }
//
//        public void setUserID(Integer userID) {
//            this.userID = userID;
//        }
//
//        public String getPostBody() {
//            return postBody;
//        }
//
//        public Integer getUserID() {
//            return userID;
//        }
//
//        public void setId(Long id) {
//            this.id = id;
//        }
//
//        public Long getId() {
//            return id;
//        }
//    }

//    @Entity
//    class forUpdatingPost{
//
//    }



}

class PostNotFoundException extends RuntimeException {

//    public String message;
    public PostNotFoundException(String message) {
//        this.message = message;
        super(message);
    }
//    public String getMessage() {
//        return PostNotFoundException.toString();
//    }
}


@Embeddable
class forAllUsers{
    String name;
    Integer UserID;
    String email;

    public forAllUsers(String name, Integer userID, String email) {
        this.name = name;
        UserID = userID;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Integer getUserID() {
        return UserID;
    }

    public String getEmail() {
        return email;
    }

    public forAllUsers() {

    }
}
