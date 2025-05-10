package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.social.model.FeedItemContent;

/**
 * Repository interface for managing feed item content
 * Provides methods to find content by feed item ID
 */
public interface FeedItemContentRepository extends JpaRepository<FeedItemContent, Long> {
    /**
     * Finds feed item content by feed item ID
     * 
     * @param fItemId The feed item ID
     * @return The feed item content if found
     */
    @Query(value = "SELECT fic FROM FeedItemContent fic WHERE fic.feedItem.feedItemId =:fItemId")
    FeedItemContent findByFItemId(Long fItemId);
}
