package org.axway.grapes.server.db;

public class DBRegExp {
    final String regExp;

    public DBRegExp(final String regExp) {
        this.regExp = regExp;
    }

    public String toString() {
        return regExp;
    }
}
