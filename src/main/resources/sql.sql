use social_media;
DELIMITER //
CREATE PROCEDURE GetTopLevelComments(
    IN postIdParam INT
)
BEGIN
    SELECT 
        c.comment_id AS commentId,
        c.created_at AS createdAt,
        c.last_updated AS lastUpdated,
        c.comment_status AS commentStatus,
        c.likes,
        c.dislikes,
        cc.content,
        c.user_id AS userId,
        c.post_id AS postId,
        COALESCE(c.parent_comment_id, 0) AS parentCommentId
    FROM Comment c
    JOIN CommentContent cc ON c.comment_id = cc.comment_id
    WHERE c.post_id = postIdParam
      AND c.comment_status = 'ACTIVE'
      AND (c.parent_comment_id IS NULL OR c.parent_comment_id = 0)
      AND c.last_updated = cc.last_updated
    ORDER BY c.created_at DESC;
END //
DELIMITER ;
