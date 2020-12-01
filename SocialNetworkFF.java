import java.util.List;
import java.util.Set;

public interface SocialNetworkFF extends SocialNetwork {

    //REQUIRES: p.getType = {0,1,3,4} &&
    //      p.getType == 4 -> containsUser(p.getAuthor) && containsPost(p.getText) &&
    //                      typeOf(p.getText) == 0
    //MODIFY: none
    //THROWS: NullPointerException, PostException
    //EFFECTS: se p.getType == 4 -> aggiunge il post al social network e aggiunge la segnalazione del post
    public void addPost(Post p) throws PostException;

    //REQUIRES: PerOgni post nella lista deve valere: p.getType = {0,1,3,4} &&
    //      p.getType == 4 -> containsUser(p.getAuthor) && containsPost(p.getText) &&
    //                        typeOf(p.getText) == 0
    //MODIFY: this
    //THROWS: PostException, NullPointerException
    //EFFECTS: PerOgni post nella lista in ordine naturale addPost(post)
    public void addPostList(List<Post> pl) throws PostException;

    //REQUIRES: s != null && containsPost(s)
    //MODIFY: none
    //THROWS: NullPointerException, PostNotFoundException
    //EFFECTS: ritorna il numero di volte che il post s è stato segnalato
    public int reportCount(String s) throws PostNotFoundException;

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: restituisce un set non modificabile delle stringhe id di post segnalati almeno 1 volta
    public Set<String> getReportedPostListId();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: restituisce una lista non modificabile dei post sociali del social network che non sono stati rimossi per le segnalazioni
    public List<Post> getPostNotBanned();

    //REQUIRES: true
    //MODIFY: none
    //THROWS: none
    //EFFECTS: restituisce la soglia sopra la quale(>=) un post viene considerato rimosso
    public int getMaxReport();

    //REQUIRES: i >= 1
    //MODIFY: none
    //THROWS: IllegalArgumentException
    //EFFECTS: modifica la soglia sopra la quale(>=) un post viene considerato rimosso
    public void setMaxReport(int i);
}
