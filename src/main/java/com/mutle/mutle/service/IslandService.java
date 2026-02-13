package com.mutle.mutle.service;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.Music;
import com.mutle.mutle.entity.Platform;
import com.mutle.mutle.entity.RepMusic;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final MusicRepository musicRepository;

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
        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);
        if (isMe || isFriend) {
            calendarDtos=bottleRepository.findByUserAndYearAndMonth(user, start, end).
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

    //bio 수정
    @Transactional
    public void updateBio(Long id, String newBio){
        User user=userRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateBio(newBio);
    }

    //repMusic 수정
    @Transactional
    public void updateRepMusic(Long id, RepMusicUpdateRequestDto requestDto) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        //repMusic(music)이 db에 있는지 확인, 없으면 생성
        Music music=musicRepository
                    .findByTrackNameAndArtistName(requestDto.getTrackName(), requestDto.getArtistName())
                    .orElseGet(()->
                            musicRepository.save(
                                    Music.builder()
                                            .trackName(requestDto.getTrackName())
                                            .artistName(requestDto.getArtistName())
                                            .artworkUrl60(requestDto.getArtworkUrl60())
                                            .build()
                            )
                    );

        //repMusic이 db에 있는지 확인, 없으면 생성
        RepMusic repMusic=repMusicRepository.findByUser(user)
                    .orElseGet(()->
                            RepMusic.builder()
                                    .user(user)
                                    .build());

        //repMusic을 music으로 update
        repMusic.updateMusic(music);
        //저장
        repMusicRepository.save(repMusic);

    }

    //repMusic 삭제
    @Transactional
    public void deleteRepMusic(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        repMusicRepository.deleteByUser(user);
    }

    //platforms 수정
    @Transactional
    public void updatePlatforms(Long id, PlatformsUpdatedRequestDto requestDto) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        platformRepository.deleteAllByUser(user);

        if (requestDto.getPlatforms() != null) {
            List<Platform> platforms = requestDto.getPlatforms().stream()
                    .map(dto -> Platform.builder()
                            .user(user)
                            .platformName(dto.getPlatformName())
                            .platformNickname(dto.getPlatformNickname())
                            .build())
                    .toList();
            platformRepository.saveAll(platforms);
        }
    }
}
