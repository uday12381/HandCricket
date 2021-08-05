package view;
class ThreadBuilder extends Thread{
	Match play;
	HandCricket hk;
	static int count; 
	static int target;
	public ThreadBuilder(HandCricket hk) {
		count++;
		this.hk = hk;
		if(count == 1) {
			this.play = new Match(hk.teamABat, hk.teamBBowl ,hk, this);
			this.play.setTarget(14562);
		}
		if(count == 2) {
			this.play = new Match(hk.teamBBat, hk.teamABowl, hk, this);
			this.play.setTarget(target);
		}
		for(int i = 0; i < 6; i++) {
			try {
				play.initializeBatsman(i, hk.Connection.batsmanDetails(play.initializeBatsman(i)));
			} catch(Exception e) {
				e.printStackTrace();
				return ;
			}
		}
		for(int i=0 ; i < 6; i++) {
			try {
				play.initializeBowler(i, hk.Connection.bowlerDetails(play.initializeBowler(i)));
			} catch(Exception e) {
				e.printStackTrace();	
				return ;
			}
		}
		start();
	}

	public void run() {
		while(!play.isInningsCompleted()) {
	    	while(!hk.isButtonClicked);
        	play.ball(hk.buttonClicked);
        	hk.isButtonClicked = false;
		}
		if(count == 1) { 
			hk.tf.setText("Innings completed");
		    target = play.getTotalScore();
			for(int i = 0; i < 6; i++) {
		        hk.Connection.setDetails(play.SQLstatements(i, true));
		    }
		    for(int i = 0; i < 6; i++) {
		        hk.Connection.setDetails(play.SQLstatements(i, false));
		    }

			hk.targetJb.setBounds(200, 650, 100, 23);
			hk.TeamP.add(hk.targetJb);
			hk.displayTarget.setText("" + target);
			hk.TeamP.add(hk.displayTarget);
			hk.secondInnings = new threadBuilder(hk);
		} else if(count == 2) {
			if(play.getTarget() < play.getTotalScore()) {
				hk.displayCommentary.setText(hk.sTeam+" won the game");
			}
			else {
				hk.displayCommentary.setText(hk.oTeam+" won the game");
			}
			for(int i = 0; i < 6; i++) {
	        	hk.Connection.setDetails(play.SQLstatements(i, true));
	        }
	        for(int i = 0; i < 6; i++) {
	        	hk.Connection.setDetails(play.SQLstatements(i, false));
	        }
	        hk.Connection.closeConnection();
		}
	}
}