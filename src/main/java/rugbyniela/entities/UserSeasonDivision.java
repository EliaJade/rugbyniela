package rugbyniela.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSeasonDivision {

	private long id;
	private int points;
	private Date registerDate;
	private List<Bet> bets;
	private Season season;
	private Division division;

	public UserSeasonDivision(long id, int points, Date registerDate, Season season, Division division) {
		super();
		this.id = id;
		this.points = points;
		this.registerDate = registerDate;
		this.bets = new ArrayList();
		this.season = season;
		this.division = division;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public List<Bet> getBets() {
		return bets;
	}

	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

}
