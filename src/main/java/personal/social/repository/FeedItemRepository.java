package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.social.dto.ReactionInfoDTO;
import personal.social.enums.ObjectType;
import personal.social.model.FeedItem;

import java.util.List;

@Repository
public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {
    @Query(value = "CALL GetTopLevelFeedItems(:pageNumber, :pageSize, :fItemType, :parentItemId)", nativeQuery = true)
    List<Object[]> findAllFeedItems(
            @Param("pageNumber") Integer pageNumber,
            @Param("pageSize") Integer pageSize,
            @Param("fItemType") String fItemType,
            @Param("parentItemId") Long parentItemId
            );

    @Query(value = "CALL GetTopLevelFeedItemsCount(:fItemType)", nativeQuery = true)
    long countAllFeedItems(@Param("fItemType") String fItemType);

    @Query(value = "CALL GetFeedItemReactionInfo(:feedItemId)", nativeQuery = true)
    ReactionInfoDTO findFeedItemReactionInfo(
            @Param("feedItemId") Long feedItemId
    );

    @Query("SELECT fi FROM FeedItem fi WHERE fi.feedItemId =:feedItemId")
    FeedItem findByFeedItemId(Long feedItemId);
}
