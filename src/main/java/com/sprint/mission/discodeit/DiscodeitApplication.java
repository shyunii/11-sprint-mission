package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		UserDto user1 = userService.create(
				new UserCreateRequest("Evan", "Evan@gmail.com", "1234", null)
		);
		UserDto user2 = userService.create(
				new UserCreateRequest("Rick", "Rick@gmail.com", "9876", null)
		);

		Channel channel1 = channelService.create("Test", "All chat");

		Message message1 = messageService.create(user1.id(), channel1.getId(), "Hello");
		Message message2 = messageService.create(user2.id(), channel1.getId(), "Hi");

		System.out.println("===== Users =====");
		for (UserDto item : userService.findAll()) {
			System.out.println(item.username());
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
		userService.find(user1.id())
				.ifPresent(u -> System.out.println(u.username()));

		System.out.println("\n===== 사용자 전체 조회 =====");
		for (UserDto user : userService.findAll()) {
			System.out.println(user.username() + " : " + user.email());
		}

		System.out.println("\n===== 채널 수정 =====");
		channelService.update(channel1.getId(), "Test2", "All chat2");
		channelService.findById(channel1.getId())
				.ifPresent(c -> System.out.println(c.getName()));

		System.out.println("\n===== 메세지 수정 =====");
		messageService.update(message1.getId(), "How are you?");
		messageService.findById(message1.getId())
				.ifPresent(m -> System.out.println(m.getContent()));

		System.out.println("\n===== 메세지 삭제 =====");
		messageService.delete(message2.getId());
		if (messageService.findById(message2.getId()).isEmpty()) {
			System.out.println("메세지가 삭제되었습니다.");
		} else {
			System.out.println("메세지 삭제 실패");
		}
	}
}