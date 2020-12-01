import java.util.*;

public class MySocialNetwork implements SocialNetwork{

    /*
    Invariante: socialPostList & generalPostList & micorblog & UserMap != null
            PerOgni post in socialPostList vale p.getType == 0
            PerOgni post in generalPostList vale p.getType == {1,3}
            PerOgni post in socialPostList o generalPostList vale p.author è contenuto in microblog.keySet e userMap.keySet
            PerOgni post in socialPostList o generalPostList vale che p è contenuto anche in author.linkedPost

            PerOgni stringa in userMap essa è collegata ad un User != null e è contenuta in Micrblog
            PerOgni stringa1 contenuta in Microblog.keySet è contenuta in UserMap
                e per ogni stringa2 in Microblog.get(stringa1) essa è contenuta in Microblog.keySet e UserMap.keySet

    FunzioneAstratta: this.socialPostList = insieme di post sociali aggiunti al social network
            this.generalPostList = insieme di post non sociali aggiunti al social network
            this.Microblog = map che mappa nomi di utenti ad altri nomi di utenti seguiti dal primo
            this.UserMap = map che mappa nomi di utenti a istanze di User
    */

    //una lista di post sociali
    protected final LinkedList<Post> socialPostList = new LinkedList<Post>();
    protected final LinkedList<Post> generalPostList = new LinkedList<Post>();
    protected final HashMap<String, Set<String>> Microblog = new HashMap<String, Set<String>>();
    protected final HashMap<String, User> UserMap = new HashMap<String, User>();
    private int maxInfluencers = 10;

    //istanza
    public void addPost(Post p) throws PostException {
        if(p == null) throw new NullPointerException("Post = null\n");
        switch(p.getType()){
            case 0:
                addSocialPost(p);
            break;
            case 1:
                addNewUserPost(p);
            break;
            case 3:
                addFollowPost(p);
            break;
            default: throw new PostNotSupported("PostType not supported");
        }

    }
    private void addSocialPost(Post p) throws PostAuthorNotFound, PostNotAddedException {
        //rivedere la storia dell'id e dei post con uguale timestamp
        //eccezione se l'autore del post non esiste
        if(! UserMap.containsKey(p.getAuthor())) throw new PostAuthorNotFound("Post:\n" + p.toString() +
                "\n Author not found");
        socialPostList.add(p);
        UserMap.get(p.getAuthor()).linkPost(p);
    }
    private void addNewUserPost(Post p){
        if(UserMap.containsKey(p.getAuthor())){
            UserMap.get(p.getAuthor()).modifyDes(p.getText());
        }
        else {
            UserMap.put(p.getAuthor(), new MyUser(p.getAuthor(), p.getText()));
            Microblog.put(p.getAuthor(), new TreeSet<String>());
        }
        try {
            UserMap.get(p.getAuthor()).linkPost(p);
        } catch (PostNotAddedException e) {
            throw new RuntimeException("Undefined error");
        }
        generalPostList.add(p);
    }
    private void addFollowPost(Post p) throws PostNotAddedException, PostAuthorNotFound, PostTargetNotFound, PostNotSupported {
        //user not found
        if(! UserMap.containsKey(p.getAuthor())) throw new PostAuthorNotFound("Post:\n" + p.toString());
        //user da seguire non trovato
        if(! UserMap.containsKey(p.getText())) throw new PostTargetNotFound("Post:\n" + p.toString());
        //un utente non può seguire se stesso
        if(p.getAuthor().equals(p.getText())) throw new PostNotSupported("Post:\n" + p.toString());

        //se segue già allora unfollow
        if( Microblog.get(p.getAuthor()).contains(p.getText()) ) {
            Microblog.get(p.getAuthor()).remove(p.getText());
        }
        //altrimenti follow
        else {
            Microblog.get(p.getAuthor()).add(p.getText());
        }

        UserMap.get(p.getAuthor()).linkPost(p);
        generalPostList.add(p);
    }
    //istanza
    public void addPostList(List<Post> pl) throws PostException {
        if(pl == null) throw new NullPointerException("PostList == null");
        for(Post p : pl){
            addPost(p);
        }
    }
    public List<String> getMentionedUser(){
        return MySocialNetwork.getMentionedUser(generalPostList);
    }
    public List<Post> writtenBy(String username) throws UserNotFoundException {
        if(username == null) throw new NullPointerException("Username == null");
        if(!UserMap.containsKey(username)) throw new UserNotFoundException("the user: " + username + " not exixts");
        return UserMap.get(username).getLinkedPost();
    }
    public List<Post> containing(List<String> words){
        return MySocialNetwork.containing(words, socialPostList);
    }
    public List<Post> containingAll(List<String> words){
       List<Post> lp = MySocialNetwork.containing(words, generalPostList);
       lp.addAll(MySocialNetwork.containing(words, socialPostList));

       return lp;
    }
    public List<String> influencers(){
        return MySocialNetwork.influencers(Microblog, maxInfluencers);
    }
    public void setMaxInfluencers(int i){
        if(i<1) throw new IllegalArgumentException("i must >= 1");
        maxInfluencers = i;
    }

