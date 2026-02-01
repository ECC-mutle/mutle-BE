package com.mutle.mutle.service;

import com.mutle.mutle.dto.MenuResponseDto;
import com.mutle.mutle.entity.FriendshipStatus;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.FriendShipRepository;
import com.mutle.mutle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;

    public MenuResponseDto getMenu(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //유리병 수
        Integer receivedBottleCount = 0; // TODO: 유리병 도메인 완성 후 교체

        //친구 수
        Integer friendRequestCount = friendShipRepository.countByReceiverAndFriendshipStatus(
                user, FriendshipStatus.valueOf("REQUEST_SENT"));

        return MenuResponseDto.builder()
                .receivedBottleCount(receivedBottleCount)
                .friendRequestCount(friendRequestCount)
                .build();
    }
}
