package rugbyniela.service;

public interface BettingService {

	void submitTicket();

	void cancelTicket();

	void fetchUserSeasonTickets();

	void fetchUserSeasonTicketsByMatchDay();
}
