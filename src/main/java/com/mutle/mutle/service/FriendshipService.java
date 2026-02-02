package com.mutle.mutle.service;


import com.mutle.mutle.dto.*;
import com.mutle.mutle.entity.*;
import com.mutle.mutle.repository.FriendShipRepository;
import com.mutle.mutle.repository.RepMusicRepository;
import com.mutle.mutle.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public FriendRequestResponse sendFriendRequest(Long meId, Long targetId)    {

        // 유저 확인
        User me = userRepository.findById(meId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 관계 확인
        Optional<FriendShip> existingRelation = friendShipRepository.findRelation(meId, targetId);

        if (existingRelation.isPresent()) {
            FriendShip relation = existingRelation.get();
            FriendshipStatus status = relation.getFriendshipStatus();

            if (status == FriendshipStatus.ACCEPTED) {
                throw new IllegalArgumentException("이미 친구인 사용자입니다.");
            }
            if (relation.getRequester().getId().equals(meId)) {
                throw new IllegalArgumentException("이미 신청을 보냈습니다.");
            } else {
                throw new IllegalArgumentException("이미 신청을 받았습니다.");
            }
        }

        // 3. 신청 저장
        FriendShip newRequest = FriendShip.builder()
                .requester(me)
                .receiver(target)
                .friendshipStatus(FriendshipStatus.ACCEPTED)
                .friendshipStatus(FriendshipStatus.valueOf("REQUEST_SENT"))
                .requestedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        FriendShip saved = friendShipRepository.save(newRequest);

        return FriendRequestResponse.builder()
                .friendRequestId(saved.getFriendRequestId())
                .targetNickname(target.getNickname())
                .friendshipStatus("REQUEST_SENT")
                .friendshipCreatedAt(Timestamp.valueOf(saved.getRequestedAt().toLocalDateTime()))
                .build();
    }

    // 친구 신청 취소
    @Transactional
    public FriendRequestCancelResponse cancelFriendRequest(Long meId, Long requestId) {

        // 신청 정보 확인
        FriendShip request = friendShipRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청 정보입니다."));

        // 본인이 보낸 신청인지 확인
        if (!request.getRequester().getId().equals(meId)) {
            throw new IllegalArgumentException("본인이 보낸 신청만 취소할 수 있습니다.");
        }

        // 취소 가능한 상태인지 확인
        if (request.getFriendshipStatus() != FriendshipStatus.valueOf("REQUEST_SENT")) {
            throw new IllegalArgumentException("이미 수락되었거나 취소할 수 없는 상태입니다.");
        }

        // 4. 삭제 처리
        User target = request.getReceiver();
        friendShipRepository.delete(request);

        return FriendRequestCancelResponse.builder()
                .friendRequestId(requestId)
                .targetId(target.getId())
                .targetNickname(target.getNickname())
                .friendshipStatus("NONE")
                .cancelledAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    // 받은 친구 신청 목록 조회
    public List<ReceivedRequestResponse> getReceivedRequests(Long id) {

        List<FriendShip> requests = friendShipRepository.findByReceiverIdAndFriendshipStatus(
                id, FriendshipStatus.valueOf("REQUEST_SENT"));

        return requests.stream().map(request -> {
            User sender = request.getRequester();

            Optional<RepMusic> repMusicOpt = repMusicRepository.findByUser(sender);

            return ReceivedRequestResponse.builder()
                    .friendRequestId(request.getFriendRequestId())
                    .id(sender.getId())
                    .nickname(sender.getNickname())
                    .profileImage(sender.getProfileImage())
                    .bio(sender.getBio())
                    .requestedAt(Timestamp.valueOf(request.getRequestedAt().toLocalDateTime()))
                    .repMusicInfo(repMusicOpt.map(rm ->
                            ReceivedRequestResponse.RepMusicInfo.builder()
                                    .trackName(rm.getMusic().getTrackName())
                                    .artistName(rm.getMusic().getArtistName())
                                    .build()
                    ).orElse(null))
                    .build();
        }).collect(Collectors.toList());
    }

    // 친구 신청 수락/거절
    @Transactional
    public FriendResponse respondFriendRequest(Long id, Long requestId, FriendRespondRequest body) {
        FriendShip request = friendShipRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청 정보입니다."));

        // 내게 온 신청인지 확인
        if (!request.getReceiver().getId().equals(id)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 상태 변경
        if ("ACCEPTED".equals(body.getFriendshipStatus())) {
            request.setFriendshipStatus(FriendshipStatus.ACCEPTED);
        } else {
            friendShipRepository.delete(request);
            return null;
        }

        return FriendResponse.builder()
                .friendRequestId(request.getFriendRequestId())
                .id(request.getRequester().getId())
                .nickname(request.getRequester().getNickname())
                .friendshipStatus("ACCEPTED")
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    // 친구 목록 조회
    public List<FriendListResponse> getFriendList(Long id) {

        List<FriendShip> friendships = friendShipRepository.findByAcceptedFriends(id);
        return friendships.stream().map(fs -> {

            User target = fs.getRequester().getId().equals(id) ? fs.getReceiver() : fs.getRequester();

            Optional<RepMusic> repMusicOpt = repMusicRepository.findByUser(target);

            return FriendListResponse.builder()
                    .id(target.getId())
                    .nickname(target.getNickname())
                    .profileImage(target.getProfileImage())
                    .bio(target.getBio())
                    .repMusicInfo(repMusicOpt.map(rm ->
                            FriendListResponse.RepMusicInfo.builder()
                                    .trackName(rm.getMusic().getTrackName())
                                    .artistName(rm.getMusic().getArtistName())
                                    .artworkUrl60(rm.getMusic().getArtworkUrl60())
                                    .build()
                    ).orElse(null))
                    .build();
        }).collect(Collectors.toList());
    }

    // 친구 삭제
    @Transactional
    public FriendDeleteResponse deleteFriend(Long id, Long targetId) {
        // 친구 관계 확인
        FriendShip friendship = friendShipRepository.findRelation(id, targetId)
                .filter(fs -> fs.getFriendshipStatus() == FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 친구 관계가 존재하지 않습니다."));

        User target = friendship.getRequester().getId().equals(id) ? friendship.getReceiver() : friendship.getRequester();

        // 2. 삭제 처리
        friendShipRepository.delete(friendship);

        return FriendDeleteResponse.builder()
                .targetId(target.getId())
                .targetNickname(target.getNickname())
                .unfriendedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    // 친구 관계 판단
    private String determineFriendshipStatus(Long meId, Long targetId) {

        Optional<FriendShip> friendship = friendShipRepository.findRelation(meId, targetId);

        if (friendship.isEmpty()) return "NONE"; // 아무 관계 X

        FriendShip relation = friendship.get();
        if (relation.getFriendshipStatus() == FriendshipStatus.ACCEPTED) return "ACCEPTED"; // 이미 친구

        // 누가 먼저 신청했는지에 따라 SENT / RECEIVED
        return relation.getRequester().getId().equals(meId) ? "REQUEST_SENT" : "REQUEST_RECEIVED";
    }

}
