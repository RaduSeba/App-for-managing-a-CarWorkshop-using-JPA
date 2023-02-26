package uo.ri.cws.application.service.client;

/**
 * This service is intended to be used by the Cashier
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface ClientCrudService {

	// ...


	public static class ClientDto {

		public String id;
		public long version;

		public String dni;
		public String name;
		public String surname;
		public String addressStreet;
		public String addressCity;
		public String addressZipcode;
		public String phone;
		public String email;

	}

}
