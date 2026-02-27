package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();

        System.out.println("=== 1. 유저 등록 ===");
        User user1 = userService.createUser("스프린트", "sprint@test.com");
        User user2 = userService.createUser("자바초보", "java@test.com");
        System.out.println("유저1 생성 완료: ID = " + user1.getId() + ", 이름 = " + user1.getName());

        System.out.println("\n=== 2. 유저 단건 조회 ===");
        User foundUser = userService.getUser(user1.getId());
        System.out.println("조회된 유저 이름: " + foundUser.getName());

        System.out.println("\n=== 3. 유저 다건 조회 ===");
        List<User> allUsers = userService.getAllUsers();
        System.out.println("총 유저 수: " + allUsers.size() + "명");

        System.out.println("\n=== 4. 데이터 수정 ===");
        System.out.println("수정 전 이름: " + user1.getName());
        userService.updateUser(user1.getId(), "스프린트_수정됨", "update@test.com");

        System.out.println("\n=== 5. 수정된 데이터 조회 ===");
        User updatedUser = userService.getUser(user1.getId());
        System.out.println("수정 후 이름: " + updatedUser.getName());

        System.out.println("\n=== 6. 삭제 ===");
        userService.deleteUser(user2.getId());
        System.out.println("유저2가 삭제되었습니다.");

        System.out.println("\n=== 7. 조회를 통해 삭제되었는지 확인 ===");
        User deletedUser = userService.getUser(user2.getId());
        if (deletedUser == null) {
            System.out.println("유저2를 찾을 수 없습니다. (삭제 성공!)");
        } else {
            System.out.println("앗, 유저2가 아직 남아있습니다.");
        }
    }
}