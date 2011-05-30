package com.badlogic.gdxinvaders.simulation;

import java.util.Comparator;
import com.badlogic.gdxinvaders.simulation.Fighting;

public class FightingComparator implements Comparator<Fighting> {

	@Override
	public int compare(Fighting o1, Fighting o2) {
		return o1.getScore() - o1.getScore();
	}

}
