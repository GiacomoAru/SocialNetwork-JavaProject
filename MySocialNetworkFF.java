import java.util.*;

public class MySocialNetworkFF extends MySocialNetwork implements SocialNetworkFF{

    /*
    Invariante: PerOgni string in reportedPost vale containsPost(string) && reportedPost.get(string) >= 1
    FunzioneAstratta: this.reportedPost = map che collega Stringhe id di post esistendi nel social network
            al numero di volte che quel post è stato segnalato
            this.maxReport = soglia di segnalazioni perchè un post sia considerato rimosso (>= rimosso). è 5 di base
    */

    private final Map<String, Integer> reportedPost = new HashMap<String, Integer>();
    private int maxReport = 5;

    private void addReportPost(Post p) throws PostAuthorNotFound, PostTargetNotFound, PostNotSupported {
        if(! containsUser(p.getAuthor())) throw new PostAuthorNotFound("Post:\n" + p.toString() +
                "\n Author not found");
        if(! containsPost(p.getText())) throw new PostTargetNotFound("Post:\n" + p.toString() +
                "\n Post target not found");
        if(p.getText().charAt(0) == '0'){
            if(reportedPost.containsKey(p.getText())){
                reportedPost.put(p.getText(), reportedPost.get(p.getText()) + 1);
            }
            else{
                reportedPost.put(p.getText(), 1);
            }

            generalPostList.add(p);
        }
        else throw new PostNotSupported("Post:\n" + p.toString() +
                "\nOnly post with post type == 0 can be reported");
    }
    @Override
    public void addPost(Post p) throws PostException {

        if(p == null) throw new NullPointerException("p == null");
        if(p.getType() == 4){
            addReportPost(p);
        }
        else super.addPost(p);
    }
    @Override
    public void addPostList(List<Post> pl) throws PostException{
        if(pl == null) throw new NullPointerException("PostList == null");
        for(Post p : pl){
            addPost(p);
        }
    }

    public int reportCount(String s) throws PostNotFoundException {
        if(s == null) throw new NullPointerException("p == null");
        if(!containsPost(s)) throw new PostNotFoundException("post not found. id: " + s);
        return reportedPost.getOrDefault(s, 0);
    }
    public Set<String> getReportedPostListId(){
        return Collections.unmodifiableSet(reportedPost.keySet());
    }
    public List<Post> getPostNotBanned(){
        List<Post> pl = new LinkedList<Post>();
        for(Post p: socialPostList){
            if(! (reportedPost.getOrDefault(p.getId(), 0) >= maxReport )) pl.add(p);
        }
        return pl;
    }
    public int getMaxReport(){
        return maxReport;
    }
    public void setMaxReport(int i){
        if( i < 1) throw new IllegalArgumentException("i can't <1");
        maxReport = i;
    }
}