package com.mutle.mutle.service;

import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.*;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;
import com.mutle.mutle.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    public BottleCreateResponse createBottle(Long id, Long questionId, BottleCreateRequest request) {

        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 질문 조회 및 예외 발생
        TodayQuest quest = todayQuestRepository.findByQuestionId(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODAY_QUEST_NOT_FOUND));

        //이미 보냈는지 확인
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        boolean alreadySent = bottleRepository.existsByUserAndBottleCreatedAtAfter(user, todayStart);

        if (alreadySent) {
            throw new CustomException(ErrorCode.ALREADY_SENT_TODAY);
        }

        //music이 db에 있는지 확인, 없으면 생성
        Music music=musicRepository
                .findByTrackNameAndArtistName(request.getMusicInfo().getTrackName(), request.getMusicInfo().getArtistName())
                .orElseGet(()->
                        musicRepository.save(
                                Music.builder()
                                        .trackName(request.getMusicInfo().getTrackName())
                                        .artistName(request.getMusicInfo().getArtistName())
                                        .artworkUrl60(request.getMusicInfo().getArtworkUrl60())
                                        .build()
                        )
                );

        // 글자 수 제한 체크
        if (request.getMemo().length() > 200) {
            throw new CustomException(ErrorCode.CONTENT_TOO_LONG);
        }

        // 유리병 엔티티 생성
        Bottle bottle = Bottle.builder()
                .user(user)
                .todayQuest(quest)
                .music(music)
                .memo(request.getMemo())
                .isShared(request.getIsShared())
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
                .orElseThrow(() -> new CustomException(ErrorCode.BOTTLE_NOT_ARRIVED));
        // 반환
        return BottleRandomResponse.fromEntity(randomBottle, id);
    }

    // 유리병 반응 남기기
    @Transactional
    public BottleReactionCreateResponse addReaction(Long id, Long bottleId) {
        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOTTLE_NOT_FOUND));

        // 중복 확인
        if (reactionRepository.existsByReactorAndBottle(user, bottle)) {
            throw new CustomException(ErrorCode.REACTION_DUPLICATION);
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
                .orElseThrow(() -> new CustomException(ErrorCode.BOTTLE_NOT_FOUND));

        // 반환
        return BottleReactionGetResponse.builder()
                .bottleId(bottle.getBottleId())
                .totalCount(bottle.getTotalCount())
                .build();
    }

    // 오늘의 질문 조회
    public TodayQuestResponse getTodayQuest() {
        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();

        Date start = Date.from(
                today.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
        Date end = Date.from(
                today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        // 질문 조회
        TodayQuest quest = todayQuestRepository.findByDate(start, end)
                .orElseThrow(() -> new CustomException(ErrorCode.TODAY_QUEST_NOT_FOUND));

        // 반환
        return TodayQuestResponse.fromEntity(quest);
    }

    // 유리병 상세페이지 조회
    public BottleDetailResponse getBottleDetail(Long bottleId, Long id) {

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findById(bottleId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOTTLE_NOT_FOUND));

        // 생성일로부터 7일이 지났는지 확인
        if (bottle.getBottleCreatedAt().toLocalDateTime().isBefore(LocalDateTime.now().minusDays(7))) {
            throw new CustomException(ErrorCode.EXPIRED_BOTTLE);
        }

        // 반환
        return BottleDetailResponse.fromEntity(bottle, id);
    }

    // 유리병 북마크 추가
    @Transactional
    public BookmarkCreateResponse addBookmark(Long bottleId, Long id) {

        // 유리병 조회 및 예외 발생
        Bottle bottle = bottleRepository.findByBottleId(bottleId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOTTLE_NOT_FOUND));

        // 유저 조회 및 예외 발생
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 중복 확인
        if (bookmarkRepository.existsByUserAndBottle(user, bottle)) {
            throw new CustomException(ErrorCode.BOOKMARK_DUPLICATION);
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
                            b.getBottle().getMusic().getTrackName(),
                            b.getBottle().getMusic().getArtistName(),
                            b.getBottle().getMusic().getArtworkUrl60()
                    ))
                    .bookmarkExpiresAt(bookmarkExpiresAt)
                    .bookmarkCreatedAt(b.getBookmarkCreatedAt())
                    .bookmarkUpdatedAt(b.getBookmarkUpdatedAt())
                    .build();
        }).collect(Collectors.toList());
    }
    }