    //lavorare su dati esterni

    //REQUIRES: ps != null && PerOgni post nella lista deve valere:
    //    p != null && p.getType = {0,1,3} &&
    //    p.getType = 3 -> containsUser(p.getAuthor) && containsUser(p.getText) && p.getAuthor != p.getText
    //MODIFY: none
    //THROWS: NullPointerException, PostTargetNotFound, PostAuthorNotFound, PostNotSupported
    //EFFECTS: ritorna una map che mappa stringhe(nomi di utenti) a set di stringhe(nomi di utenti seguiti dal primo) derivata
    //  dalla lista di post passata come parametro. se ps.isEmpty ritorna una map vuota
    public static Map<String, Set<String>> guessMicroblog(List<Post> ps) throws PostTargetNotFound, PostAuthorNotFound, PostNotSupported {
        if(ps == null) throw new NullPointerException("PostList == null");

        Map<String, Set<String>> retMap = new TreeMap<String, Set<String>>();

        for(Post p: ps){
            switch(p.getType()){
                case 1:
                    if(! retMap.containsKey(p.getAuthor()) ){
                        retMap.put(p.getAuthor(), new TreeSet<String>());
                    }
                break;
                case 3:
                    //user not found
                    if(! retMap.containsKey(p.getAuthor())) throw new PostAuthorNotFound("Post:\n" + p.toString());
                    //user da seguire non trovato
                    if(! retMap.containsKey(p.getText())) throw new PostTargetNotFound("Post:\n" + p.toString());
                    //un utente non può seguire se stesso
                    if(p.getAuthor().equals(p.getText())) throw new PostNotSupported("Post:\n" + p.toString());

                    //se segue già allora unfollow
                    if( retMap.get(p.getAuthor()).contains(p.getText()) ) {
                        retMap.get(p.getAuthor()).remove(p.getText());
                    }
                    //altrimenti follow
                    else {
                        retMap.get(p.getAuthor()).add(p.getText());
                    }
                break;
            }
        }

        return retMap;
    }

    //REQUIRES: followers != null && n>=1
    //MODIFY: none
    //THROWS: NullPointerException, IllegalArgumentException
    //EFFECTS: ritorna una lista contenente i primi n nomi della lista
    //  di user presenti in followers ordinata in modo decresscente rispetto alla cardinalità dell'insieme di utenti
    //  che lo seguono. se followers.isEmpty ritorna una lista vuota
    public static List<String> influencers(Map<String, Set<String>> followers, int n){

        if(n<1) throw new IllegalArgumentException("n must be >= 1");
        if(followers == null) throw new NullPointerException("followers == null");
        Map<String, Integer> PMap = new HashMap<String, Integer>();

        for(String s1: followers.keySet()) PMap.put(s1, 0);
        for(String s1: followers.keySet()){
            for(String s2: followers.get(s1)){
               PMap.put(s2, PMap.get(s2) + 1);
            }
        }

        Vector<String> userV = new Vector<String>(followers.keySet());

        userV.sort(new userFollowerComparator(PMap));
        return userV.subList(0, Math.min(n, userV.size()));
    }

