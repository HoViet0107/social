package personal.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.social.model.FeedItemContent;

public interface FeedItemContentRepository extends JpaRepository<FeedItemContent, Long> {
    @Query(value = "SELECT fic FROM FeedItemContent fic WHERE fic.feedItem.feedItemId =:fItemId")
    FeedItemContent findByFItemId (Long fItemId);
}
