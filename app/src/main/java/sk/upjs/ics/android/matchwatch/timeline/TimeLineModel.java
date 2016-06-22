package sk.upjs.ics.android.matchwatch.timeline;

import java.io.Serializable;

public class TimeLineModel implements Serializable {
    /*private String name;
        private int age;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }*/
    private String notif;
    private String team;
    private String type;
    private String time;
    private String player;
    private String assist1;
    private String assist2;
    private String duration;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAssist2() {
        return assist2;
    }

    public void setAssist2(String assist2) {
        this.assist2 = assist2;
    }

    public String getAssist1() {
        return assist1;
    }

    public void setAssist1(String assist1) {
        this.assist1 = assist1;
    }

}