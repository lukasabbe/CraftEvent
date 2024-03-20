package me.lukasabbe.craftevent;

import org.bukkit.entity.Player;

public class CraftEventPlayer {
    public Player player;

    private boolean isSpectating = false;

    private CraftEventPlayer teamPlayer = null;

    public CraftEventPlayer(Player player) {
        this.player = player;
    }

    public CraftEventPlayer getTeamPlayer() {
        return teamPlayer;
    }

    public void setTeamPlayer(CraftEventPlayer teamPlayer) {
        this.teamPlayer = teamPlayer;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public void setSpectating(boolean spectating) {
        if(spectating) {
            teamPlayer.setTeamPlayer(null);
            teamPlayer = null;
        }
        isSpectating = spectating;
    }
}
