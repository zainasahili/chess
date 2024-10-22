package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuth implements AuthDAO{

    HashSet<AuthData> db = new HashSet<>();

    @Override
    public void add(AuthData data) {
            db.add(data);
    }

    @Override
    public void clear() {
        db = new HashSet<>();

    }
}
