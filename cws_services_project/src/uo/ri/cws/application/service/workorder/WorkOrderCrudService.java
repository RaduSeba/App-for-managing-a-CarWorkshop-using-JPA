package uo.ri.cws.application.service.workorder;

import java.time.LocalDateTime;

/**
 * This service is intended to be used by the Mechanic
 * It follows the ISP principle (@see SOLID principles from RC Martin)
 */
public interface WorkOrderCrudService {

	// ...

	public static class WorkOrderDto {

		public String id;
		public long version;

		public String vehicleId;
		public String description;
		public LocalDateTime date;
		public double total;
		public String state;

		// might be null
		public String mechanicId;
		public String invoiceId;

	}
}
