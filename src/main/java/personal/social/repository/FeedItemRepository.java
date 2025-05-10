package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.social.dto.ReactionInfoDTO;
import personal.social.model.FeedItem;

import java.util.List;

/**
 * Repository interface for managing feed items (posts, comments)
 * Provides methods to retrieve feed items with pagination and reaction information
 */
@Repository
public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {
    /**
     * Retrieves top-level feed items with pagination
     * 
     * @param pageNumber The page number
     * @param pageSize The page size
     * @param fItemType The feed item type (POST, COMMENT)
     * @param parentItemId The parent item ID (for comments)
     * @return List of feed items as Object arrays
     */
    @Query(value = "CALL GetTopLevelFeedItems(:pageNumber, :pageSize, :fItemType, :parentItemId)", nativeQuery = true)
    List<Object[]> findAllFeedItems(
            @Param("pageNumber") Integer pageNumber,
            @Param("pageSize") Integer pageSize,
            @Param("fItemType") String fItemType,
            @Param("parentItemId") Long parentItemId
            );

    /**
     * Counts all feed items of a specific type
     * 
     * @param fItemType The feed item type (POST, COMMENT)
     * @return The total count of feed items
     */
    @Query(value = "CALL GetTopLevelFeedItemsCount(:fItemType)", nativeQuery = true)
    long countAllFeedItems(@Param("fItemType") String fItemType);

    /**
     * Retrieves reaction information for a feed item
     * 
     * @param feedItemId The feed item ID
     * @return Reaction information DTO
     */
    @Query(value = "CALL GetFeedItemReactionInfo(:feedItemId)", nativeQuery = true)
    ReactionInfoDTO findFeedItemReactionInfo(
            @Param("feedItemId") Long feedItemId
    );

    /**
     * Finds a feed item by its ID
     * 
     * @param feedItemId The feed item ID
     * @return The feed item if found
     */
    @Query("SELECT fi FROM FeedItem fi WHERE fi.feedItemId =:feedItemId")
    FeedItem findByFeedItemId(Long feedItemId);
}
