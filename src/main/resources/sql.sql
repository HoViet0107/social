use social_media;
DELIMITER //
CREATE PROCEDURE GetTopLevelComments(
    IN postIdParam BIGINT, -- ✅ Sửa từ LONG thành BIGINT
    IN pageNumber INT,
    IN pageSize INT
)
BEGIN
    DECLARE offsetValue INT;
    SET offsetValue = (pageNumber - 1) * pageSize;

    SELECT 
        CAST(c.comment_id AS SIGNED) AS commentId,  -- ✅ Ép kiểu về BIGINT
        c.created_at AS createdAt,
        c.last_updated AS lastUpdated,
        c.comment_status AS commentStatus,
        CAST(c.likes AS SIGNED) AS likes,  -- ✅ Ép kiểu về BIGINT
        CAST(c.dislikes AS SIGNED) AS dislikes,  -- ✅ Ép kiểu về BIGINT
        cc.content,
        CAST(c.user_id AS SIGNED) AS userId,
        CAST(c.post_id AS SIGNED) AS postId,
        COALESCE(CAST(c.parent_comment_id AS SIGNED), 0) AS parentCommentId
    FROM comments c
    JOIN comment_content cc ON c.comment_id = cc.comment_id
    WHERE c.post_id = postIdParam
      AND c.comment_status = 'ACTIVE'
      AND (c.parent_comment_id IS NULL OR c.parent_comment_id = 0)
      AND c.last_updated = cc.last_updated
    ORDER BY c.created_at DESC
    LIMIT pageSize OFFSET offsetValue;
END//
DELIMITER ;

