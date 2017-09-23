package Server;

import java.util.HashMap;

public class BaseAuthService {
    private class Entry{
        private String login;
        private String password;
        private String nick;
        public Entry (String login, String password, String nick){
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
    private static HashMap<String, Entry> entries;

    public BaseAuthService (){
        entries = new HashMap <> (  );

    }
}
