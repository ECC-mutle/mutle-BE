package com.mutle.mutle.service;


import com.mutle.mutle.dto.FriendSearchResponse;
import com.mutle.mutle.entity.*;
import com.mutle.mutle.repository.FriendShipRepository;
import com.mutle.mutle.repository.RepMusicRepository;
import com.mutle.mutle.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class FriendshipService {

    @Autowired
    private FriendShipRepository friendShipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RepMusicRepository repMusicRepository;

    // 친구 검색
    public FriendSearchResponse searchFriend(Long currentUserId, String type, String keyword) {

        // 조건에 따라 사용자 검색
        User targetUser;

        //이메일 검색
        if ("EMAIL".equals(type)) {
            targetUser = userRepository.findByEmail(keyword)
                    .orElseThrow(() -> new IllegalArgumentException("해당 정보를 가진 사용자가 존재하지 않습니다."));
        } //id 검색
        else if ("ID".equals(type)) {
            targetUser = userRepository.findByUserId(keyword)
                    .orElseThrow(() -> new IllegalArgumentException("해당 정보를 가진 사용자가 존재하지 않습니다."));
        } // 검색 결과 없음
        else {
            throw new IllegalArgumentException("잘못된 검색 조건입니다.");
        }

        // 관계 판단
        String status = determineFriendshipStatus(currentUserId, targetUser.getId());

        // 대표곡 조회
        Optional<RepMusic> repMusicOpt = repMusicRepository.findByUser(targetUser);

        //반환
        return FriendSearchResponse.builder()
                .id(targetUser.getId())
                .nickname(targetUser.getNickname())
                .profileImage(targetUser.getProfileImage())
                .bio(targetUser.getBio())
                .friendshipStatus(status)
                .repMusicInfo(repMusicOpt.map(rm -> {
                    Music m = rm.getMusic(); // RepMusic 안에 담긴 Music 엔티티 꺼내기
                    return FriendSearchResponse.RepMusicInfo.builder()
                            .trackName(m.getTrackName())
                            .artistName(m.getArtistName())
                            .artworkUrl60(m.getArtworkUrl60())
                            .build();
                }).orElse(null)) // 대표곡이 없으면 null
                .build();
    }


    // 친구 신청 보내기
    @Transactional
    public void sendFriendRequest() {}

    // 친구 신청 취소
    @Transactional
    public void cancelFriendRequest() {}

    // 받은 친구 신청 목록 조회
    public void getReceivedRequests() {}

    // 친구 신청 수락/거절
    @Transactional
    public void respondFriendRequest() {}

    // 친구 목록 조회
    public void getFriendList() {}

    // 친구 삭제
    @Transactional
    public void deleteFriend() {}

    // 친구 관계 판단
    private String determineFriendshipStatus(Long meId, Long youId) {

        Optional<FriendShip> friendship = friendShipRepository.findRelation(meId, youId);

        if (friendship.isEmpty()) return "NONE"; // 아무 관계 X

        FriendShip relation = friendship.get();
        if (relation.getFriendshipStatus() == FriendshipStatus.ACCEPTED) return "ACCEPTED"; // 이미 친구

        // 누가 먼저 신청했는지에 따라 SENT / RECEIVED
        return relation.getRequester().getId().equals(meId) ? "REQUEST_SENT" : "REQUEST_RECEIVED";
    }

}
