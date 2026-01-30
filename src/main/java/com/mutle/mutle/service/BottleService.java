package com.mutle.mutle.service;

import com.mutle.mutle.dto.BottleCreateRequest;
import com.mutle.mutle.dto.BottleCreateResponse;
import com.mutle.mutle.dto.BottleRandomResponse;
import com.mutle.mutle.entity.Bottle;
import com.mutle.mutle.entity.Music;
import com.mutle.mutle.entity.TodayQuest;
import com.mutle.mutle.entity.User;
import com.mutle.mutle.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Slf4j
@Service
public class BottleService {

    @Autowired
    private BottleRepository bottleRepository;
    @Autowired
    private TodayQuestRepository todayQuestRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MusicRepository musicRepository;

    // 유리병 보내기
    @Transactional
    public BottleCreateResponse createBottle(Long id, Long questionId, Long musicId, BottleCreateRequest request) {

        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 질문 조회 및 예외 발생
        TodayQuest quest = todayQuestRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new IllegalArgumentException("오늘의 질문을 찾을 수 업습니다."));

        // 음악 조회 및 예외 발생
        Music music = musicRepository.findByMusicId(musicId)
                .orElseThrow(() -> new IllegalArgumentException("오늘의 질문을 찾을 수 업습니다."));

        // 유리병 엔티티 생성
        Bottle bottle = Bottle.builder()
                .user(user)
                .todayQuest(quest)
                .music(music)
                .memo(request.getMemo())
                .isShared(request.getIsShared())
                .trackName(request.getMusicInfo().getTrackName())
                .artistName(request.getMusicInfo().getArtistName())
                .artworkUrl60(request.getMusicInfo().getArtworkUrl60())
                .build();
        // 유리병 엔티티를 DB에 저장
        Bottle savedBottle = bottleRepository.save(bottle);

        // DTO로 변환해 반환
        return BottleCreateResponse.fromEntity(savedBottle);
    }

    //유리병 받기
    public BottleRandomResponse getBottle(Long id) {
        // 랜덤 유리병 조회
        Bottle randomBottle = bottleRepository.findRandomBottle(id)
                .orElseThrow(() -> new IllegalArgumentException("도착한 유리병이 없습니다."));

        // 반환
        return BottleRandomResponse.fromEntity(randomBottle, id);
    }

    // 유리병 반응 남기기
    @Transactional
    public void addReaction() {}

    //유리병 반응 조회
    public void getReactions() {}

    // 오늘의 질문 조회
    public void getTodayQuest() {}

    // 유리병 상세페이지 조회
    public void getBottleDetail() {}

    // 유리병 북마크 추가
    @Transactional
    public void addBookmark() {}

    // 북마크 목록 조회
    public void getBookmarks() {}
}