    //REQUIRES: PostList != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna una lista di stringhe che contengono gli autori dei Post contenuti in PostList, seguendo l'ordine della PostList.
    //  se PostList.isEmpty ritorna una lista vuota
    public static List<String> getMentionedUser(List<Post> PostList){

        if(PostList == null) throw new NullPointerException("PostList == null");
        List<String> retList = new LinkedList<String>();
        for(Post p: PostList){
            if( !retList.contains(p.getAuthor()) ) retList.add(p.getAuthor());
        }
        return retList;
    }

    //REQUIRES: ps != null && username != null
    //MODIFY: none
    //THROWS: UserNotFoundException, NullPointerException
    //EFFECTS: ritorna tutti i post contenuti in ps tale che p.getAuthor = username rispettando l'ordine della lista ps
    //  se ps.isEmpty ritorna lista vuota
    public static List<Post> writtenBy(List<Post> ps, String username){
        if(username == null) throw  new NullPointerException("username == null");

        List<Post> retList = new LinkedList<Post>();
        for(Post p: ps){
            if(p.getAuthor().equals(username)) retList.add(p);
        }
        return retList;
    }

    //REQUIRES: words != null && pl != null
    //MODIFY: none
    //THROWS: NullPointerException
    //EFFECTS: ritorna la lista di tutti i PostSociali presenti in pl che contengono almeno una parola contentuta
    // nella lista di parole passate come parametro. ritorna lista vuota se pl è vuota
    public static List<Post> containing(List<String> words, List<Post> pl){
        if(words == null) throw new NullPointerException("words list == null");
        if(pl == null) throw new NullPointerException("pl == null");

        List<Post> retList = new LinkedList<Post>();
        for(Post p: pl){
            for(String s: words) {
                if(p.getText().contains(s) || p.getAuthor().contains(s)) retList.add(p);

            }
        }
        return retList;
    }


    //observer vari strani
    public boolean containsPost(String s){
        if(s == null) throw new NullPointerException("PostId == null");
        if(s.charAt(0) == '0') {
            for (Post p : socialPostList) {
                if (p.getId().equals(s)) return true;
            }
        }
        else {
            for (Post p : generalPostList) {
                if (p.getId().equals(s)) return true;
            }
        }
        return false;
    }
    public boolean containsUser(String u){
        if(u == null) throw new NullPointerException("PostId == null");
        return UserMap.containsKey(u);
    }
    public boolean AsegueB(String a, String b) throws UserNotFoundException {
        if(a == null || b == null) throw new NullPointerException("a or b == null");
        if(!containsUser(a) || !containsUser(b)) throw new UserNotFoundException(a + " or " + b + " do not exist");
        return Microblog.get(a).contains(b);
    }

    //observer seri
    public List<Post> getAllSocialPost(){
        return Collections.unmodifiableList(socialPostList);
    }
    public List<Post> getAllNotSocialPost(){
        return Collections.unmodifiableList(generalPostList);
    }
    public Map<String, Set<String>> getMicroblog(){
        return Collections.unmodifiableMap(Microblog);
    }
    public int getMaxInfluencers() {
        return maxInfluencers;
    }

    private static class userFollowerComparator implements Comparator<String> {
        Map<String, Integer> PMap;
        public userFollowerComparator(Map<String, Integer> m){
            PMap = m;
        }

        @Override
        public int compare(String o1, String o2) {
            return PMap.get(o2) - PMap.get(o1);
        }
    }
}
