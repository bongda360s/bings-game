package com.badlogic.gdxinvaders.simulation;

import java.util.Comparator;

public class FightingComparator implements Comparator<Fighting> {

	@Override
	public int compare(Fighting o1, Fighting o2) {
		// TODO Auto-generated method stub
		return o1.getScore() - o2.getScore();
	}

}
