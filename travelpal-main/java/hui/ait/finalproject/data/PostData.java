package hui.ait.finalproject.data;

public class PostData {

    private String postTitle;
    private String postLocation;
    private String postBody;

    public PostData(String postTitle, String postLocation, String postBody) {
        this.postTitle = postTitle;
        this.postLocation = postLocation;
        this.postBody = postBody;
    }

    public String getTitle() {
        return postTitle;
    }

    public void setTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

}
