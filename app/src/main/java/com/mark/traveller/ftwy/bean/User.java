package com.mark.traveller.ftwy.bean;

/**
 * Created by Mark on 2016/11/15 0015.
 */

public class User {
    private int id;
    private String phone;
    // private String email;
    private String password;
    private String name;

    /**
     * 创建对象时,可以设置一个随机的MD5加密的字符串的ID
     */
    public User() {

    }

    // -------------------------- set get方法 ---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
//                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
