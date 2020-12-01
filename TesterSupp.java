import java.util.*;

public class TesterSupp {
    public static void MyPostTest(){
        //test costruttore e eccezioni
        Post p;
        int eccezioni = 0;
        p = new MyPost("AUTORE", 0, "DESCRIZIONE");

        try{
            p = new MyPost("", 0, "DESCRIZIONE");
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        try{
            p = new MyPost("AUTORE", 0, "TESTO CON PIù DI 140 CARATTERI------------------------------" +
                    "-----------------------------------------------------------------------------------------");
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        try{
            p = new MyPost(null, 0, "DESCRIZIONE");
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        try{
            p = new MyPost("AUTORE", 0, null);
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        assert eccezioni == 4;

        String a = "AUTORE";
        String t = "TESTO";
        int tdp = 0;
        p = new MyPost(a , tdp , t);

        assert a.equals(p.getAuthor());
        assert t.equals(p.getText());
        assert tdp == p.getType();
        String id = "" + tdp + "." + p.getTimestampInMills() + "." + a;
        assert id.equals(p.getId());

        assert p.equals(p);
        assert !p.equals(new MyPost(a + "1", tdp, t));

        MyPost p1 = new MyPost("AUTORE 2", 0, "DESCRIZIONE 2");
        assert p.compareTo(p1) < 0;
    }
    public static void MyUserTest(){
        User u;
        int eccezioni = 0;

        try{
            u = new MyUser("", "DESCRIZIONE");
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        try{
            u = new MyUser(null, "DESCRIZIONE");
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        try{
            u = new MyUser("NOME", null);
        }catch(NullPointerException | IllegalArgumentException e){
            eccezioni++;
        }
        u = new MyUser("NOME", "");
        assert u.getName().equals("NOME");
        assert u.getDes().equals("");

        u.modifyDes("DESCRIZIONE");
        assert u.getDes().equals("DESCRIZIONE");

        try {
            u.linkPost(new MyPost("NOME DIVERSO", 0, "TEXTO POST"));
        } catch (PostNotAddedException e) {
            eccezioni++;
        }
        assert eccezioni == 4;

        for(int i = 0; i<50; i++){
            try {
                u.linkPost(new MyPost("NOME", 0, "TEXTO POST " + i));
            } catch (PostNotAddedException e) {
                e.printStackTrace();
            }
        }
        for(Post p: u.getLinkedPost()){
            assert p.getAuthor().equals("NOME");
        }

        u = new MyUser("NOME", "DESCRIZIONE 1");
        assert u.equals(new MyUser("NOME", "DESCRIZIONE 2"));
        assert !u.equals(new MyUser("NOME DIVERSO", "DESCRIZIONE"));
    }
    public static void MySocialNetworkTest() throws InterruptedException, UserNotFoundException, PostException {
        SocialNetwork sn1 = new MySocialNetwork();
        SocialNetwork sn2 = new MySocialNetwork();

        Map<String, Set<String>> Microblog = new HashMap<String, Set<String>>();
        List<Post> lp = TesterSupp.createColl(100);

        //for(Post p : lp) System.out.println(p.toString());

        for(Post p: lp) {
            try {
                sn2.addPost(p);
            } catch (PostException e) {
                e.printStackTrace();
            }
        }
        try {
            sn1.addPostList(lp);
        } catch (PostException e) {
            e.printStackTrace();
        }
        {//correttezza add e addPostList
            for (Post p : lp) {
                switch (p.getType()) {
                    case 0:
                        assert sn1.containsPost(p.getId());
                        assert sn2.containsPost(p.getId());
                        break;
                    case 1:
                        assert sn1.containsUser(p.getAuthor());
                        assert sn2.containsUser(p.getAuthor());
                        Microblog.put(p.getAuthor(), new HashSet<String>());
                        break;
                    case 3:
                        if (Microblog.get(p.getAuthor()).contains(p.getText()))
                            Microblog.get(p.getAuthor()).remove(p.getText());
                        else Microblog.get(p.getAuthor()).add(p.getText());
                }
            }

            assert sn1.getMicroblog().equals(Microblog);
            assert sn2.getMicroblog().equals(Microblog);

            List<Post> postAggiunti = new LinkedList<Post>();
            for (Post p : lp) {
                if (p.getType() == 0) postAggiunti.add(p);
            }
            assert sn1.getAllSocialPost().equals(postAggiunti);

            postAggiunti = new LinkedList<Post>();
            for (Post p : lp) {
                if (p.getType() != 0) postAggiunti.add(p);
            }
            assert sn1.getAllNotSocialPost().equals(postAggiunti);

        }
        {//mentionedUser
            List<String> autori = new LinkedList<String>();
            for (Post p : lp) {
                if (!autori.contains(p.getAuthor())) autori.add(p.getAuthor());
            }
            assert sn1.getMentionedUser().equals(autori);
        }
        {//written by
            for (int i = 0; i < 15; i++) {
                List<Post> postDiA = new LinkedList<Post>();
                for (Post p : lp) {
                    if (p.getAuthor().equals("AUTORE: " + i)) postDiA.add(p);
                }
                assert sn1.writtenBy("AUTORE: " + i).equals(postDiA);
            }
            assert MySocialNetwork.writtenBy( lp,"AUTORE: 5").equals(sn1.writtenBy("AUTORE: 5"));
        }
        {//containing all e non solo
            List<Post> postConTesto = new LinkedList<Post>();
            for (Post p : lp) {
                if (p.getAuthor().contains("AUTORE: 4") || p.getAuthor().contains("AUTORE: 2")) postConTesto.add(p);
                if (p.getText().contains("AUTORE: 4") || p.getText().contains("AUTORE: 2")) postConTesto.add(p);
            }
            List<String> gigio = new LinkedList<String>();
            gigio.add("AUTORE: 4");
            gigio.add("AUTORE: 2");
            assert MySocialNetwork.containing(gigio, lp).containsAll(postConTesto);
            assert postConTesto.containsAll(MySocialNetwork.containing(gigio, lp));
            assert sn1.containingAll(gigio).containsAll(postConTesto);//containing all non mantiene l'ordinamento
            assert postConTesto.containsAll(sn1.containingAll(gigio));

            postConTesto = new LinkedList<Post>();
            for (Post p : lp) {
                if (p.getType() == 0 && (p.getAuthor().contains("AUTORE: 4") || p.getAuthor().contains("AUTORE: 2"))) postConTesto.add(p);
                if (p.getType() == 0 && (p.getText().contains("AUTORE: 4") || p.getText().contains("AUTORE: 2"))) postConTesto.add(p);
            }
            assert sn1.containing(gigio).equals(postConTesto);
        }
        {//influencers
            List<Post> postInfluencers = new LinkedList<Post>();
            Post pi;
            for(int i = 0; i<50; i++){
                pi = new MyPost("AUTORE: " + i, 1, "");
                postInfluencers.add(pi);
            }
            for(int i = 0; i<25; i++){
                for(int j = 25; j<i+26; j++){
                    pi = new MyPost("AUTORE: " + i, 3, "AUTORE: " + j);
                    postInfluencers.add(pi);
                }
            }

            SocialNetwork social = new MySocialNetwork();
            social.addPostList(postInfluencers);
            social.setMaxInfluencers(20);
            assert social.getMaxInfluencers() == 20;

            List<String> ls = new LinkedList<String>();
            for(int i = 25; i<50; i++) ls.add("AUTORE: " + i);

            assert social.influencers().equals(ls.subList(0, 20));
            assert social.AsegueB("AUTORE: 0", "AUTORE: 25");
        }
        {//guess Microblog
            assert sn1.getMicroblog().equals(MySocialNetwork.guessMicroblog(lp));
        }
        {//throws
            int eccezioni = 0;
            try {
                sn1.addPost(new MyPost("AUTORE INESISTENTE", 0, ""));
            }catch(PostAuthorNotFound e){
                eccezioni ++;
            }
            try {
                sn1.writtenBy("AUTORE INESISTENTE");
            }catch(UserNotFoundException e){
                eccezioni ++;
            }
            try {
                sn1.addPost(new MyPost("AUTORE: 0", 3, "AUTORE: 0"));
            }catch(PostNotSupported e){
                eccezioni ++;
            }
            assert eccezioni == 3;
        }

        {//social network family friendly
            SocialNetworkFF snff = new MySocialNetworkFF();
            snff.addPostList(lp);
            List<String> listio = new LinkedList<String>();

            for(Post p: lp){
                if(p.getType() == 0 && p.getAuthor().charAt(8) == '1'){
                    snff.addPost(new MyPost("AUTORE: 0", 4, p.getId()));
                    listio.add(p.getId());
                }
            }

            Iterator<String> it = listio.listIterator();

            for(int j = 0; j<4; j++) {
                String stri = it.next();
                for (int i = 0; i < 50; i++) {
                    snff.addPost(new MyPost("AUTORE: 0", 4, stri));
                }
            }
            assert snff.getReportedPostListId().containsAll(listio);
            assert listio.containsAll(snff.getReportedPostListId());//non mantiene l'ordinamento

            snff.setMaxReport(50);
            Iterator<Post> iter = lp.listIterator();
            //molto complicato per via dell'eccezione che però non viene lanciata
            while( iter.hasNext() ){
                Post p = iter.next();
                if(p.getType() != 0 || snff.reportCount(p.getId()) >= snff.getMaxReport()) iter.remove();
            }
            assert snff.getPostNotBanned().equals(lp);

            int eccezioni = 0;
            try {
                snff.addPost(new MyPost("AUTORE: 0", 4, "IDNONESISTENTE"));
            }catch(PostTargetNotFound e){
                eccezioni++;
            }
            try {
                snff.reportCount("IDNONESISTENTE");
            }catch(PostNotFoundException e){
                eccezioni++;
            }
            assert eccezioni == 2;
        }
    }

    //genera liste di post con n utenti creati e altri post in cui gli utenti si seguono a vicenda random
    private static List<Post> createColl(int n) throws InterruptedException {
        List<Post> SP = new LinkedList<Post>();
        Post p;
        Random r = new Random();

        //n post di aggiunta utente
        for(int i = 0; i<n; i++){
            p = new MyPost("AUTORE: " + i, 1, "DESCRIZIONE LOLLOSA: " + i);
            SP.add(p);
            Thread.sleep(1);
        }

        //crea un array di n numeri e lo randomizza
        Vector<Integer> numeri = new Vector<Integer>();
        for(int i = 0; i<n; i++) numeri.add(i);
        for(int i = 0; i<n; i++){
            int dummy = r.nextInt(n);
            int dummy2 = numeri.elementAt(dummy);
            numeri.set(dummy, numeri.elementAt(i));
            numeri.set(i, dummy2);
        }

        for(int i = 0; i<n; i++){
            int j = 0;
            for(Integer g: numeri){
                p = new MyPost("AUTORE: " + i, 3, "AUTORE: " + g);
                if(!p.getAuthor().equals(p.getText()))SP.add(p);
                if(j>=i) break;
                j++;
                Thread.sleep(1);
            }
        }

        for(int i = 0; i<n; i++){
            p = new MyPost("AUTORE: " + i, 0, "CIAO MONDO " + i);
            SP.add(p);
            Thread.sleep(1);
            p = new MyPost("AUTORE: " + i, 0, "CIAO MONDO " + i + 1);
            SP.add(p);
            Thread.sleep(1);
            p = new MyPost("AUTORE: " + i, 0, "CIAO MONDO " + i + 2);
            SP.add(p);
            Thread.sleep(1);
        }

        for(int i = 1; i<n; i++){
            int g = numeri.elementAt(r.nextInt(i));
            if(g != i) {
                p = new MyPost("AUTORE: " + i, 3, "AUTORE: " + g);
                SP.add(p);
            }
            Thread.sleep(1);
        }

        return SP;
    }
}
