DELIMITER //
CREATE PROCEDURE GetTopLevelFeedItems(
    IN pageNumber INT,
    IN pageSize INT,
    IN fItemType VARCHAR(20),
    IN parentItemId BIGINT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (pageNumber - 1) * pageSize;

    SELECT
        fi.feed_item_id AS feedItemId,
        fi.created_at AS createdAt,
        fi.updated_at AS updatedAt,
        fi.f_status AS feedItemStatus,
        fi.item_type AS itemType,
        fic.content,
        fi.user_id AS userId,
        COALESCE(fi.parent_item_id, 0) AS parentItemId
    FROM feed_item fi
    JOIN feed_item_content fic ON fi.feed_item_id = fic.feed_item_id
    WHERE fi.f_status = 'ACTIVE'
      AND fi.item_type = fItemType
      AND (
                (parentItemId IS NULL AND (fi.parent_item_id IS NULL OR fi.parent_item_id = 0))
                OR (parentItemId IS NOT NULL AND fi.parent_item_id = parentItemId)
            )
    ORDER BY fi.created_at DESC
    LIMIT pageSize OFFSET offsetValue;
END //
DELIMITER ;