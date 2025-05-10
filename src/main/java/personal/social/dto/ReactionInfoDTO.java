package personal.social.dto;

public class ReactionInfoDTO {
    private long likes;
    private boolean isLike;

    private long dislikes;
    private boolean isDislike;

    private long shares;
    private boolean isShare;

    private long reports;
    private boolean isReport;

    public ReactionInfoDTO() {
    }

    public ReactionInfoDTO(long likes, boolean isLike, long dislikes, boolean isDislike, long shares, boolean isShare, long reports, boolean isReport) {
        this.likes = likes;
        this.isLike = isLike;
        this.dislikes = dislikes;
        this.isDislike = isDislike;
        this.shares = shares;
        this.isShare = isShare;
        this.reports = reports;
        this.isReport = isReport;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public boolean isDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public long getReports() {
        return reports;
    }

    public void setReports(long reports) {
        this.reports = reports;
    }

    public boolean isReport() {
        return isReport;
    }

    public void setReport(boolean report) {
        isReport = report;
    }
}
