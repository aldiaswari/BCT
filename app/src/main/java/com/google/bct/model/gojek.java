package com.google.bct.model;

public class gojek {
    private String ac;
    private String av;

    private String di;
    private String li;
    private String me;
    private String si;
    private String tok;

    public gojek(){
    }

    public gojek(String ac, String av, String di, String li, String me, String si, String tok) {
        this.ac = ac;
        this.av = av;
        this.di = di;
        this.li = li;
        this.me = me;
        this.si = si;
        this.tok = tok;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getAv() {
        return av;
    }

    public void setAv(String av) {
        this.av = av;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getLi() {
        return li;
    }

    public void setLi(String li) {
        this.li = li;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getTok() {
        return tok;
    }

    public void setTok(String tok) {
        this.tok = tok;
    }
}
