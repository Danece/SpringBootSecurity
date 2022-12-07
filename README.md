# <h1> 目的
實作 **Spring Boot Security、AOP、HTTPS、i18N** 功能
  
# <h2> 建置環境
* 構建工具 : Maven
* 程式語言 : Java
* 編譯工具 : Visual Studio Code
* 頁面模板 : JSP
* JAVA 版本 : 19.0.1
* Maven 版本 : 3.8.6
* Thymleaf 版本 : 3.0.15
* jQuery 版本 : 3.5.1
* AOP 版本 : 2.7.5
* 資料庫 : MsSQL

# <h2> 操作
* Pom.xml 設定
* 透過繼承 WebSecurityConfigurerAdapter 來進行設定
* 實做 UserDetailsService 調整驗證功能
* 建立使用者資訊
* 自定義登入驗證
* 頁面實作角色、權限檢核

# <h2> Spring Boot Security 觀念
* 有**角色**、**權限**兩種設定，根據需求做設定，增加管控的靈活度
* 都設定在 [authority ] 變數名稱內
  > Ex : userInfo_admin.put("authority", "admin, normal, ROLE_manager");
* **角色 [Role]**
  > 設定的格式 : ROLE_角色名稱
* **權限 [Authority]**
  > 設定的格式 : 權限名稱
* 搭配JSTL標籤，即可達到頁面管控的效果

# <h2> AOP 觀念
* 用途
  > 針對重複使用的共通方法，設定切入點即可以供指定對象共同使用
* 採用 **切面(Aspect)** 概念，其包含二部分
  * **切入點(@Pointcut)**
  * **共通方法(Advice)**
* 切入時機標籤
  * **@Before** : 在方法執行前執行
  * **@After** : 在方法執行後執行
  * **@AfterReturning** : 在方法返回後才執行，但若方法發生例外，則不執行
  * **@Around** : 實作同時涵蓋方法執行前後
  * **@AfterThrowing** : 在方法拋出例外後執行
