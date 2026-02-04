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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final FriendShipRepository friendShipRepository;

    @Transactional(readOnly = true)
    public MenuResponseDto getMenu(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_103));

        //친구 요청 수
        Integer friendRequestCount = friendShipRepository.countByReceiverAndFriendshipStatus(
                user, FriendshipStatus.REQUEST_SENT);

        return MenuResponseDto.builder()
                .friendRequestCount(friendRequestCount)
                .build();
    }
}
