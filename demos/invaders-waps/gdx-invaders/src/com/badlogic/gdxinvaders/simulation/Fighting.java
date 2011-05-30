package com.badlogic.gdxinvaders.simulation;

public class Fighting  implements Comparable{
	private String name;
	private int score;
	private String phoneName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Fighting(String name,int score,String phoneName){
		this.name = name;
		this.score = score;
		this.phoneName = phoneName;
	}
	/**
	 * @return the phoneName
	 */
	public String getPhoneName() {
		return phoneName;
	}
	/**
	 * @param phoneName the phoneName to set
	 */
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	@Override
	public int compareTo(Object o) {
		return ((Fighting)o).getScore() - this.getScore();
	}
}
