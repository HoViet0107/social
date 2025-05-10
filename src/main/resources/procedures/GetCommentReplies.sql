DELIMITER //
CREATE PROCEDURE GetCommentReplies(
	IN postIdParam BIGINT,
    IN pageNumber INT,
    IN pageSize INT,
    IN parentCmtId INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (pageNumber - 1) * pageSize;

    SELECT
		c.comment_id AS commentId,
		c.created_at AS createdAt,
		c.last_updated AS lastUpdated,
		c.comment_status AS commentStatus,
		c.likes AS likes,
		c.dislikes AS dislikes,
		cc.content,
		c.user_id AS userId,
		c.post_id AS postId,
		c.parent_comment_id AS parentCommentId
    FROM comments c
    JOIN comment_content cc ON c.comment_id = cc.comment_id
	WHERE c.comment_status = 'ACTIVE'
       AND c.parent_comment_id != 0
       AND c.last_updated = cc.last_updated
       AND c.parent_comment_id = parentCmtId
    ORDER BY c.created_at DESC
    LIMIT pageSize OFFSET offsetValue;
END //
DELIMITER ;