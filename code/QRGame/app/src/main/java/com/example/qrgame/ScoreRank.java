package com.example.qrgame;

import android.app.Dialog;

public class ScoreRank {
    private String name;

    private int score;

    private int rank;

    public ScoreRank(String name, int score){
        this.score = score;
        this.name = name;
        this.rank = 0;
    }

    public void SetName(String name)
    {
        this.name = name;
    }

    public String GetName()
    {
        return name;
    }

    public void SetScore(int score)
    {
        this.score = score;
    }

    public void SetScore(String score)
    {
        try
        {
            this.score = Integer.parseInt(score);
        }
        finally {

        }

    }

    public String GetScoreString()
    {
        return Integer.toString(score);
    }

    public int GetScoreInt()
    {
        return score;
    }

    public void SetRank(int rank)
    {
        this.rank = rank;
    }

    public void SetRank(String rank)
    {
        try
        {
            this.rank = Integer.parseInt(rank);
        }
        finally
        {

        }
    }

    public String GetRankString()
    {
        return Integer.toString(rank);
    }

    public int GetRankInt()
    {
        return rank;
    }
}
