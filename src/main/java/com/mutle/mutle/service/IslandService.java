package com.mutle.mutle.service;

import com.mutle.mutle.dto.CalendarDto;
import com.mutle.mutle.dto.IslandResponseDto;
import com.mutle.mutle.dto.PlatformDto;
import com.mutle.mutle.dto.RepMusicDto;
import com.mutle.mutle.entity.RepMusic;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IslandService {

    private final UserRepository userRepository;
    private final RepMusicRepository repMusicRepository;
    private final PlatformRepository platformRepository;
    private final BottleRepository bottleRepository;
    private final FriendShipRepository friendShipRepository;

    //프로필 조회
    @Transactional(readOnly = true)
    public IslandResponseDto getIsland(Long id, Long currentuserId, Integer year, Integer month) {

        User user=userRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //권한 확인
        boolean isMe = currentuserId.equals(user.getId());
        boolean isFriend= !isMe && friendShipRepository.existsAcceptedFriendship(currentuserId, id);

        //repmusic
        RepMusic repMusic=repMusicRepository.findByUser(user).orElse(null);
        RepMusicDto repMusicDto=(repMusic!=null) ? new RepMusicDto(
                repMusic.getMusic().getTrackName(),
                repMusic.getMusic().getArtistName(),
                repMusic.getMusic().getArtworkUrl60()
        ) : null;

        //platform
        List<PlatformDto> platformDtos=platformRepository.findAllByUser(user).stream().
                map(p->new PlatformDto(p.getPlatformName(), p.getPlatformNickname()))
                .toList();

        //calender
        List<CalendarDto> calendarDtos;
        if (isMe || isFriend) {
            calendarDtos=bottleRepository.findByUserAndYearAndMonth(user, year, month).
                    stream()
                    .map(b->new CalendarDto(
                            b.getBottleId(), b.getMusic().getArtworkUrl60(), formatToDate(b.getBottleCreatedAt()) ))
                    .toList();
        } else{
            calendarDtos= Collections.emptyList();
        }
        return IslandResponseDto.builder()
                .isMe(isMe)
                .isFriend(isFriend)
                .year(year)
                .month(month)
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .repMusic(repMusicDto)
                .platforms(platformDtos)
                .calendars(calendarDtos)
                .build();

    }

    //날짜 가공
    private String formatToDate(Timestamp createdAt) {
        return createdAt.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
