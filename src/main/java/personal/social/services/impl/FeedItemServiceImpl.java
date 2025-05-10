package personal.social.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.social.dto.FeedItemDTO;
import personal.social.dto.ReactionInfoDTO;
import personal.social.dto.ReactionRequest;
import personal.social.enums.ObjectType;
import personal.social.enums.ReactionType;
import personal.social.enums.Status;
import personal.social.helper.CommonHelpers;
import personal.social.model.*;
import personal.social.repository.FeedItemContentRepository;
import personal.social.repository.FeedItemRepository;
import personal.social.repository.ReactionSummaryRepository;
import personal.social.repository.UserReactionRepository;
import personal.social.services.FeedItemService;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedItemServiceImpl implements FeedItemService {
    private final CommonHelpers helpers;
    private final FeedItemRepository feedItemRepo;
    private final FeedItemContentRepository fItemContentRepo;
    private final ReactionSummaryRepository summaryRepo;
    private final UserReactionRepository userReactionRepo;

    @Autowired
    public FeedItemServiceImpl(
            CommonHelpers helpers,
            FeedItemRepository feedItemRepo,
            FeedItemContentRepository fItemContentRepo,
            ReactionSummaryRepository summaryRepo,
            UserReactionRepository userReactionRepo) {
        this.helpers = helpers;
        this.feedItemRepo = feedItemRepo;
        this.fItemContentRepo = fItemContentRepo;
        this.summaryRepo = summaryRepo;
        this.userReactionRepo = userReactionRepo;
    }

    @Override
    public Page<FeedItemDTO> getAllFeedItem(Integer pageNumber, Integer pageSize, ObjectType fItemType) throws Exception {
        List<Object[]> results = feedItemRepo.findAllFeedItems(pageNumber, pageSize, fItemType.toString(), null);

        // throw exception if results have no items
        if (results.isEmpty()) {
            throw new RuntimeException("No Post Found!");
        }

        List<FeedItemDTO> dtoList = results.stream().map(obj -> new FeedItemDTO(
                ((Number) obj[0]).longValue(),
                ((Timestamp) obj[1]).toLocalDateTime(), // created At
                ((Timestamp) obj[2]).toLocalDateTime(), // updated at
                Status.valueOf((String) obj[3]), // feed item status
                ObjectType.valueOf((String) obj[4]), //feed item type(POST, COMMENT)
                (obj[5].toString()), // content
                ((Number) obj[6]).longValue(), // user id
                ((Number) obj[7]).longValue() // parent feed item id
        )).collect(Collectors.toList());

        long total = feedItemRepo.countAllFeedItems(fItemType.name()); // call procedure count
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public ReactionInfoDTO getFeedItemReactionInfo(Long feedItemId) {
        return feedItemRepo.findFeedItemReactionInfo(feedItemId);
    }

    @Override
    @Transactional
    public ReactionInfoDTO createFeedItem(FeedItemDTO feedItemDTO, Users user) {
        LocalDateTime currentTime = LocalDateTime.now();
        // create and save new feed item to db
        FeedItem newFeedItem = new FeedItem(
                currentTime,
                currentTime,
                Status.ACTIVE,
                feedItemDTO.getItemType(),
                user,
                feedItemDTO.getParentFItem()
        );
        helpers.saveEntity(newFeedItem, feedItemRepo, "feedItem record");

        // create and save feed item content
        FeedItemContent fItemContent = new FeedItemContent(
                feedItemDTO.getContent(),
                currentTime,
                newFeedItem
        );
        helpers.saveEntity(fItemContent, fItemContentRepo, "feedItem content");

        // create and save feed item reaction rummary
        ReactionSummary reactionSummary = new ReactionSummary(
                feedItemDTO.getItemType(),
                newFeedItem,
                0L, 0L, 0L, 0L
        );
        helpers.saveEntity(reactionSummary, summaryRepo, "feedItem reaction summary");

        return new ReactionInfoDTO(
                0L, false,
                0L, false,
                0L, false,
                0L, false
        );
    }

    @Override
    @Transactional
    public String editFeedItem(FeedItemDTO feedItemDTO, ReactionRequest reactionRequest, Users user) throws AccessDeniedException {
        FeedItem existedFeedItem = feedItemRepo.findByFeedItemId(feedItemDTO.getFeedItemId());

        // check if status or content changed
        if (existedFeedItem != null) {
            boolean isFeedItemChanged = false;
            boolean isContentChanged = false;
            // check user permission
            if (!existedFeedItem.getUser().equals(user)) {
                throw new AccessDeniedException("You don't have permission to edit this feed item!");
            }

            // set new status if its changed
            if (!existedFeedItem.getFstatus().equals(feedItemDTO.getFstatus())) {
                existedFeedItem.setFstatus(feedItemDTO.getFstatus());
                isFeedItemChanged = true;
            }

            FeedItemContent existedFiContent = fItemContentRepo.findByFItemId(existedFeedItem.getFeedItemId());
            // set new content if its changed
            if (!existedFiContent.getContent().equals(feedItemDTO.getContent())) {
                existedFiContent.setContent(feedItemDTO.getContent());
                isContentChanged = true;
            }

            if (isFeedItemChanged) {
                helpers.saveEntity(existedFeedItem, feedItemRepo, "feed item status");
            }
            if (isContentChanged) {
                helpers.saveEntity(existedFiContent, fItemContentRepo, "feed item content");
            }
        } else {
            throw new RuntimeException("FeedItem not existed!");
        }

        // if user reaction to feed item changed
        if (reactionRequest.getItemType() == null ||
            reactionRequest.getReactionType() == null ||
            reactionRequest.getItemId() == null) {
            return "";
        }

        switch (reactionRequest.getReactionType()) {
            case LIKE -> handleLike(reactionRequest, user, existedFeedItem);
            case DISLIKE -> handleDislike(reactionRequest, user, existedFeedItem);
            case SHARE -> {
                handleShare(reactionRequest);
                return "Feed item shared!";
            }
            case REPORT -> {
                handleReport(reactionRequest);
                return "Feed item reported";
            }
            default ->
                    throw new IllegalArgumentException("Unsupported reaction type: " + reactionRequest.getReactionType());
        }
        return "";
    }

    // -------------- handle LIKE reaction
    private void handleLike(ReactionRequest reactionRequest, Users user, FeedItem existedFeedItem) {
        handleReaction(reactionRequest, user, existedFeedItem, "like");
    } // -------- end handle like post, comment

    // -------------- handle DISLIKE reaction
    private void handleDislike(ReactionRequest reactionRequest, Users user, FeedItem existedFeedItem) {
        handleReaction(reactionRequest, user, existedFeedItem, "dislike");
    } // ----------- end handle dislike post, comment

    // -------------- handle SHARE reaction
    private void handleShare(ReactionRequest reactionRequest) {
        // handle share (POST, COMMENT)
    } // ------------ end handle share post

    // -------------- handle REPORT reaction
    private void handleReport(ReactionRequest reactionRequest) {
        // handle report (POST, COMMENT)
    }

    private void handleReaction(ReactionRequest reactionRequest, Users user, FeedItem existedFeedItem, String operation) {
        ReactionSummary existedReactionSum = summaryRepo.findByFeedItemId(reactionRequest.getItemId());
        UserReaction userReaction = userReactionRepo.findByFeedItemId(reactionRequest.getItemId(), reactionRequest.getReactionType());

        if (userReaction == null) {
            // create new if user not reacted yet
            userReaction = createNewUserReaction(
                    user, existedFeedItem,
                    reactionRequest.getReactionType(),
                    reactionRequest.getItemType()
            );

            existedReactionSum = updateReactionCount(
                    existedReactionSum,
                    reactionRequest.getReactionType(),
                    true
            );
        } else if (userReaction.getReactionType().equals(reactionRequest.getReactionType())) {
            // Already reacted -> undo
            existedReactionSum = updateReactionCount(
                    existedReactionSum,
                    reactionRequest.getReactionType(),
                    false
            );

            // set opposite reaction type and reacted time
            userReaction.setReactionType(
                    getOppositeReactionType(reactionRequest.getReactionType())
            );
            userReaction.setReactedAt(LocalDateTime.now());
        } else {
            // Switch to this reaction if already exist opposite reaction
            userReaction.setReactionType(reactionRequest.getReactionType());
            userReaction.setReactedAt(LocalDateTime.now());
            existedReactionSum = updateReactionCount(existedReactionSum, reactionRequest.getReactionType(), true);
        }

        // Save entities
        helpers.saveEntity(userReaction, userReactionRepo, "user " + operation + " reaction");
        helpers.saveEntity(existedReactionSum, summaryRepo, "feed item summary(" + operation + ")");
    } // --------- end handle Reaction

    private UserReaction createNewUserReaction(Users user, FeedItem feedItem, ReactionType reactionType, ObjectType itemType) {
        UserReaction reaction = new UserReaction();
        reaction.setReactedAt(LocalDateTime.now());
        reaction.setFeedItem(feedItem);
        reaction.setReactionType(reactionType);
        reaction.setObjectType(itemType);
        reaction.setUser(user);
        return reaction;
    }

    private ReactionSummary updateReactionCount(ReactionSummary existedReactionSum, ReactionType reactionType, boolean increment) {
        long delta = increment ? 1 : -1;// if increment is true -> 1 else -> -1

        switch (reactionType) {
            case LIKE -> existedReactionSum.setLikes(Math.max(0, existedReactionSum.getLikes() + delta));
            case DISLIKE -> existedReactionSum.setDislikes(Math.max(0, existedReactionSum.getDislikes() + delta));
            case SHARE -> existedReactionSum.setShares(Math.max(0, existedReactionSum.getShares() + delta));
            case REPORT -> existedReactionSum.setReports(Math.max(0, existedReactionSum.getReports() + delta));
        }
        return existedReactionSum;
    }

    private ReactionType getOppositeReactionType(ReactionType type) {
        return switch (type) {
            case LIKE -> ReactionType.UNLIKE;
            case DISLIKE -> ReactionType.UNDISLIKE;
            case SHARE -> ReactionType.UNSHARE;
            case REPORT -> ReactionType.UNREPORT;
            default -> throw new IllegalArgumentException("No opposite defined for: " + type);
        };
    }
}
