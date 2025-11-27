package rugbyniela.service;

public interface UserService {

	void register();
	void update();
	void addAddress();
	void removeAddress();
	void updateAddress();
	void fetchUserById();
	void changePassword();
	void recoveryAccount();
	void login();//TODO: this method possibly belong to security service
	void logout();//TODO: this method possibly belong to security service
	void registerInSeason();
	void fetchSeasonPoints();
	void fetchSeasonUserHaveBeenRegistered();
	void registerInCoalition();
	void dismissOfCoalition();
	void fetchCoalitionUserHaveBeenRegistered();
	void sendNotificationToUser();
	void sendNotificationToUsers();
}
