package ru.flow.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.flow.socialnetwork.entity.FriendRequest;
import ru.flow.socialnetwork.entity.FriendRequest.Status;
import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.sender.username = :user1 AND fr.receiver.username = :user2) OR " +
            "(fr.sender.username = :user2 AND fr.receiver.username = :user1) " +
            "ORDER BY fr.createdAt DESC")
    List<FriendRequest> findBetweenUsers(@Param("user1") String user1,
                                         @Param("user2") String user2);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.sender.username = :sender " +
            "AND fr.receiver.username = :receiver AND fr.status = 'PENDING'")
    Optional<FriendRequest> findPendingRequest(@Param("sender") String sender,
                                               @Param("receiver") String receiver);

    List<FriendRequest> findByReceiverUsernameAndStatus(String receiver, Status status);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.sender.username = :username OR fr.receiver.username = :username) " +
            "AND fr.status = 'ACCEPTED'")
    List<FriendRequest> findAcceptedFriendships(@Param("username") String username);
}