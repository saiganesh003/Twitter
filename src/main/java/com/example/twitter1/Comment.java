package com.example.twitter1;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer commentID;
    String commentBody;
    @Embedded
    UserInfo commentCreator;

    public void setInformation(String name,Integer userId) {
        commentCreator = new UserInfo(userId, name);
    }
//    public void setId(Integer id) {
//        this.commentID = id;
//    }
//
//    public Integer getId() {
//        return commentID;
//    }
static class UserInfo{
    Integer userID;
    String name;

    UserInfo(Integer userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public UserInfo() {

    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
}
}


//@Embeddable
