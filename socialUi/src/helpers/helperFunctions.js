import { UserReactionServices } from "src/services/api";

// Helper function to get auth token
const getAuthToken = () => {
    try {
      return JSON.parse(localStorage.getItem("authUser"));
    } catch (error) {
      console.error("Error parsing authUser:", error);
      return null;
    }
  };

export async function userPostCommentReaction (objectType, objectId, reactionType) {
    const token = getAuthToken();
    if (!token) {
      console.error('No authentication token found');
      return;
    }
    try {
        const response = await UserReactionServices.userReaction({
            objectType,
            objectId,
            reactionType
        }, token);
        if (response.status === 200) {
          return response
        }
      } catch (error) {
        console.error('Error reacting to post:', error);
      }
}