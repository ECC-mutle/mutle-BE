package com.mutle.mutle.service;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.*;
import com.mutle.mutle.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
                .orElseThrow(() -> new IllegalArgumentException("오늘의 질문을 찾을 수 없습니다."));

        // 음악 조회 및 예외 발생
        Music music = musicRepository.findByMusicId(musicId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 음악 정보입니다."));

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
    public BottleReactionCreateResponse addReaction(Long id, Long bottleId) {
        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유리병입니다."));

        // 중복 확인
        if (reactionRepository.existsByUserAndBottle(user, bottle)) {
            throw new IllegalArgumentException("이미 반응을 남긴 유리병입니다.");
        }

        // 반응 엔티티 생성 및 저장
        Reaction reaction = Reaction.builder()
                .reactor(user)
                .bottle(bottle)
                .build();

        Reaction savedReaction = reactionRepository.save(reaction);

        // 유리병 총 반응 개수
        bottle.setTotalCount(bottle.getTotalCount() + 1);

        // 반환
        return BottleReactionCreateResponse.builder()
                .bottleId(bottle.getBottleId())
                .totalCount(bottle.getTotalCount())
                .reactionCreatedAt(savedReaction.getReactionCreatedAt()) // 엔티티의 생성 시간
                .reactionUpdatedAt(savedReaction.getReactionUpdatedAt()) // 엔티티의 수정 시간
                .build();
    }

    //유리병 반응 조회
    public BottleReactionGetResponse getReactions(Long bottleId) {

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유리병입니다."));

        // 반환
        return BottleReactionGetResponse.builder()
                .bottleId(bottle.getBottleId())
                .totalCount(bottle.getTotalCount())
                .build();
    }

    // 오늘의 질문 조회
    public TodayQuestResponse getTodayQuest() {
        // 오늘 날짜 구하기
        Date today = new Date();

        // 질문 조회
        TodayQuest quest = todayQuestRepository.findByDate(today)
                .orElseThrow(() -> new IllegalArgumentException("오늘의 질문을 찾을 수 없습니다."));

        // 반환
        return TodayQuestResponse.fromEntity(quest);
    }

    // 유리병 상세페이지 조회
    public BottleDetailResponse getBottleDetail(Long bottleId, Long id) {

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유리병입니다."));

        // 반환
        return BottleDetailResponse.fromEntity(bottle, id);
    }

    // 유리병 북마크 추가
    @Transactional
    public BookmarkCreateResponse addBookmark(Long bottleId, Long id) {

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유리병입니다."));

        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 중복 확인
        if (bookmarkRepository.existsByUserAndBottle(user, bottle)) {
            throw new IllegalArgumentException("이미 저장된 유리병입니다.");
        }

        // 저장
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .bottle(bottle)
                .build();
        Bookmark saved = bookmarkRepository.save(bookmark);

        // 만료 시간 계산
        Timestamp expiresAt = new Timestamp(saved.getBookmarkCreatedAt().getTime() + (7L * 24 * 60 * 60 * 1000));

        // 반환
        return BookmarkCreateResponse.builder()
                .bookmarkId(saved.getBookmarkId())
                .bottleId(bottle.getBottleId())
                .bookmarkExpiresAt(expiresAt)
                .bookmarkCreatedAt(saved.getBookmarkCreatedAt())
                .bookmarkUpdatedAt(saved.getBookmarkUpdatedAt())
                .build();
        }

    // 북마크 목록 조회
    public List<BookmarkListResponse> getBookmarks(Long id) {

        // 현재 시간으로부터 7일 전 시간 계산
        Timestamp sevenDaysAgo = new Timestamp(System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000));

        // 리포지토리에 정의한 findActiveBookmarks 활용
        List<Bookmark> bookmarks = bookmarkRepository.findActiveBookmarks(id, sevenDaysAgo);

        return bookmarks.stream().map(b -> {
            Timestamp bookmarkExpiresAt = new Timestamp(b.getBookmarkCreatedAt().getTime() + (7L * 24 * 60 * 60 * 1000));
            return BookmarkListResponse.builder()
                    .bookmarkId(b.getBookmarkId())
                    .bottleId(b.getBottle().getBottleId())
                    .senderId(b.getBottle().getUser().getId())
                    .senderNickname(b.getBottle().getUser().getNickname())
                    .senderProfileImage(b.getBottle().getUser().getProfileImage())
                    .questionText(b.getBottle().getTodayQuest().getQuestionText())
                    .musicInfo(new BookmarkListResponse.MusicInfo(
                            b.getBottle().getTrackName(),
                            b.getBottle().getArtistName()
                    ))
                    .bookmarkExpiresAt(bookmarkExpiresAt)
                    .bookmarkCreatedAt(b.getBookmarkCreatedAt())
                    .bookmarkUpdatedAt(b.getBookmarkUpdatedAt())
                    .build();
        }).collect(Collectors.toList());
    }
    }

