package ru.flow.socialnetwork.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flow.socialnetwork.entity.FriendRequest;
import ru.flow.socialnetwork.entity.FriendRequest.Status;
import ru.flow.socialnetwork.entity.User;
import ru.flow.socialnetwork.repository.FriendRequestRepository;
import ru.flow.socialnetwork.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRequestRepository friendRequestRepository,
                         UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean sendFriendRequest(String senderUsername, String receiverUsername) {
        if (senderUsername.equals(receiverUsername)) {
            throw new IllegalArgumentException("Нельзя отправить запрос самому себе");
        }
        if (friendRequestRepository.findPendingRequest(senderUsername, receiverUsername).isPresent()) {
            return false;
        }
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Отправитель не найден"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Получатель не найден"));
        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);
        return true;
    }

    @Transactional
    public boolean acceptFriendRequest(Long requestId, String receiverUsername) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));
        if (!request.getReceiver().getUsername().equals(receiverUsername)) {
            throw new IllegalArgumentException("Вы не можете принять этот запрос");
        }
        if (request.getStatus() != Status.PENDING) {
            return false;
        }
        request.setStatus(Status.ACCEPTED);
        friendRequestRepository.save(request);
        return true;
    }

    @Transactional
    public boolean rejectFriendRequest(Long requestId, String receiverUsername) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));
        if (!request.getReceiver().getUsername().equals(receiverUsername)) {
            throw new IllegalArgumentException("Вы не можете отклонить этот запрос");
        }
        if (request.getStatus() != Status.PENDING) {
            return false;
        }
        request.setStatus(Status.REJECTED);
        friendRequestRepository.save(request);
        return true;
    }

    @Transactional
    public boolean cancelFriendRequest(Long requestId, String senderUsername) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));
        if (!request.getSender().getUsername().equals(senderUsername)) {
            throw new IllegalArgumentException("Вы не можете отменить этот запрос");
        }
        if (request.getStatus() != Status.PENDING) {
            return false;
        }
        friendRequestRepository.delete(request);
        return true;
    }

    @Transactional
    public boolean removeFriend(String user1, String user2) {
        List<FriendRequest> requests = friendRequestRepository.findBetweenUsers(user1, user2);
        boolean removed = false;
        for (FriendRequest request : requests) {
            if (request.getStatus() == Status.ACCEPTED) {
                friendRequestRepository.delete(request);
                removed = true;
            }
        }
        return removed;
    }

    public String getFriendshipStatus(String user1, String user2) {
        List<FriendRequest> requests = friendRequestRepository.findBetweenUsers(user1, user2);
        if (requests.isEmpty()) {
            return "NOT_EXISTS";
        }
        return requests.get(0).getStatus().name();
    }

    public Long getRequestId(String sender, String receiver) {
        return friendRequestRepository.findPendingRequest(sender, receiver)
                .map(FriendRequest::getId)
                .orElse(-1L);
    }

    public boolean isRequestSender(String potentialSender, String receiver) {
        return friendRequestRepository.findPendingRequest(potentialSender, receiver).isPresent();
    }

    public List<String> getFriendRequestSenders(String receiverUsername) {
        return friendRequestRepository.findByReceiverUsernameAndStatus(receiverUsername, Status.PENDING)
                .stream()
                .map(fr -> fr.getSender().getUsername())
                .collect(Collectors.toList());
    }

    public List<String> getFriendsList(String username) {
        return friendRequestRepository.findAcceptedFriendships(username)
                .stream()
                .map(fr -> fr.getSender().getUsername().equals(username)
                        ? fr.getReceiver().getUsername()
                        : fr.getSender().getUsername())
                .collect(Collectors.toList());
    }
}