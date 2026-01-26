package se.jensen.william.springboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.william.springboot.dto.FriendshipResponseDTO;
import se.jensen.william.springboot.dto.UserResponseDTO;
import se.jensen.william.springboot.entities.Friendship;
import se.jensen.william.springboot.entities.User;
import se.jensen.william.springboot.exceptions.*;
import se.jensen.william.springboot.mapper.FriendshipMapper;
import se.jensen.william.springboot.mapper.UserMapper;
import se.jensen.william.springboot.repository.FriendshipRepository;
import se.jensen.william.springboot.repository.UserRepository;

import java.util.List;

@Service
public class FriendshipService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final UserMapper userMapper;

    public FriendshipService(UserRepository userRepository, FriendshipRepository friendshipRepository, FriendshipMapper friendshipMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendshipMapper = friendshipMapper;
        this.userMapper = userMapper;
    }

    public FriendshipResponseDTO sendFriendRequest (Long senderId, Long receiverId) {
        if(senderId.equals(receiverId))
            throw new FriendRequestToSelfException(receiverId);

        User requester = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User addressee = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        // Check if the request already exists in either direction
        if (friendshipRepository.existsByRequesterAndAddressee(requester, addressee) ||
            friendshipRepository.existsByRequesterAndAddressee(addressee, requester)) {
            throw new FriendRequestAlreadyExistsException("Friendship or request already exists");
        }

        Friendship friendship = new Friendship(requester, addressee, Friendship.FriendshipStatus.PENDING);
        Friendship savedFriendship = friendshipRepository.save(friendship);

        return friendshipMapper.toDto(savedFriendship);
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponseDTO> getAllFriendshipStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        return friendshipRepository.findAllFriendshipStatusForUser(user).stream()
                .map(friendshipMapper::toDto)
                .toList();
    }

    @Transactional
    public Friendship acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId));

        if (friendship.getStatus() !=  Friendship.FriendshipStatus.PENDING) {
            throw new InvalidFriendshipStateException(
                    "Friendship is not in PENDING state"
            );
        }

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);

        return friendship;
    }

    @Transactional
    public Friendship declineFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFoundException(friendshipId));

        if (friendship.getStatus() !=  Friendship.FriendshipStatus.PENDING) {
            throw new InvalidFriendshipStateException(
                    "Friendship is not in PENDING state"
            );
        }

        friendship.setStatus(Friendship.FriendshipStatus.DECLINED);

        return friendship;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Friendship> friends =
                friendshipRepository.findAcceptedFriendshipsForUser(
                        user,
                        Friendship.FriendshipStatus.ACCEPTED
                );

        return friends.stream()
                .map(friendship -> {
                    User friend =
                            friendship.getRequester().equals(user)
                                    ? friendship.getAddressee()
                                    : friendship.getRequester();

                    return userMapper.toDto(friend);
                })
                .toList();
    }

}
