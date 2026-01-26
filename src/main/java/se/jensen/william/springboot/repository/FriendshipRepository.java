package se.jensen.william.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.jensen.william.springboot.entities.Friendship;
import se.jensen.william.springboot.entities.User;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByRequesterAndAddressee(User requester, User addressee);
    boolean existsByRequesterAndAddressee(User requester, User addressee);

    @Query("""
    SELECT f FROM Friendship f
    WHERE f.status = :status
      AND (f.requester = :user OR f.addressee = :user)
""")
    List<Friendship> findAcceptedFriendshipsForUser(
            @Param("user") User user,
            @Param("status") Friendship.FriendshipStatus status
    );

    @Query("""
    SELECT f FROM Friendship f
    WHERE f.requester = :user OR f.addressee = :user
""")
    List<Friendship> findAllFriendshipStatusForUser(@Param("user") User user);

}
