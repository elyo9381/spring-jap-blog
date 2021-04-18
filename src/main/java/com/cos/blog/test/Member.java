package com.cos.blog.test;

import lombok.Data;

@Data
public class Member {

    private long id;
    private final String userName;
    private  String passWord;
}