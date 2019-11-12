package bean;

public class Page {

    private int pageId;
    private int authorUid;
    private String authorUsername;
    private int itemId;
    private int catId;
    private String pageTitle;
    private String pageComments;
    private String pageContent;
    private int sNumber;
    private long addTime;


    public Page(int pageId, int authorUid, String authorUsername, int itemId, int catId, String pageTitle, String pageComments, String pageContent, int sNumber, long addTime) {
        this.pageId = pageId;
        this.authorUid = authorUid;
        this.authorUsername = authorUsername;
        this.itemId = itemId;
        this.catId = catId;
        this.pageTitle = pageTitle;
        this.pageComments = pageComments;
        this.pageContent = pageContent;
        this.sNumber = sNumber;
        this.addTime = addTime;
    }

    public Page() {
    }


    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(int authorUid) {
        this.authorUid = authorUid;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageComments() {
        return pageComments;
    }

    public void setPageComments(String pageComments) {
        this.pageComments = pageComments;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageId=" + pageId +
                ", authorUid=" + authorUid +
                ", authorUsername='" + authorUsername + '\'' +
                ", itemId=" + itemId +
                ", catId=" + catId +
                ", pageTitle='" + pageTitle + '\'' +
                ", pageComments='" + pageComments + '\'' +
                ", pageContent='" + pageContent + '\'' +
                ", sNumber=" + sNumber +
                ", addTime=" + addTime +
                '}';
    }
}
