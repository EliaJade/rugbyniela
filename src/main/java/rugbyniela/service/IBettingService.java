package rugbyniela.service;

public interface IBettingService {

	void submitTicket();
	void cancelTicket();
	void fetchUserSeasonTickets();
	void fetchUserSeasonTicketsByMatchDay();
}
