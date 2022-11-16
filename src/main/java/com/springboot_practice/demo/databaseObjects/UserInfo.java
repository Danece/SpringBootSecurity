package com.springboot_practice.demo.databaseObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @Entity : 告訴 Spring 這是數據模型層的宣告
 * @Data : 為類提供讀寫屬性，此外還提供了equals()、hashCode()、toString() 方法
 * @NoArgsConstructor : 自動生成無參數構造函數
 */
@Entity
@Data
@NoArgsConstructor
public class UserInfo {

    /*
     * @Id : 指定為 Primary Key
     * @GeneratedValue : 指定 ID 的生成方式，(strategy = GenerationType.IDENTITY) Mysql, MSSQL 都是常用這種方式
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String authority;

    // 建立新使用者
    public UserInfo (String name, String password, String authority) {
        this.name = name;
        this.password = password;
        this.authority = authority;
    }

    // 更新使用者資訊
    public UserInfo (Long id, String name, String password, String authority) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
