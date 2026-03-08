package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

public class JavaApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService =
                new BasicMessageService(messageRepository, userRepository, channelRepository);

        User user1 = userService.create("Evan", "Evan@gmail.com", "1234");
        User user2 = userService.create("Rick", "Rick@gmail.com", "9876");

        Channel channel1 = channelService.create("Test", "All chat");

        Message message1 = messageService.create(user1.getId(), channel1.getId(), "Hello");
        Message message2 = messageService.create(user2.getId(), channel1.getId(), "Hi");

        System.out.println("===== Users =====");
        for (User item : userService.findAll()) {
            System.out.println(item.getUsername());
        }

        System.out.println("===== Channels =====");
        for (Channel item : channelService.findAll()) {
            System.out.println(item.getName());
        }

        System.out.println("===== Messages =====");
        for (Message item : messageService.findAll()) {
            System.out.println(item.getContent());
        }

        System.out.println("===== 사용자 선택 조회 =====");
        System.out.println(userService.findById(user1.getId()).getUsername());

        System.out.println("\n===== 사용자 전체 조회 =====");
        for (User user : userService.findAll()) {
            System.out.println(user.getUsername() + " : " + user.getEmail());
        }

        System.out.println("\n===== 채널 수정 =====");
        channelService.update(channel1.getId(), "Test2", "All chat2");
        System.out.println(channelService.findById(channel1.getId()).getName());

        System.out.println("\n===== 메세지 수정 =====");
        messageService.update(message1.getId(), "How are you?");
        System.out.println(messageService.findById(message1.getId()).getContent());

        System.out.println("\n===== 메세지 삭제 =====");
        messageService.delete(message2.getId());
        if (messageService.findById(message2.getId()) == null) {
            System.out.println("메세지가 삭제되었습니다.");
        } else {
            System.out.println("메세지 삭제 실패");
        }
    }
}