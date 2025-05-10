DELIMITER //
CREATE PROCEDURE GetFeedItemReactionInfo(
    IN fItemType VARCHAR(20)
)
BEGIN
    DECLARE isLike BOOLEAN DEFAULT FALSE;
    DECLARE isDislike BOOLEAN DEFAULT FALSE;
    DECLARE isShare BOOLEAN DEFAULT FALSE;
    DECLARE isReport BOOLEAN DEFAULT FALSE;

    -- set isLike
    IF (SELECT COUNT(*) FROM user_reaction
        WHERE feed_item_id = fItemType
          AND object_type = 'LIKE') > 0
    THEN
        SET isLike = TRUE;
    END IF;

    -- set isDislike
    IF (SELECT COUNT(*) FROM user_reaction
        WHERE feed_item_id = fItemType
          AND object_type = 'DISLIKE') > 0
    THEN
        SET isDislike = TRUE;
    END IF;

    -- set isShare
    IF (SELECT COUNT(*) FROM user_reaction
        WHERE feed_item_id = fItemType
          AND object_type = 'SHARE') > 0
    THEN
        SET isShare = TRUE;
    END IF;

    -- set isReport
    IF (SELECT COUNT(*) FROM user_reaction
        WHERE feed_item_id = fItemType
          AND object_type = 'REPORT') > 0
    THEN
        SET isReport = TRUE;
    END IF;

    -- return both aggregate + state variables
    SELECT DISTINCT ra.likes,
           isLike AS isLike,
           ra.dislikes,
           isDislike AS isDislike,
           ra.shares,
           isShare AS isShare,
           ra.reports,
           isReport AS isReport
    FROM reaction_aggregate ra
    WHERE ra.object_type = object_type;
END //
DELIMITER ;